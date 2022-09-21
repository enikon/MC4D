package com.cyanheron.magiccube4d.gui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.superliminal.magiccube4d.R;
import com.superliminal.util.android.Color;

import java.util.LinkedHashMap;
import java.util.Map;

public class ControlsDialogFragment extends DialogFragment {

    public MC4DPreferencesManager manager;

    public ControlsDialogFragment(){

    }
    public static ControlsDialogFragment newInstance(MC4DPreferencesManager manager) {
        ControlsDialogFragment f = new ControlsDialogFragment();
        f.manager = manager;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.controls, container);
        SwitchCompat sw = (SwitchCompat) v.findViewById(R.id.switch_controls_amt);
        sw.setChecked(Boolean.parseBoolean(this.manager.getControls(MC4DConfig.Controls.AMT)));
        sw.setOnCheckedChangeListener(
                (buttonView, isChecked)->{
                    this.manager.setControls(MC4DConfig.Controls.AMT, isChecked+"");
                }
        );
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
