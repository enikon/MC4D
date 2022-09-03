package com.cyanheron.magiccube4d.gui;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class Utils {
    public static String DIALOG_TAG = "dialog";

    public static FragmentTransaction prepareDialog(AppCompatActivity activity) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//        Fragment prev = activity.getFragmentManager().findFragmentByTag(DIALOG_TAG);
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);
        return ft;
    }

}
