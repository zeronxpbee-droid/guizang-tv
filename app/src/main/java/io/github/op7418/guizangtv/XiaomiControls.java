package io.github.op7418.guizangtv;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

/** Local profile verified on MiTV-MTSU0, OS3.0.119.0.UTSMATV. */
final class XiaomiControls {
    private static final String MEDIA_EXPLORER_ACTION =
            "duokan.mediaexplorer.intent.action.BROWSE_ALBUM";

    private XiaomiControls() { }

    static boolean enabled() {
        return DeviceProfile.supportsVerifiedXiaomiControls(Build.MODEL);
    }

    /** Observed on this firmware: source 23 = HDMI 1, source 24 = HDMI 2. */
    static boolean openHdmi(Context context, int port) {
        if (!enabled() || port < 1 || port > 2) return false;
        ComponentName target = new ComponentName("com.xiaomi.mitv.tvplayer",
                "com.xiaomi.mitv.tvplayer.ExternalSourceActivity");
        try {
            ActivityInfo info = context.getPackageManager().getActivityInfo(target, 0);
            if (!info.enabled || !info.exported) return false;
            if (info.permission != null
                    && context.checkSelfPermission(info.permission) != PackageManager.PERMISSION_GRANTED)
                return false;
            context.startActivity(new Intent("com.xiaomi.mitv.tvplayer.EXTSRC_PLAY")
                    .setComponent(target).putExtra("input", 22 + port)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        } catch (PackageManager.NameNotFoundException | RuntimeException unavailable) {
            android.util.Log.w("TvHome.Xiaomi", "HDMI entry unavailable", unavailable);
            return false;
        }
    }

    static Intent settings(Context context, String standardAction) {
        if (!enabled()) return new Intent(standardAction);
        String action, activity;
        if (Settings.ACTION_SETTINGS.equals(standardAction)) {
            action = "com.xiaomi.mitv.settings.SETTINGS";
            activity = ".entry.MainActivity";
        } else if (Settings.ACTION_WIFI_SETTINGS.equals(standardAction)) {
            action = "com.xiaomi.mitv.settings.WIFI_SETTINGS";
            activity = ".wifi.MitvWifiActivity";
        } else return null;
        Intent intent = new Intent(action).setComponent(ComponentName.unflattenFromString(
                "com.xiaomi.mitv.settings/" + activity));
        return intent.resolveActivity(context.getPackageManager()) != null ? intent : null;
    }

    static boolean showInputs(Context context) {
        if (!enabled()) return false;
        ComponentName receiver = new ComponentName("com.xiaomi.mitv.settings",
                "com.xiaomi.mitv.settings.SettingsBroadcastReceiver");
        try {
            ActivityInfo info = context.getPackageManager().getReceiverInfo(receiver, 0);
            if (!info.exported || !info.enabled) return false;
            if (info.permission != null
                    && context.checkSelfPermission(info.permission) != PackageManager.PERMISSION_GRANTED)
                return false;
            context.sendBroadcast(new Intent("com.xiaomi.mitv.settings.INPUTSOURCE_POPUP")
                    .setComponent(receiver));
            return true;
        } catch (PackageManager.NameNotFoundException | SecurityException unavailable) {
            return false;
        }
    }

    static String label(String pkg, String original) {
        return DeviceProfile.label(Build.MODEL, pkg, original);
    }

    static boolean isDirectEntry(String id) {
        return DeviceProfile.STOCK_HOME.equals(id)
                || (DeviceProfile.usesMediaExplorerGallery(Build.MODEL)
                && DeviceProfile.MEDIA_EXPLORER.equals(id));
    }

    static boolean openGallery(Context context, String id) {
        if (!DeviceProfile.usesMediaExplorerGallery(Build.MODEL)
                || !DeviceProfile.MEDIA_EXPLORER.equals(id)) return false;
        Intent candidate = new Intent(MEDIA_EXPLORER_ACTION)
                .addCategory(Intent.CATEGORY_DEFAULT)
                .setPackage(DeviceProfile.MEDIA_EXPLORER);
        ComponentName target = candidate.resolveActivity(context.getPackageManager());
        if (target == null) return false;
        return startChecked(context, candidate.setComponent(target), target, "Album entry unavailable");
    }

    static boolean startStockHome(Context context) {
        String className = DeviceProfile.stockHomeActivity(Build.MODEL);
        if (className == null) return false;
        ComponentName target = new ComponentName(DeviceProfile.STOCK_HOME, className);
        Intent intent = new Intent(Intent.ACTION_MAIN).setComponent(target)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return startChecked(context, intent, target, "Public Xiaomi Home entry unavailable");
    }

    private static boolean startChecked(Context context, Intent intent, ComponentName target,
            String logMessage) {
        try {
            ActivityInfo info = context.getPackageManager().getActivityInfo(target, 0);
            if (!info.enabled || !info.exported) return false;
            if (info.permission != null
                    && context.checkSelfPermission(info.permission) != PackageManager.PERMISSION_GRANTED)
                return false;
            context.startActivity(intent);
            return true;
        } catch (PackageManager.NameNotFoundException | RuntimeException unavailable) {
            android.util.Log.w("TvHome.Xiaomi", logMessage, unavailable);
            return false;
        }
    }
}
