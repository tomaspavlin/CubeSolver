package com.tomas.cubesolver.graphics;

/**
 * Class for operation with vertex
 */
public class Vertex {

	private float x;
	private float y;
	private float z;

	public Vertex() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vertex(Vertex other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	public Vertex(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Vertex) {
			Vertex g = (Vertex) o;
			if (this.x == g.x && this.y == g.y && this.z == g.z) {
				return true;
			}
		}
		return false;
	}

	/**
	 * translate point across one axis
	 */
	public void translate(char axis, double distance) {
		if (axis == 'X') {
			this.x += distance;
		} else if (axis == 'Y') {
			this.y += distance;
		} else if (axis == 'Z') {
			this.z += distance;
		}
	}

	/**
	 * rotate a point around an axis (X, Y, Z) radians amount
	 */
	public void rotate(char axis, float radians) {
		float x = this.x;
		float y = this.y;
		float z = this.z;

		float x2 = x;
		float y2 = y;
		float z2 = z;

		if (axis == 'X') {
			// y' = y*cos q - z*sin q
			// z' = y*sin q + z*cos q
			// x' = x
			y2 = (float) (y * Math.cos(radians) - z * Math.sin(radians));
			z2 = (float) (y * Math.sin(radians) + z * Math.cos(radians));
		} else if (axis == 'Y') {
			// z' = z*cos q - x*sin q
			// x' = z*sin q + x*cos q
			// y' = y
			z2 = (float) (z * Math.cos(radians) - x * Math.sin(radians));
			x2 = (float) (z * Math.sin(radians) + x * Math.cos(radians));
		} else if (axis == 'Z') {
			// x' = x*cos q - y*sin q
			// y' = x*sin q + y*cos q
			// z' = z
			x2 = (float) (x * Math.cos(radians) - y * Math.sin(radians));
			y2 = (float) (x * Math.sin(radians) + y * Math.cos(radians));
		}

		// set new coordinates
		this.x = x2;
		this.y = y2;
		this.z = z2;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
}
