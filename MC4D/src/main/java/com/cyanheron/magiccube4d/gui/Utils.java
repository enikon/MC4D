package com.cyanheron.magiccube4d.gui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;

public class Utils {
    public static String DIALOG_TAG = "dialog";
    public static FragmentTransaction prepareDialog(Activity activity){
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
//        Fragment prev = activity.getFragmentManager().findFragmentByTag(DIALOG_TAG);
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);
        return ft;
    }
}
