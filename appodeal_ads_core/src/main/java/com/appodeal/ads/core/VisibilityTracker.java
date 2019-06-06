package com.appodeal.ads.core;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class VisibilityTracker {

    public static final int NO_TRACK = -1;

    private static final String TAG = VisibilityTracker.class.getSimpleName();

    private static final ArrayList<TrackingHolder> holders = new ArrayList<>();

    private static class TrackingHolder {

        private final WeakReference<View> viewReference;
        private final long requiredOnScreenTime;
        private final float visibilityPercent;
        private final VisibilityChangeCallback callback;

        private ViewTreeObserver.OnPreDrawListener preDrawListener;
        private View.OnAttachStateChangeListener attachStateChangeListener;

        private long lastShownTimeMs;
        private boolean isShownTracked;
        private boolean isFinishedTracked;
        private boolean isFinishedRequested;

        TrackingHolder(@NonNull View view,
                       long requiredOnScreenTimeMs,
                       float visibilityPercent,
                       @NonNull VisibilityChangeCallback callback) {
            this.viewReference = new WeakReference<>(view);
            this.requiredOnScreenTime = requiredOnScreenTimeMs;
            this.visibilityPercent = visibilityPercent;
            this.callback = callback;
        }

        public void start() {
            final View view = viewReference.get();
            if (view == null) {
                release();
                return;
            }
            if (preDrawListener == null) {
                preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        validate();
                        return true;
                    }
                };
            }
            if (attachStateChangeListener == null) {
                attachStateChangeListener = new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        release();
                    }
                };
            }
            view.addOnAttachStateChangeListener(attachStateChangeListener);
            view.getViewTreeObserver().addOnPreDrawListener(preDrawListener);
            validate();
        }

        void validate() {
            final View view = viewReference.get();
            if (view == null) {
                release();
                return;
            }
            if (Utils.isOnTop(view, visibilityPercent)) {
                if (!isShownTracked) {
                    callback.onViewShown();
                    isShownTracked = true;
                }
                if (!isFinishedRequested && !isFinishedTracked) {
                    Utils.onUiThread(finishRunnable, requiredOnScreenTime);
                    lastShownTimeMs = System.currentTimeMillis();
                    isFinishedRequested = true;
                }
            } else if (!isFinishedTracked) {
                Utils.cancelUiThreadTask(finishRunnable);
                isFinishedRequested = false;
                lastShownTimeMs = 0;
            }
        }

        private void release() {
            final View view = viewReference.get();
            if (view != null) {
                if (isShownTracked
                        && !isFinishedTracked
                        && requiredOnScreenTime > NO_TRACK
                        && lastShownTimeMs > 0
                        && (System.currentTimeMillis() - lastShownTimeMs) >= requiredOnScreenTime) {
                    isFinishedTracked = true;
                    callback.onViewTrackingFinished();
                }
                view.removeOnAttachStateChangeListener(attachStateChangeListener);
                view.getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
            }
            Utils.cancelUiThreadTask(finishRunnable);
            synchronized (holders) {
                holders.remove(this);
            }
        }

        private final Runnable finishRunnable = new Runnable() {
            @Override
            public void run() {
                release();
            }
        };

    }

    public static void startTracking(@NonNull View view,
                                     long requiredOnScreenTimeMs,
                                     float visibilityPercent,
                                     @NonNull VisibilityChangeCallback callback) {
        synchronized (holders) {
            stopTracking(view);
            TrackingHolder holder = new TrackingHolder(view, requiredOnScreenTimeMs, visibilityPercent, callback);
            holders.add(holder);
            holder.start();
        }
    }

    public static void stopTracking(@NonNull View view) {
        synchronized (holders) {
            for (TrackingHolder holder : holders) {
                if (holder.viewReference.get() == view) {
                    holder.release();
                    holders.remove(holder);
                    break;
                }
            }
        }
    }

    public interface VisibilityChangeCallback {
        void onViewShown();

        void onViewTrackingFinished();
    }

}
