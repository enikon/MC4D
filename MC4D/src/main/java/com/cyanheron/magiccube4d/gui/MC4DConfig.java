package com.cyanheron.magiccube4d.gui;

import java.util.HashMap;
import java.util.Map;

public class MC4DConfig {
    public static enum FaceNames{
        FRONT, BACK, TOP, BOTTOM, LEFT, RIGHT, UPPER, LOWER
    }

    public static enum Names{
        BACKGROUND, ACTIVE
    }

    public static Map<FaceNames, Integer> faceToColorArrayIndex;
    static {
        faceToColorArrayIndex = new HashMap<>();
        faceToColorArrayIndex.put(FaceNames.FRONT, 2);
        faceToColorArrayIndex.put(FaceNames.BACK, 5);
        faceToColorArrayIndex.put(FaceNames.TOP, 0);
        faceToColorArrayIndex.put(FaceNames.BOTTOM, 7);
        faceToColorArrayIndex.put(FaceNames.LEFT, 3);
        faceToColorArrayIndex.put(FaceNames.RIGHT, 4);
        faceToColorArrayIndex.put(FaceNames.UPPER, 6);
        faceToColorArrayIndex.put(FaceNames.LOWER, 1);
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
        faceToColorTable = new HashMap<>();
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
        nameToColorTable = new HashMap<>();
        nameToColorTable.put(Names.BACKGROUND, new ColorsStruct(0, 0, "Background Color"));
        nameToColorTable.put(Names.ACTIVE, new ColorsStruct(1, 0, "Active Element Color"));
    }

    public static Map<FaceNames, String> faceToColorPrefTag;
    static {
        faceToColorPrefTag = new HashMap<>();
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
        nameToColorPrefTag = new HashMap<>();
        nameToColorPrefTag.put(Names.BACKGROUND, "color_background");
        nameToColorPrefTag.put(Names.ACTIVE, "color_active");
    }

}
