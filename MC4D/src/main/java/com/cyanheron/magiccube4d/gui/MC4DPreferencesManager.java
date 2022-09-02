package com.cyanheron.magiccube4d.gui;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.superliminal.util.android.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MC4DPreferencesManager {

    private final Gson gson;
    private final SharedPreferences prefs;
    private Context context;

    public Color[] defaultColors;
    public Color[] colors;

    public HashMap<MC4DConfig.Names, Color> nameColor;
    public HashMap<MC4DConfig.Names, Color> defaultNameColor;

    private OnColorsChange onColorsChange;

    public MC4DPreferencesManager(
            Context context,
            Color[] defaultColors,
            Color[] defaultNameColors,
            OnColorsChange onColorsChange
    ){
        this.context = context;
        this.defaultColors = defaultColors;
        this.colors = Arrays.<Color>copyOf(this.defaultColors, this.defaultColors.length);
        this.onColorsChange = onColorsChange;
        this.gson = new Gson();
        this.prefs = this.context.getSharedPreferences("mc4d", 0);


        this.nameColor = new HashMap<>();
        this.defaultNameColor = new HashMap<>();
        for(MC4DConfig.Names n : MC4DConfig.Names.values()){
            this.defaultNameColor.put(n, defaultNameColors[n.ordinal()]);
        }
        this.reloadColors();
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
        performAction();
    }

    public Color getFaceColor(MC4DConfig.FaceNames key) {
        return this.colors[MC4DConfig.faceToColorArrayIndex.get(key)];
    }
    public Color getNameColor(MC4DConfig.Names key) {
        return this.nameColor.get(key);
    }

    public void performAction(){
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
        performAction();
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
        performAction();
    }
}
