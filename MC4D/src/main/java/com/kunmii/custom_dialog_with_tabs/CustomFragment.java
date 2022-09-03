package com.kunmii.custom_dialog_with_tabs;

//based on http://kunmii.blogspot.com/2017/01/how-to-create-custom-dialog-with-tabs.html

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cyanheron.magiccube4d.gui.OnAdjustmentChange;
import com.superliminal.magiccube4d.R;

public class CustomFragment extends Fragment {
    public String mText = "";
    public float minValue = 0f;
    public float maxValue = 1f;
    public float currentValue = 0.5f;
    public OnAdjustmentChange onAdjustmentChange;
    private final float accuracy = 0.01f;

    public static CustomFragment createInstance(
            String name,
            float minValue,
            float maxValue,
            float currentValue,
            OnAdjustmentChange onAdjustmentChange
    )
    {
        CustomFragment fragment = new CustomFragment();
        fragment.mText = name;
        fragment.minValue = minValue;
        fragment.maxValue = maxValue;
        fragment.onAdjustmentChange = onAdjustmentChange;
        fragment.currentValue = currentValue;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sample,container,false);
        ((TextView) v.findViewById(R.id.fragment_tab_label)).setText(mText);
        SeekBar sb = (SeekBar) v.findViewById(R.id.fragment_tab_slider);
        sb.setMax((int)((this.maxValue - this.minValue)/this.accuracy));
        sb.setProgress((int)((this.currentValue-this.minValue)/this.accuracy));
        final OnAdjustmentChange onAdjustmentChange = this.onAdjustmentChange;
        final float minValue = this.minValue;
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    onAdjustmentChange.onAdjustmentChanged(progress * accuracy + minValue);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        return v;
    }
}