package io.github.op7418.guizangtv;

import org.junit.Test;
import static org.junit.Assert.*;

public class DeviceProfileTest {
    @Test public void mftp0UsesVerifiedSafeDefaults() {
        assertArrayEquals(new String[]{
                "hdmi:1", "hdmi:2", "hdmi:3",
                DeviceProfile.BILIBILI,
                DeviceProfile.SMART_SHARE,
                DeviceProfile.MEDIA_EXPLORER,
                DeviceProfile.STOCK_HOME
        }, DeviceProfile.defaultItems(DeviceProfile.MODEL_MFTP0));
        assertTrue(DeviceProfile.usesMediaExplorerGallery(DeviceProfile.MODEL_MFTP0));
        assertTrue(DeviceProfile.supportsVerifiedXiaomiControls(DeviceProfile.MODEL_MFTP0));
        assertEquals(3, DeviceProfile.maxHdmiPort(DeviceProfile.MODEL_MFTP0));
        assertEquals(23, DeviceProfile.hdmiInput(DeviceProfile.MODEL_MFTP0, 1));
        assertEquals(25, DeviceProfile.hdmiInput(DeviceProfile.MODEL_MFTP0, 3));
        assertEquals(-1, DeviceProfile.hdmiInput(DeviceProfile.MODEL_MFTP0, 4));
        assertTrue(DeviceProfile.usesPackagedInputPopup(DeviceProfile.MODEL_MFTP0));
        assertEquals("Fire TV", DeviceProfile.label(DeviceProfile.MODEL_MFTP0, "hdmi:1", "HDMI 1"));
    }

    @Test public void mtsu0KeepsExistingCapabilities() {
        assertArrayEquals(new String[]{
                "hdmi:2", "hdmi:1", DeviceProfile.BILIBILI, DeviceProfile.SMART_SHARE,
                DeviceProfile.LEGACY_GALLERY, DeviceProfile.STOCK_HOME
        }, DeviceProfile.defaultItems(DeviceProfile.MODEL_MTSU0));
        assertTrue(DeviceProfile.supportsVerifiedXiaomiControls(DeviceProfile.MODEL_MTSU0));
        assertEquals(2, DeviceProfile.maxHdmiPort(DeviceProfile.MODEL_MTSU0));
        assertFalse(DeviceProfile.usesMediaExplorerGallery(DeviceProfile.MODEL_MTSU0));
        assertFalse(DeviceProfile.usesPackagedInputPopup(DeviceProfile.MODEL_MTSU0));
    }

    @Test public void unknownModelsDoNotGainXiaomiCapabilities() {
        assertFalse(DeviceProfile.supportsVerifiedXiaomiControls("unknown"));
        assertEquals(0, DeviceProfile.maxHdmiPort("unknown"));
        assertEquals(-1, DeviceProfile.hdmiInput("unknown", 1));
        assertNull(DeviceProfile.stockHomeActivity("unknown"));
    }

    @Test public void returnedDefaultsCannotMutateProfile() {
        String[] changed = DeviceProfile.defaultItems(DeviceProfile.MODEL_MFTP0);
        changed[0] = "changed";
        assertEquals("hdmi:1",
                DeviceProfile.defaultItems(DeviceProfile.MODEL_MFTP0)[0]);
    }
}
