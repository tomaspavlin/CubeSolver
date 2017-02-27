package com.tomas.cubesolver.graphics;

/**
 * Represents a Color in OpenGL
 * Red green blue are float values from 0 -> 1.0
 * 
 */
public class StickerColor {

	private final float red;
	private final float green;
	private final float blue;

	public StickerColor(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof StickerColor) {
			StickerColor g = (StickerColor) o;
			if (this.red == g.red && this.green == g.green && this.blue == g.blue) {
				return true;
			}
		}
		return false;
	}

	public float getRed() {
		return red;
	}
	public float getGreen() {
		return green;
	}
	public float getBlue() {
		return blue;
	}
}
