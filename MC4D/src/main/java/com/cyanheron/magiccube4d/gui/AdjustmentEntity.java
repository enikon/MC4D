package com.cyanheron.magiccube4d.gui;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class AdjustmentEntity extends LinearLayout {

    public String entityName = "Text";
    private TextView txt;
    private SeekBar sb;

    public AdjustmentEntity(Context context) {
        super(context);
        this.init(context);
    }
    public AdjustmentEntity(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }
    public AdjustmentEntity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    public void init(Context context) {

    }
}
