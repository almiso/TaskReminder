package org.almiso.taskreminder.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;

import org.almiso.taskreminder.core.TaskReminderApplication;

import java.util.Hashtable;

/**
 * Created by Alexandr Sosorev on 04.04.2016.
 */
public class AndroidUtilities {

    protected static final String TAG = "AndroidUtilities";
    public static float density = 1;
    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();

    static {
        density = TaskReminderApplication.applicationContext.getResources().getDisplayMetrics().density;
    }

    public static void RunOnUIThread(Runnable runnable) {
        RunOnUIThread(runnable, 0);
    }

    public static void RunOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            TaskReminderApplication.applicationHandler.post(runnable);
        } else {
            TaskReminderApplication.applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static Resources getResources() {
        return TaskReminderApplication.application.getResources();
    }

    public static void showView(View view) {
        showView(view, true);
    }

    public static void showView(View view, boolean isAnimating) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() == View.VISIBLE) {
            return;
        }

        if (isAnimating) {
            AlphaAnimation alpha = new AlphaAnimation(0.0F, 1.0f);
            alpha.setDuration(250);
            alpha.setFillAfter(false);
            view.startAnimation(alpha);
        }
        view.setVisibility(View.VISIBLE);
    }

    public static void hideView(View view) {
        hideView(view, true);
    }

    public static void hideView(View view, boolean isAnimating) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() != View.VISIBLE) {
            return;
        }
        if (isAnimating) {
            AlphaAnimation alpha = new AlphaAnimation(1.0F, 0.0f);
            alpha.setDuration(250);
            alpha.setFillAfter(false);
            view.startAnimation(alpha);
        }
        view.setVisibility(View.INVISIBLE);
    }

    public static void goneView(View view) {
        goneView(view, true);
    }

    public static void goneView(View view, boolean isAnimating) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() != View.VISIBLE) {
            return;
        }
        if (isAnimating) {
            AlphaAnimation alpha = new AlphaAnimation(1.0F, 0.0f);
            alpha.setDuration(250);
            alpha.setFillAfter(false);
            view.startAnimation(alpha);
        }
        view.setVisibility(View.GONE);
    }

    public static void prepareView(View view, boolean isVisible) {
        if (view == null) {
            return;
        }
        if (isVisible) {
            showView(view);
        } else {
            goneView(view);
        }
    }

    public static int getPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, AndroidUtilities.getResources().getDisplayMetrics());
    }

    public static int getSp(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, AndroidUtilities.getResources().getDisplayMetrics());
    }

    public static int dp(float value) {
        return (int) Math.ceil(density * value);
    }

    public static Typeface getTypeface(String assetPath) {
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(TaskReminderApplication.applicationContext.getAssets(), assetPath);
                    typefaceCache.put(assetPath, t);
                } catch (Exception e) {
                    Logger.e(TAG, "Could not get typeface '" + assetPath + "' because " + e.getMessage(), e);
                    return null;
                }
            }
            return typefaceCache.get(assetPath);
        }
    }

    public static void showKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

        ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
    }
}
