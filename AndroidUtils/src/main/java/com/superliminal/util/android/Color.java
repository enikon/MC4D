package com.superliminal.util.android;

/**
 * An Android replacement for java.awt.Color.
 * Used to leave Java SE code alone and provides an intValue method
 * for when you need to supply an Android color.
 * 
 * @author Melinda Green
 */
public class Color {
	private float r, g, b;
	private static final float SCALE_UP = 1.2f, SCALE_DOWN = 1/SCALE_UP;
	
	// Predefined colors. Add more as needed.
	public static Color black = new Color(0, 0, 0);
	public static Color gray = new Color(.5f, .5f, .5f);
	public static Color white = new Color(1, 1, 1);
	public static Color red = new Color(1, 0, 0);
	public static Color green = new Color(0, 1, 0);
	public static Color blue = new Color(0, 0, 1);
	public static Color magenta = new Color(1, 0, 1);
	public static Color yellow = new Color(1, 1, 0);
	public static Color cyan = new Color(0, 1, 1);
    public static Color brown = new Color(.6f, .8f, .3f);
    public static Color rose = new Color(1, .2f, .7f);
	
	private Color mDarker, mBrighter; // Caching for speed and garbage control.

	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public Color(int c){
		this.r = ((c >> 16) & 0xff) / 255f;
		this.g = ((c >> 8) & 0xff) / 255f;
		this.b = ((c >> 0) & 0xff) / 255f;
	}

	public Color() {
		r = g = b = 0;
	}

	public void getColorComponents(float[] rgb) {
		rgb[0] = r;
		rgb[1] = g;
		rgb[2] = b;
	}
	
	public void setColorComponents(float[] rgb) {
		r = rgb[0];
		g = rgb[1];
		b = rgb[2];
	}
	
	public static Color decode(String str) {
		return new Color(android.graphics.Color.parseColor(str));
	}

	public Color darker() {
		if(mDarker == null)
			mDarker = new Color(r*SCALE_DOWN, g*SCALE_DOWN, b*SCALE_DOWN);
		return mDarker;
	}

	public Color brighter() {
		if(mBrighter == null)
			mBrighter = new Color(
			Math.max(r*SCALE_UP, .9f), 
			Math.max(g*SCALE_UP, .9f), 
			Math.max(b*SCALE_UP, .9f));
		return mBrighter;
	}
	
	private static int intVal(float component) { return Math.round(255 * component); }

	public static int intValue(float r, float g, float b) {
		int iR = intVal(r), iG = intVal(g), iB = intVal(b);
		int iABits = 255 << 8*3; // Alpha is in the high bits. Always fully opaque for now.
		int iRBits = iR  << 8*2;
		int iGBits = iG  << 8*1;
		int iBBits = iB  << 8*0;
		int packed = iABits | iRBits | iGBits | iBBits;
		return packed;
	}
	
	public int intValue() { 
		return intValue(r, g, b); 
	}

}
