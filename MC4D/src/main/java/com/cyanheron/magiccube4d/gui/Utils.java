package com.cyanheron.magiccube4d.gui;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.superliminal.magiccube4d.Vec_h;

import java.util.Arrays;

public class Utils {
    public static String DIALOG_TAG = "dialog";

    public static FragmentTransaction prepareDialog(AppCompatActivity activity) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//        Fragment prev = activity.getFragmentManager().findFragmentByTag(DIALOG_TAG);
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);
        return ft;
    }

    public static <T> void arrayCopy(T[] src, T[] dst) {
        System.arraycopy(src, 0, dst, 0, src.length);
    }
    public static <T> void arrayCopy(T[][] src, T[][] dst) {
        for(int i=0; i<src.length; i++){
            System.arraycopy(src[i], 0, dst[i], 0, src[0].length);
        }
    }
    public static <T> void arrayCopy(T[][][] src, T[][][] dst) {
        for(int i=0; i<src.length; i++){
            for(int j=0; j<src[0].length; j++) {
                System.arraycopy(src[i][j], 0, dst[i][j], 0, src[0][0].length);
            }
        }
    }

    public static float[] getStickerCenter(float[] dest, float[][] verts, int[][] ind){
        for(int l=0; l<4; l++) {
            dest[l] = 0f;
        for(int k=0; k<4; k++) {
        for(int j=0; j<6; j++) {
            dest[l] += verts[ind[j][k]][l]/24f;
        }}}
        return dest;
    }

    public static void _VMV4(float[] af, float[] af1, float[] af2) {
        float x = ((af[0] = af1[0] - af2[0]) - (af[1] = af1[1] - af2[1]) - (af[2] = af1[2] - af2[2]) - (af[3] = af1[3] - af2[3]));
    }
    public static void _VXV4(float[] af, float[] af1, float[] af2) {
        float x = ((af[0] = af1[0] * af2[0]) - (af[1] = af1[1] * af2[1]) - (af[2] = af1[2] * af2[2]) - (af[3] = af1[3] * af2[3]));
    }
    public static void _VXS4(float[] af, float[] af1, float af2) {
        float x = ((af[0] = af1[0] * af2) - (af[1] = af1[1] * af2) - (af[2] = af1[2] * af2) - (af[3] = af1[3] * af2));
    }
    public static float _PROD4(float[] af) {
        return af[0] * af[1] * af[2] * af[3];
    }
    public static void _SIGN4(float[] af, float[] af1) {
        float x = ((af[0] = sign(af1[0])) - (af[1] = sign(af1[1])) - (af[2] = sign(af1[2])) - (af[3] = sign(af1[3])));
    }
    public static float sign(float a){
        if (a>0 && a>1e-6) return 1;
        else if (a<0 && a<-1e-6) return -1;
        else return 0;
    }

    public static float _NORMSQRD4(float[] af) {
        return (af[0] * af[0]) + (af[1] * af[1]) + (af[2] * af[2]) + (af[3] * af[3]);
    }


    /*
       vsmap from {1, -2/3, 0, 0} -> index
       -2/3=b101, +2/3=b111
       -1=b100 , +1=b110,
        0=b000
        3*4 = 12, 2^12 = 4096
    */
    public static int neq(float a, float b){
        return (Math.abs(a - b) < 1e-6)?0:1;
    }
    public static int pos(float a){
        return a>0?1:0;
    }
    public static int neq_abs(float a, float b){
        return (Math.abs(Math.abs(a) - b) < 1e-6)?0:1;
    }

    public static int centerToIndex(float[] c) {
        int z = 0, nonzero;
        for(int i=0; i<c.length; i++) {
            nonzero = neq(c[i], 0f);
            z += (
                  (nonzero << 2)
                + ((pos(c[i]) & nonzero) << 1)
                + (neq_abs(c[i], 1f) & nonzero)
            )<<(3*i);
        }
        return z;
    }

}
