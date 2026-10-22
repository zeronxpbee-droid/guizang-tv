package io.github.op7418.guizangtv;

/** Model-specific behavior that can be unit tested without an Android runtime. */
final class DeviceProfile {
    static final String MODEL_MTSU0 = "MiTV-MTSU0";
    static final String MODEL_MFTP0 = "MiTV-MFTP0";
    static final String BILIBILI = "com.xiaodianshi.tv.yst";
    static final String SMART_SHARE = "com.xiaomi.mitv.smartshare";
    static final String LEGACY_GALLERY = "com.mitv.gallery";
    static final String MEDIA_EXPLORER = "com.xiaomi.mitv.mediaexplorer";
    static final String STOCK_HOME = "com.mitv.tvhome";

    private static final String[] LEGACY_DEFAULTS = {
            "hdmi:2", "hdmi:1", BILIBILI, SMART_SHARE, LEGACY_GALLERY, STOCK_HOME
    };
    private static final String[] MFTP0_DEFAULTS = {
            BILIBILI, SMART_SHARE, MEDIA_EXPLORER, STOCK_HOME
    };

    private DeviceProfile() { }

    /** HDMI, input-popup and boot behavior is verified only on this model. */
    static boolean supportsVerifiedXiaomiControls(String model) {
        return MODEL_MTSU0.equals(model);
    }

    static boolean usesMediaExplorerGallery(String model) {
        return MODEL_MFTP0.equals(model);
    }

    static String[] defaultItems(String model) {
        return (MODEL_MFTP0.equals(model) ? MFTP0_DEFAULTS : LEGACY_DEFAULTS).clone();
    }

    static String label(String model, String id, String original) {
        switch (id) {
            case BILIBILI: return "哔哩哔哩";
            case SMART_SHARE: return "小米投屏";
            case LEGACY_GALLERY:
            case MEDIA_EXPLORER: return "相册";
            case STOCK_HOME: return "小米原桌面";
            default: return original;
        }
    }

    static String stockHomeActivity(String model) {
        if (MODEL_MFTP0.equals(model)) return "com.mitv.tvhome.MainActivityUserMode";
        if (MODEL_MTSU0.equals(model)) return "com.mitv.tvhome.MainActivity";
        return null;
    }
}
