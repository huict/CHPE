package com.mygdx.game.UI;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.DebugLog;

import java.util.ArrayList;

/**
 * Helper class that provides static functions for repetitive operations.
 */
class AAL {
    /**
     * Makes the title bar transparent.
     * @param window Window of the current activity.
     */
    static void setTitleBar(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * Checks if a permission is granted already.
     * @param context Context of the activity.
     * @param permissions permissions to check for.
     * @return Whether the permission was granted or not.
     */
    static boolean permissionsGranted(Context context, String[] permissions) {
        for(String permission : permissions) {
            if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper functions that requests permissions from the user.
     * @param context Context of the activity.
     * @param activity Activity.
     * @param permissions Permissions to request.
     */
    static void requestPermissions(Context context, Activity activity, String[] permissions) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        // collect missing permissions just to be safe
        for(String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        // convert the ArrayList to a c-style array for argument passing
        String[] arr = new String[deniedPermissions.size()];
        arr = deniedPermissions.toArray(arr);
        ActivityCompat.requestPermissions(activity, arr, 1);
    }
}
