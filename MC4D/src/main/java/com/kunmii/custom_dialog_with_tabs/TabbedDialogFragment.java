package com.kunmii.custom_dialog_with_tabs;

//based on http://kunmii.blogspot.com/2017/01/how-to-create-custom-dialog-with-tabs.html

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.cyanheron.magiccube4d.gui.ColorPickEntity;
import com.cyanheron.magiccube4d.gui.MC4DConfig;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.cyanheron.magiccube4d.gui.MC4DPreferencesManager;
import com.superliminal.magiccube4d.R;
import com.superliminal.util.PropertyManager;

import java.util.Map;

public class TabbedDialogFragment extends DialogFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public MC4DPreferencesManager manager;

    public TabbedDialogFragment(){

    }
    public static TabbedDialogFragment newInstance(MC4DPreferencesManager manager) {
        TabbedDialogFragment f = new TabbedDialogFragment();
        f.manager = manager;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        getDialog().getWindow().setDimAmount(0.0f);

        View rootview = inflater.inflate(R.layout.dialog_sample,container,false);
        tabLayout = (TabLayout) rootview.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) rootview.findViewById(R.id.masterViewPager);
        CustomAdapter adapter = new CustomAdapter(getChildFragmentManager(), MC4DConfig.adjustmentToAdjustmentTabs.size());

        for(Map.Entry<MC4DConfig.Adjustments, MC4DConfig.AdjustmentStruct> entry : MC4DConfig.adjustmentToAdjustmentTabs.entrySet()){
            Fragment cf = makeCustomFragment(entry);
            adapter.addFragment(entry.getValue().abbrev, cf, entry.getKey().ordinal());
        }
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return rootview;
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
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private Fragment makeCustomFragment(Map.Entry<MC4DConfig.Adjustments, MC4DConfig.AdjustmentStruct> entry) {
        MC4DConfig.AdjustmentStruct value = entry.getValue();
        CustomFragment cf = CustomFragment.createInstance(
                value.name,
                value.minValue,
                value.maxValue,
                manager.adjustmentValues.get(entry.getKey()),
                v -> {
                    manager.setAdjustmentValue(entry.getKey(), v);
                }
        );
        return cf;
    }

}