package com.tomas.cubesolver.cube;
/**
 * Holds each possible color value at each tile of the cube
 */
public enum Color {
	WHITE("W"), 
	YELLOW("Y"), 
	BLUE("B"), 
	RED("R"), 
	GREEN("G"), 
	ORANGE("O");
	
	private String mName;
	
	private Color(String name){
		mName = name;
	}

	@Override
	public String toString() {
		return mName;
	}
}
