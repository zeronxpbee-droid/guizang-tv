package io.github.op7418.guizangtv;

final class BootStartPolicy {
    static final int MODE_OFF = 0;
    static final int MODE_BOOT_ONLY = 1;
    static final int MODE_BOOT_AND_WAKE = 2;
    static final long BOOT_WINDOW_MS = 10 * 60_000L;
    static final long WAKE_DEBOUNCE_MS = 30_000L;

    private BootStartPolicy() { }

    static int mode(int value) {
        return value >= MODE_OFF && value <= MODE_BOOT_AND_WAKE ? value : MODE_OFF;
    }

    static String skipReason(boolean supported, boolean wakeSupported, int mode, int boot,
            int attempted, long elapsed, long lastAttemptElapsed) {
        if (!supported) return "当前机型未适配，已跳过";
        if (mode(mode) == MODE_OFF) return "开机与唤醒自动进入已关闭";
        if (boot < 0) return "无法确认本次启动，已跳过";
        if (elapsed < 0) return "无法确认运行时间，已跳过";
        if (boot != attempted)
            return elapsed <= BOOT_WINDOW_MS ? null : "启动通知过晚，已跳过以免打断使用";
        if (mode != MODE_BOOT_AND_WAKE) return "本次开机已尝试，未重复打开";
        if (!wakeSupported) return "当前机型未验证待机唤醒，已跳过";
        if (lastAttemptElapsed >= 0) {
            if (elapsed < lastAttemptElapsed) return "运行时间回退，已跳过";
            if (elapsed - lastAttemptElapsed < WAKE_DEBOUNCE_MS)
                return "启动入口调用过于频繁，已跳过";
        }
        return null;
    }
}
