package com.cyanheron.magiccube4d.gui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.superliminal.util.android.Color;

public class ColorPickEntity extends LinearLayout {

    public Color entityColor = Color.black;
    public String entityName = "Text";
    private TextView txt;
    private Button btn;
    private OnColorChange listener;

    public ColorPickEntity(Context context) {
        super(context);
        this.init(context);
    }
    public ColorPickEntity(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }
    public ColorPickEntity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    public void init(Context context){
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        int margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.setMargins(margin, margin, margin, margin);
        layout.setLayoutParams(params);

        txt = new TextView(context);
        txt.setText(this.entityName);
        txt.setTextSize(10);
        btn = new Button(context);
        btn.setText("");
        btn.setBackgroundColor(this.entityColor.intValue());
        layout.addView(txt);
        layout.addView(btn);

        this.addView(layout);
    }
    public ColorPickEntity setName(String name){
        this.entityName = name;
        txt.setText(this.entityName);
        return this;
    }
    public ColorPickEntity setColor(Color color){
        this.entityColor = color;
        btn.setBackgroundColor(this.entityColor.intValue());
        return this;
    }

    public ColorPickEntity setOnColorChangeListener(OnColorChange listener) {
        btn.setOnClickListener(v->{
            this.btn.setEnabled(false);

            ColorPickerDialog colorPickerDialog = ColorPickerDialog.createColorPickerDialog(
                    this.getContext(),
                    ColorPickerDialog.DARK_THEME
                );
            colorPickerDialog.setInitialColor(this.entityColor.intValue());
            colorPickerDialog.setOnColorPickedListener((color, hexVal) -> {
                listener.onColorChanged(new Color(color));
                this.btn.setEnabled(true);
            });
            colorPickerDialog.setOnDismissListener(dialog -> {
                this.btn.setEnabled(true);
            });
            colorPickerDialog.show();
        });
        return this;
    }

    public ColorPickEntity setOnColorResetListener(OnColorChange listener) {
        btn.setOnLongClickListener(v->{
            listener.onColorChanged(null);
            return true;
        });
        return this;
    }
}
