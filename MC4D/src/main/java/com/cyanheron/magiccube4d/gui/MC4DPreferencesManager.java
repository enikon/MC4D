package com.cyanheron.magiccube4d.gui;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.superliminal.util.PropertyManager;
import com.superliminal.util.android.Color;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class MC4DPreferencesManager {

    private final Gson gson;
    private final SharedPreferences prefs;
    private Context context;

    public Color[] defaultColors;
    public Color[] colors;

    public LinkedHashMap<MC4DConfig.Names, Color> nameColor;
    public LinkedHashMap<MC4DConfig.Names, Color> defaultNameColor;

    public LinkedHashMap<MC4DConfig.Controls, String> defaultControlsSettings;
    public LinkedHashMap<MC4DConfig.Controls, String> controlsSettings;

    public LinkedHashMap<MC4DConfig.Adjustments, Float> adjustmentValues;
    public LinkedHashMap<MC4DConfig.Adjustments, Float> defaultAdjustmentValues;

    private OnColorsChange onColorsChange;
    private OnAdjustmentsChange onAdjustmentsChange;
    private OnControlChange onControlChange;

    public MC4DPreferencesManager(
            Context context,
            Color[] defaultColors,
            Color[] defaultNameColors,
            Float[] defaultAdjustmentValues,
            String[] defaultControlsSettings,
            OnColorsChange onColorsChange,
            OnAdjustmentsChange onAdjustmentsChange,
            OnControlChange onControlChange

    ){
        this.context = context;
        this.defaultColors = defaultColors;
        this.colors = Arrays.<Color>copyOf(this.defaultColors, this.defaultColors.length);
        this.onColorsChange = onColorsChange;
        this.onAdjustmentsChange = onAdjustmentsChange;
        this.onControlChange = onControlChange;
        this.gson = new Gson();
        this.prefs = this.context.getSharedPreferences(MC4DConfig.APPNAME, 0);

        this.nameColor = new LinkedHashMap<>();
        this.defaultNameColor = new LinkedHashMap<>();
        for(MC4DConfig.Names n : MC4DConfig.Names.values()){
            this.defaultNameColor.put(n, defaultNameColors[n.ordinal()]);
        }
        this.reloadColors();

        this.controlsSettings = new LinkedHashMap<>();
        this.defaultControlsSettings = new LinkedHashMap<>();
        for(MC4DConfig.Controls n : MC4DConfig.Controls.values()){
            this.defaultControlsSettings.put(n, defaultControlsSettings[n.ordinal()]);
        }
        this.reloadControls();

        this.adjustmentValues = new LinkedHashMap<>();
        this.defaultAdjustmentValues = new LinkedHashMap<>();
        for(MC4DConfig.Adjustments n : MC4DConfig.Adjustments.values()){
            this.defaultAdjustmentValues.put(n, defaultAdjustmentValues[n.ordinal()]);
        }
       this.reloadAdjustment();

        PropertyManager.top.setProperty("twistfactor", "1f");
        PropertyManager.top.setProperty("outlines", "true");
        PropertyManager.top.setProperty("outlines.color", "#000000");
    }

    public void reloadColors(){
        for (Map.Entry<MC4DConfig.FaceNames, MC4DConfig.ColorsStruct> entry : MC4DConfig.faceToColorTable.entrySet()) {

            String colorJson = this.prefs.getString(
                    MC4DConfig.faceToColorPrefTag.get(entry.getKey()),
                    null);
            Color color;
            if(colorJson == null)
                color = this.defaultColors[MC4DConfig.faceToColorArrayIndex.get(entry.getKey())];
            else
                color = this.gson.fromJson(colorJson, Color.class);

            this.colors[MC4DConfig.faceToColorArrayIndex.get(entry.getKey())] = color;
        }
        for (Map.Entry<MC4DConfig.Names, MC4DConfig.ColorsStruct> entry : MC4DConfig.nameToColorTable.entrySet()) {
            String colorJson = this.prefs.getString(
                    MC4DConfig.nameToColorPrefTag.get(entry.getKey()),
                    null);
            Color color;
            if (colorJson == null)
                color = this.defaultNameColor.get(entry.getKey());
            else
                color = this.gson.fromJson(colorJson, Color.class);

            this.nameColor.put(entry.getKey(), color);
        }
        onColorChangeAction();
    }

    public Color getFaceColor(MC4DConfig.FaceNames key) {
        return this.colors[MC4DConfig.faceToColorArrayIndex.get(key)];
    }
    public Color getNameColor(MC4DConfig.Names key) {
        return this.nameColor.get(key);
    }

    public void onColorChangeAction(){
        this.onColorsChange.onColorsChanged(
                this.colors, this.nameColor.values().toArray(new Color[this.nameColor.size()]));
    }

    public void setFaceColor(MC4DConfig.FaceNames key, Color color) {
        int ii = MC4DConfig.faceToColorArrayIndex.get(key);
        SharedPreferences.Editor prefsed = this.prefs.edit();
        if(color == null) {
            prefsed.remove(MC4DConfig.faceToColorPrefTag.get(key));
            this.colors[ii] = this.defaultColors[ii];
        }else{
            prefsed.putString(
                MC4DConfig.faceToColorPrefTag.get(key),
                gson.toJson(color)
            );
            this.colors[ii] = color;
        }
        prefsed.apply();
        onColorChangeAction();
    }

    public void setNameColor(MC4DConfig.Names key, Color color) {
        SharedPreferences.Editor prefsed = this.prefs.edit();
        if(color == null) {
            prefsed.remove(MC4DConfig.nameToColorPrefTag.get(key));
            this.nameColor.put(key, this.defaultNameColor.get(key));
        }else{
            prefsed.putString(
                    MC4DConfig.nameToColorPrefTag.get(key),
                    gson.toJson(color)
            );
            this.nameColor.put(key, color);
        }
        prefsed.apply();
        onColorChangeAction();
    }

    public void onAdjustmentsChangeAction(){
        this.onAdjustmentsChange.onAdjustmentsChanged(
                this.adjustmentValues.values().toArray(
                        new Float[this.adjustmentValues.size()]));
    }

    public void reloadAdjustment() {
        for (Map.Entry<MC4DConfig.Adjustments, MC4DConfig.AdjustmentStruct> entry : MC4DConfig.adjustmentToAdjustmentTabs.entrySet()) {
            String adjustmentJson = this.prefs.getString(
                    MC4DConfig.adjustmentsToAdjustmentPrefTag.get(entry.getKey()),
                    null);
            Float value;
            if (adjustmentJson == null)
                value = this.defaultAdjustmentValues.get(entry.getKey());
            else
                value = this.gson.fromJson(adjustmentJson, Float.class);
            this.adjustmentValues.put(entry.getKey(), value);
        }
        onAdjustmentsChangeAction();
    }

    public void setAdjustmentValue(MC4DConfig.Adjustments key, Float value) {
        SharedPreferences.Editor prefsed = this.prefs.edit();
        if(value == null) {
            prefsed.remove(MC4DConfig.adjustmentsToAdjustmentPrefTag.get(key));
            this.adjustmentValues.put(key, this.defaultAdjustmentValues.get(key));
        }else{
            prefsed.putString(
                    MC4DConfig.adjustmentsToAdjustmentPrefTag.get(key),
                    gson.toJson(value)
            );
            this.adjustmentValues.put(key, value);
        }
        prefsed.apply();
        onAdjustmentsChangeAction();
    }


    public void setControls(MC4DConfig.Controls key, String value) {
        SharedPreferences.Editor prefsed = this.prefs.edit();
        if(value == null) {
            prefsed.remove(MC4DConfig.controlsToControlsPrefTag.get(key));
            this.controlsSettings.put(key, this.defaultControlsSettings.get(key));
        }else{
            prefsed.putString(
                    MC4DConfig.controlsToControlsPrefTag.get(key),
                    value
            );
            this.controlsSettings.put(key, value);
        }
        prefsed.apply();
        this.onControlChange.onControlChange(Boolean.parseBoolean(this.controlsSettings.get(MC4DConfig.Controls.AMT)));

    }

    public String getControls(MC4DConfig.Controls key){
        return this.controlsSettings.get(key);
    }
    public void reloadControls() {
        for (Map.Entry<MC4DConfig.Controls, String> entry : MC4DConfig.controlsToControlsPrefTag.entrySet()) {
            String controlSetting = this.prefs.getString(
                    MC4DConfig.controlsToControlsPrefTag.get(entry.getKey()),
                    null);
            String value;
            if (controlSetting == null)
                value = this.defaultControlsSettings.get(entry.getKey());
            else
                value = controlSetting;
            this.controlsSettings.put(entry.getKey(), value);
        }
        this.onControlChange.onControlChange(Boolean.parseBoolean(this.controlsSettings.get(MC4DConfig.Controls.AMT)));
    }

}
