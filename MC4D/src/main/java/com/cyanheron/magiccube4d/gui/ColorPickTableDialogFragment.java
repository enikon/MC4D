package com.cyanheron.magiccube4d.gui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;

import com.google.gson.Gson;
import com.superliminal.magiccube4d.R;
import com.superliminal.util.android.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ColorPickTableDialogFragment extends DialogFragment {

    public HashMap<String, Color> colors;
    public MC4DPreferencesManager manager;

    public ColorPickTableDialogFragment(){

    }
    public static ColorPickTableDialogFragment newInstance(MC4DPreferencesManager manager) {
        ColorPickTableDialogFragment f = new ColorPickTableDialogFragment();
//        Bundle args = new Bundle();
//        ArrayList<HashMap> list = new ArrayList<>();
//        args.putSerializable("colors", list);
//        f.setArguments(args);
        f.manager = manager;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        HashMap<String, Color> colors = (HashMap<String, Color>)(
//                ((ArrayList<HashMap>)(savedInstanceState.getSerializable("colors"))).get(0)
//            );

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.colors, container);
        GridLayout gl = (GridLayout) v.findViewById(R.id.grid_color);

        for (Map.Entry<MC4DConfig.FaceNames, MC4DConfig.ColorsStruct> entry : MC4DConfig.faceToColorTable.entrySet()) {
            ColorPickEntity cpe = makeFaceColorEntity(v.getContext(), entry);
            GridLayout.LayoutParams glp = new GridLayout.LayoutParams(
                    GridLayout.spec(entry.getValue().Y+1, 1, 1f),
                    GridLayout.spec(entry.getValue().X, 1, 1f)
            );
            gl.addView(cpe, glp);
        }
        for (Map.Entry<MC4DConfig.Names, MC4DConfig.ColorsStruct> entry : MC4DConfig.nameToColorTable.entrySet()) {
            ColorPickEntity cpe = makeColorEntity(v.getContext(), entry);
            GridLayout.LayoutParams glp = new GridLayout.LayoutParams(
                    GridLayout.spec(0, 1, 1f),
                    GridLayout.spec(entry.getKey().ordinal(), 1, 1f)
            );
            gl.addView(cpe, glp);
            //break;
        }



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

    public ColorPickEntity makeFaceColorEntity(
            Context context,
            Map.Entry<MC4DConfig.FaceNames, MC4DConfig.ColorsStruct> entry
        ){
        ColorPickEntity cpe = new ColorPickEntity(context);
        cpe.setName(entry.getValue().name);
        cpe.setColor(manager.getFaceColor(entry.getKey()));
        cpe.setOnColorChangeListener(
                color -> {
                    manager.setFaceColor(entry.getKey(), color);
                    cpe.setColor(manager.getFaceColor(entry.getKey()));
                }
        );
        cpe.setOnColorResetListener(
                color -> {
                    manager.setFaceColor(entry.getKey(), null);
                    cpe.setColor(manager.getFaceColor(entry.getKey()));
                }
        );
        return cpe;
    }

    public ColorPickEntity makeColorEntity(
            Context context,
            Map.Entry<MC4DConfig.Names, MC4DConfig.ColorsStruct> entry
    ){
        ColorPickEntity cpe = new ColorPickEntity(context);
        cpe.setName(entry.getValue().name);
        cpe.setColor(manager.getNameColor(entry.getKey()));
        cpe.setOnColorChangeListener(
                color -> {
                    manager.setNameColor(entry.getKey(), color);
                    cpe.setColor(manager.getNameColor(entry.getKey()));
                }
        );
        cpe.setOnColorResetListener(
                color -> {
                    manager.setNameColor(entry.getKey(), null);
                    cpe.setColor(manager.getNameColor(entry.getKey()));
                }
        );
        return cpe;
    }
}
