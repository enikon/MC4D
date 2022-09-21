package com.cyanheron.magiccube4d.gui;

import java.util.LinkedHashMap;
import java.util.Map;

public class MC4DConfig {
    public static enum FaceNames{
        FRONT, BACK, TOP, BOTTOM, LEFT, RIGHT, UPPER, LOWER
    }

    public static enum Names{
        BACKGROUND, ACTIVE
    }

    public static enum Controls{
        AMT
    }

    public static enum Adjustments{
        VIEW_SCALE, FACE_SHRINK, STICKER_SHRINK, EYE_W_SCALE
    }

    public static Map<FaceNames, Integer> faceToColorArrayIndex;
    static {
        faceToColorArrayIndex = new LinkedHashMap<>();
        faceToColorArrayIndex.put(FaceNames.FRONT, 6);
        faceToColorArrayIndex.put(FaceNames.BACK, 1);
        faceToColorArrayIndex.put(FaceNames.TOP, 0);
        faceToColorArrayIndex.put(FaceNames.BOTTOM, 7);
        faceToColorArrayIndex.put(FaceNames.LEFT, 5);
        faceToColorArrayIndex.put(FaceNames.RIGHT, 2);
        faceToColorArrayIndex.put(FaceNames.UPPER, 4);
        faceToColorArrayIndex.put(FaceNames.LOWER, 3);
    }

    public static class ColorsStruct{
        public int X=0, Y=0;
        public String name = "";
        public ColorsStruct(int X, int Y, String name){
            this.X = X;
            this.Y = Y;
            this.name = name;
        }
    }
    public static Map<FaceNames, ColorsStruct> faceToColorTable;
    static {
        faceToColorTable = new LinkedHashMap<>();
        faceToColorTable.put(FaceNames.FRONT, new ColorsStruct(0, 0, "Front Face Color"));
        faceToColorTable.put(FaceNames.BACK, new ColorsStruct(1, 0, "Back Face Color"));
        faceToColorTable.put(FaceNames.TOP, new ColorsStruct(0, 1, "Top Face Color"));
        faceToColorTable.put(FaceNames.BOTTOM, new ColorsStruct(1, 1, "Bottom Face Color"));
        faceToColorTable.put(FaceNames.LEFT, new ColorsStruct(0, 2, "Left Face Color"));
        faceToColorTable.put(FaceNames.RIGHT, new ColorsStruct(1, 2, "Right Face Color"));
        faceToColorTable.put(FaceNames.UPPER, new ColorsStruct(0, 3, "Upper Face Color"));
        faceToColorTable.put(FaceNames.LOWER, new ColorsStruct(1, 3, "Lower Face Color"));
    }
    public static Map<Names, ColorsStruct>  nameToColorTable;
    static {
        nameToColorTable = new LinkedHashMap<>();
        nameToColorTable.put(Names.BACKGROUND, new ColorsStruct(0, 0, "Background Color"));
        nameToColorTable.put(Names.ACTIVE, new ColorsStruct(1, 0, "Active Element Color"));
    }

    public static Map<FaceNames, String> faceToColorPrefTag;
    static {
        faceToColorPrefTag = new LinkedHashMap<>();
        faceToColorPrefTag.put(FaceNames.FRONT, "face_color_front");
        faceToColorPrefTag.put(FaceNames.BACK, "face_color_back");
        faceToColorPrefTag.put(FaceNames.TOP, "face_color_top");
        faceToColorPrefTag.put(FaceNames.BOTTOM, "face_color_bottom");
        faceToColorPrefTag.put(FaceNames.LEFT, "face_color_left");
        faceToColorPrefTag.put(FaceNames.RIGHT, "face_color_right");
        faceToColorPrefTag.put(FaceNames.UPPER, "face_color_upper");
        faceToColorPrefTag.put(FaceNames.LOWER, "face_color_lower");
    }
    public static Map<Names, String>  nameToColorPrefTag;
    static {
        nameToColorPrefTag = new LinkedHashMap<>();
        nameToColorPrefTag.put(Names.BACKGROUND, "color_background");
        nameToColorPrefTag.put(Names.ACTIVE, "color_active");
    }

    public static Map<Adjustments, String>  adjustmentsToAdjustmentPrefTag;
    static {
        adjustmentsToAdjustmentPrefTag = new LinkedHashMap<>();
        adjustmentsToAdjustmentPrefTag.put(Adjustments.VIEW_SCALE, "adjustment_scale");
        adjustmentsToAdjustmentPrefTag.put(Adjustments.FACE_SHRINK, "adjustment_4D_scale");
        adjustmentsToAdjustmentPrefTag.put(Adjustments.STICKER_SHRINK, "adjustment_3D_scale");
        adjustmentsToAdjustmentPrefTag.put(Adjustments.EYE_W_SCALE, "adjustment_4D_fov");
    }

    public static class AdjustmentStruct{
        public float minValue=0, maxValue=1;
        public String name = "";
        public String abbrev = "";
        public String property = "";

        public AdjustmentStruct(float minValue, float maxValue, String name,  String abbrev, String property){
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.name = name;
            this.abbrev = abbrev;
            this.property = property;
        }
    }

    public static Map<Adjustments, AdjustmentStruct> adjustmentToAdjustmentTabs;
    static {
        adjustmentToAdjustmentTabs = new LinkedHashMap<>();
        adjustmentToAdjustmentTabs.put(Adjustments.VIEW_SCALE, new AdjustmentStruct(0.25f, 2.5f, "Global Scale (XYZW-axes) \n (Whole hypercube scale)", "GSc", "scale"));
        adjustmentToAdjustmentTabs.put(Adjustments.FACE_SHRINK, new AdjustmentStruct(0.25f, 1.0f, "Scale in 4D (W-axis) \n (Face scale)", "4DSc", "faceshrink"));
        adjustmentToAdjustmentTabs.put(Adjustments.STICKER_SHRINK, new AdjustmentStruct(0f, 1f, "Scale in 3D (XYZ-axis) \n (Stickers scale)", "3DSc", "stickershrink"));
        adjustmentToAdjustmentTabs.put(Adjustments.EYE_W_SCALE, new AdjustmentStruct(0.625f, 1.5f, "Field of view in 4D (W-axis) \n (Side faces distortion)", "4DFov", "eyew"));
    }

    public static Map<Controls, String>  controlsToControlsPrefTag;
    static {
        controlsToControlsPrefTag = new LinkedHashMap<>();
        controlsToControlsPrefTag.put(Controls.AMT, "advanced_control_settings");
    }

    //faceshrink stickershrink scale eyew

}
