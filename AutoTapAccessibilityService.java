package com.example.idleslayerautotap;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.accessibility.AccessibilityEvent;

public class AutoTapAccessibilityService extends AccessibilityService {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean busy = false;
    private long lastTap = 0;

    private final Runnable loop = new Runnable() {
        @Override public void run() {
            if (getSharedPreferences("settings", MODE_PRIVATE).getBoolean("enabled", false)) {
                capture();
            }
            handler.postDelayed(this, 180);
        }
    };

    @Override public void onServiceConnected() {
        super.onServiceConnected();
        handler.post(loop);
    }

    private void capture() {
        if (busy) return;
        busy = true;

        takeScreenshot(Display.DEFAULT_DISPLAY, getMainExecutor(), new TakeScreenshotCallback() {
            @Override public void onSuccess(ScreenshotResult result) {
                Bitmap bitmap = null;
                try {
                    bitmap = Bitmap.wrapHardwareBuffer(result.getHardwareBuffer(), result.getColorSpace());
                    if (bitmap == null) return;
                    Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, false);

                    var p = getSharedPreferences("settings", MODE_PRIVATE);
                    TargetDetector.Hit hit = TargetDetector.find(
                            copy,
                            p.getInt("x1",5),
                            p.getInt("x2",35),
                            p.getInt("y1",15),
                            p.getInt("y2",70)
                    );

                    long now = System.currentTimeMillis();
                    if (hit != null && now-lastTap >= p.getInt("cooldown",450)) {
                        lastTap = now;
                        tap(hit.x, hit.y);
                    }
                    copy.recycle();
                } catch (Exception ignored) {
                } finally {
                    busy = false;
                    try { result.getHardwareBuffer().close(); } catch (Exception ignored) {}
                }
            }

            @Override public void onFailure(int errorCode) {
                busy = false;
            }
        });
    }

    private void tap(float x, float y) {
        long now = System.currentTimeMillis();
        GestureDescription.StrokeDescription stroke =
                new GestureDescription.StrokeDescription(
                        new android.graphics.Path() {{
                            moveTo(x,y);
                        }}, 0, 35);
        dispatchGesture(new GestureDescription.Builder().addStroke(stroke).build(), null, null);
    }

    @Override public void onAccessibilityEvent(AccessibilityEvent event) {}
    @Override public void onInterrupt() {}
}