package io.github.op7418.guizangtv;

import org.junit.Test;
import static org.junit.Assert.*;

public class DeviceProfileTest {
    @Test public void mftp0UsesVerifiedSafeDefaults() {
        assertArrayEquals(new String[]{
                DeviceProfile.BILIBILI,
                DeviceProfile.SMART_SHARE,
                DeviceProfile.MEDIA_EXPLORER,
                DeviceProfile.STOCK_HOME
        }, DeviceProfile.defaultItems(DeviceProfile.MODEL_MFTP0));
        assertTrue(DeviceProfile.usesMediaExplorerGallery(DeviceProfile.MODEL_MFTP0));
        assertFalse(DeviceProfile.supportsVerifiedXiaomiControls(DeviceProfile.MODEL_MFTP0));
    }

    @Test public void mtsu0KeepsExistingCapabilities() {
        assertArrayEquals(new String[]{
                "hdmi:2", "hdmi:1", DeviceProfile.BILIBILI, DeviceProfile.SMART_SHARE,
                DeviceProfile.LEGACY_GALLERY, DeviceProfile.STOCK_HOME
        }, DeviceProfile.defaultItems(DeviceProfile.MODEL_MTSU0));
        assertTrue(DeviceProfile.supportsVerifiedXiaomiControls(DeviceProfile.MODEL_MTSU0));
        assertFalse(DeviceProfile.usesMediaExplorerGallery(DeviceProfile.MODEL_MTSU0));
    }

    @Test public void unknownModelsDoNotGainXiaomiCapabilities() {
        assertFalse(DeviceProfile.supportsVerifiedXiaomiControls("unknown"));
        assertNull(DeviceProfile.stockHomeActivity("unknown"));
    }

    @Test public void returnedDefaultsCannotMutateProfile() {
        String[] changed = DeviceProfile.defaultItems(DeviceProfile.MODEL_MFTP0);
        changed[0] = "changed";
        assertEquals(DeviceProfile.BILIBILI,
                DeviceProfile.defaultItems(DeviceProfile.MODEL_MFTP0)[0]);
    }
}
