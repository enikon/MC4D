package com.cyanheron.magiccube4d.gui;

import android.content.Context;
import android.content.SharedPreferences;

import com.superliminal.magiccube4d.MagicCube;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveManager {
    private File directory;
    private int slot;
    private String fileName, fileExt;
    private Context context;
    public boolean lock=false;

    public SaveManager(Context context){
        this.context = context;
        this.directory = context.getFilesDir();
        this.fileName = MagicCube.LOG_FILE.split("\\.")[0];
        this.fileExt = "."+MagicCube.LOG_FILE.split("\\.")[1];

        determineLastSlot();
    }

    public void determineLastSlot(){
        SharedPreferences prefs = context.getSharedPreferences(MC4DConfig.APPNAME, 0);
        if(prefs.contains(MC4DConfig.currentSaveSlotToPrefTag)) {
            this.slot = prefs.getInt(MC4DConfig.currentSaveSlotToPrefTag, 1);
        }
        else{
            this.slot = 1;
            prefs.edit().putInt(MC4DConfig.currentSaveSlotToPrefTag, 1).apply();
        }
    }

    public String getInfo(int slot, boolean auto){
        File f = getFile(slot, auto);
        Date lastModified = new Date(f.lastModified());
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
        return formatter.format(lastModified);
    }

    public String getCurrentInfo(boolean auto){
        return getInfo(this.slot, auto);
    }

    public int getSlot(){
        return this.slot;
    }

    public void setSlot(int slot){
        this.slot = slot;
        SharedPreferences prefs = context.getSharedPreferences(MC4DConfig.APPNAME, 0);
        prefs.edit().putInt(MC4DConfig.currentSaveSlotToPrefTag, slot).apply();
    }

    public File getFile(int slot, boolean auto){
        return new File(this.directory, this.fileName+"_"+slot+(auto?"_auto":""+this.fileExt));
    }
    public File getCurrentFile(boolean auto){
        return getFile(this.slot, auto);
    }

}
