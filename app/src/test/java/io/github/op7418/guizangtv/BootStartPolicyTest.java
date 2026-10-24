package io.github.op7418.guizangtv;

import org.junit.Test;
import static org.junit.Assert.*;

public class BootStartPolicyTest {
    @Test public void newBootCanEnter() {
        assertNull(BootStartPolicy.skipReason(true, true, BootStartPolicy.MODE_BOOT_ONLY,
                20, 19, 75000, 50000));
    }
    @Test public void disabledCannotEnterEvenDuringBoot() {
        assertNotNull(BootStartPolicy.skipReason(true, true, BootStartPolicy.MODE_OFF,
                20, 19, 75000, 50000));
    }
    @Test public void unsupportedModelCannotEnter() {
        assertNotNull(BootStartPolicy.skipReason(false, true, BootStartPolicy.MODE_BOOT_ONLY,
                20, 19, 75000, 50000));
    }
    @Test public void bootOnlyCannotTakeFocusAgainDuringSameBoot() {
        assertNotNull(BootStartPolicy.skipReason(true, true, BootStartPolicy.MODE_BOOT_ONLY,
                20, 20, 76000, 50000));
    }
    @Test public void optedInWakeCanEnterAfterDebounce() {
        assertNull(BootStartPolicy.skipReason(true, true, BootStartPolicy.MODE_BOOT_AND_WAKE,
                20, 20, 81000, 50000));
    }
    @Test public void wakeCannotEnterOnUnverifiedModel() {
        assertNotNull(BootStartPolicy.skipReason(true, false, BootStartPolicy.MODE_BOOT_AND_WAKE,
                20, 20, 81000, 50000));
    }
    @Test public void repeatedWakeInvocationIsDebounced() {
        assertNotNull(BootStartPolicy.skipReason(true, true, BootStartPolicy.MODE_BOOT_AND_WAKE,
                20, 20, 79999, 50000));
        assertNull(BootStartPolicy.skipReason(true, true, BootStartPolicy.MODE_BOOT_AND_WAKE,
                20, 20, 80000, 50000));
    }
    @Test public void unknownBootIsSkipped() {
        assertNotNull(BootStartPolicy.skipReason(true, true, BootStartPolicy.MODE_BOOT_ONLY,
                -1, -1, 75000, 50000));
    }
    @Test public void lateDeliveryCannotInterruptUse() {
        assertNull(BootStartPolicy.skipReason(true, true, BootStartPolicy.MODE_BOOT_ONLY,
                20, 19, 600000, 50000));
        assertNotNull(BootStartPolicy.skipReason(true, true, BootStartPolicy.MODE_BOOT_ONLY,
                20, 19, 600001, 50000));
    }
    @Test public void invalidClockIsSkipped() {
        assertNotNull(BootStartPolicy.skipReason(true, true, BootStartPolicy.MODE_BOOT_ONLY,
                20, 19, -1, 50000));
        assertNotNull(BootStartPolicy.skipReason(true, true, BootStartPolicy.MODE_BOOT_AND_WAKE,
                20, 20, 49000, 50000));
    }
    @Test public void invalidStoredModeFailsClosed() {
        assertEquals(BootStartPolicy.MODE_OFF, BootStartPolicy.mode(-1));
        assertEquals(BootStartPolicy.MODE_OFF, BootStartPolicy.mode(3));
    }
}
