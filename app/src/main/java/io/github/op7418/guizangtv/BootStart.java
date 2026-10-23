package io.github.op7418.guizangtv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/** Opt-in, once-per-full-boot launch. Never handles screen-on, HDMI or Home keys. */
public final class BootStart {
    private static final String PREFS = "boot-start";
    private static final String EXTRA = "guizang.boot-count";
    private static final String EXTRA_WAKE = "guizang.standby-wake";
    private static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
    static int mode(Context c) {
        SharedPreferences values = prefs(c);
        if (values.contains("mode")) return BootStartPolicy.mode(values.getInt("mode", BootStartPolicy.MODE_OFF));
        return values.getBoolean("enabled", false) ? BootStartPolicy.MODE_BOOT_ONLY : BootStartPolicy.MODE_OFF;
    }
    static boolean enabled(Context c) { return mode(c) != BootStartPolicy.MODE_OFF; }
    static String label(Context c) {
        switch (mode(c)) {
            case BootStartPolicy.MODE_BOOT_ONLY: return "仅完整重启";
            case BootStartPolicy.MODE_BOOT_AND_WAKE: return "完整重启＋待机唤醒";
            default: return "已关闭";
        }
    }
    static String note(Context c) {
        String target = Settings.System.getString(c.getContentResolver(), "start_3rd_app");
        String entry = c.getPackageName().equals(target) ? "系统入口已配置" : "系统入口当前为空或不是归藏";
        return entry + "。待机唤醒模式只在已验证的 MiTV-MFTP0 上开放；Home 键仍返回小米桌面，且不能跳过系统启动阶段的广告。";
    }
    private static int bootCount(Context c) {
        return Settings.Global.getInt(c.getContentResolver(), Settings.Global.BOOT_COUNT, -1);
    }
    static void enter(Activity a) {
        int boot = bootCount(a);
        SharedPreferences values = prefs(a);
        int attempted = values.getInt("attempted", -1);
        long elapsed = SystemClock.elapsedRealtime();
        boolean wake = boot == attempted;
        String reason = BootStartPolicy.skipReason(XiaomiControls.enabled(), XiaomiControls.standbyWakeSupported(),
                mode(a), boot, attempted, elapsed, values.getLong("last-attempt-elapsed", -1));
        if (reason != null) { Log.i("GuiZangBoot", reason); return; }
        SharedPreferences.Editor update = values.edit().putInt("attempted", boot)
                .putLong("last-attempt-elapsed", elapsed)
                .putString("status", wake ? "系统已调用待机唤醒入口，等待界面确认" : "系统已调用完整启动入口，等待界面确认");
        if (!update.commit()) return;
        try {
            a.startActivity(new Intent(a, FrameHomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra(EXTRA, boot).putExtra(EXTRA_WAKE, wake));
        } catch (RuntimeException error) {
            status(a, "启动失败：" + error.getClass().getSimpleName());
            Log.w("GuiZangBoot", "Launch rejected", error);
        }
    }
    private static boolean configured(Context c) {
        return c.getPackageName().equals(Settings.System.getString(c.getContentResolver(), "start_3rd_app"));
    }
    private static void status(Context c, String value) {
        prefs(c).edit().putString("status", value).apply();
        Log.i("GuiZangBoot", value);
    }
    static void confirm(Activity activity) {
        Intent intent = activity.getIntent();
        int boot = intent.getIntExtra(EXTRA, -1);
        boolean wake = intent.getBooleanExtra(EXTRA_WAKE, false);
        if (boot < 0) return;
        intent.removeExtra(EXTRA);
        intent.removeExtra(EXTRA_WAKE);
        if (boot == bootCount(activity) && prefs(activity).getInt("attempted", -1) == boot && enabled(activity)) {
            status(activity, wake ? "已在本次待机唤醒后进入桌面" : "已在本次完整启动后进入桌面");
        }
    }
    static void settings(Activity a, Runnable systemSettings) {
        new AlertDialog.Builder(a).setTitle("设置")
                .setItems(new String[]{"电视系统设置", "开机自动进入：" + (enabled(a) ? "已开启" : "已关闭")},
                        (d, i) -> { if (i == 0) systemSettings.run(); else show(a, () -> { }); })
                .setNegativeButton("返回", null).show();
    }
    static void show(Activity a, Runnable changed) {
        if (!XiaomiControls.enabled()) {
            new AlertDialog.Builder(a).setTitle("开机自动进入（实验）")
                    .setMessage("目前仅适配 MiTV-MTSU0 和 MiTV-MFTP0。其他机型请先使用普通应用入口。")
                    .setNegativeButton("返回", null).show();
            return;
        }
        String[] choices = {"关闭", "仅完整重启", "完整重启＋待机唤醒"};
        if (!XiaomiControls.standbyWakeSupported()) choices[2] = "完整重启＋待机唤醒（当前机型未验证）";
        new AlertDialog.Builder(a).setTitle("开机与唤醒（实验）")
                .setSingleChoiceItems(choices, mode(a), (dialog, which) -> {
                    if (which == BootStartPolicy.MODE_BOOT_AND_WAKE && !XiaomiControls.standbyWakeSupported()) {
                        Toast.makeText(a, "当前机型未验证待机唤醒", Toast.LENGTH_LONG).show();
                        return;
                    }
                    prefs(a).edit().putInt("mode", which)
                            .putBoolean("enabled", which != BootStartPolicy.MODE_OFF).apply();
                    Toast.makeText(a, which == BootStartPolicy.MODE_OFF ? "已关闭自动进入"
                            : configured(a) ? "已保存；下次系统调用入口时生效"
                            : "已保存；还需配置系统启动入口", Toast.LENGTH_LONG).show();
                    dialog.dismiss();changed.run();
                })
                .setNegativeButton("返回", null).show();
    }
    static void dump(Context c, java.io.PrintWriter out) {
        out.println("BootStart: enabled=" + enabled(c) + " mode=" + mode(c) + " configured=" + configured(c)
                + " boot=" + bootCount(c) + " attempted=" + prefs(c).getInt("attempted", -1)
                + " lastAttemptElapsed=" + prefs(c).getLong("last-attempt-elapsed", -1)
                + " status=" + prefs(c).getString("status", "none"));
    }
}
