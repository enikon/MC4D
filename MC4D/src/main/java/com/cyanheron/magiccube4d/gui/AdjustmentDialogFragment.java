package com.cyanheron.magiccube4d.gui;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.superliminal.magiccube4d.R;
import com.superliminal.util.android.Color;

import java.util.LinkedHashMap;

public class AdjustmentDialogFragment extends DialogFragment {

    public LinkedHashMap<String, Color> colors;
    public MC4DPreferencesManager manager;

    public AdjustmentDialogFragment(){

    }
    public static AdjustmentDialogFragment newInstance(MC4DPreferencesManager manager) {
        AdjustmentDialogFragment f = new AdjustmentDialogFragment();
//        Bundle args = new Bundle();
//        ArrayList<LinkedHashMap> list = new ArrayList<>();
//        args.putSerializable("colors", list);
//        f.setArguments(args);
        f.manager = manager;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_sample, container);
        //LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout_adjustment);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


}
