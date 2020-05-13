package com.young.note.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;

import com.young.note.NoteApplication;

public class WakeLockUtil {
    public static void wakeUpAndUnlock() {
        PowerManager powerManager = (PowerManager) NoteApplication.getContext().getSystemService(Context.POWER_SERVICE);
        assert powerManager != null;
        boolean isScreenOn = powerManager.isInteractive();
        if (!isScreenOn) {
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, "note:bright");
            wakeLock.acquire(10000);
            wakeLock.release();
        }
        KeyguardManager keyguardManager = (KeyguardManager) NoteApplication.getContext().getSystemService(Context.KEYGUARD_SERVICE);
        assert keyguardManager != null;
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unlock");
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard();
    }
}
