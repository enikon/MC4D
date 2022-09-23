package com.cyanheron.magiccube4d.gui;

import android.view.MenuItem;

import java.io.File;

public class MenuItemHandler{

    private String baseName;
    private MenuItem mi;
    private StringMapper s;

    public MenuItemHandler(MenuItem mi, StringMapper s){
        this.mi = mi;
        this.baseName = this.mi.getTitle().toString().split("\\*")[0];
        this.s = s;
        update();

    }

    public void update(){
        this.mi.setTitle(this.baseName + this.s.map());
    }
}
