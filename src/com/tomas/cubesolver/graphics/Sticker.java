package com.tomas.cubesolver.graphics;

import java.util.ArrayList;

import com.tomas.cubesolver.cube.Color;
import com.tomas.cubesolver.cube.Cube;
import com.tomas.cubesolver.cube.Face;

/**
 * Utility class for openGL to hold a square
 */
public class Sticker {
	
	private Face face;
	private int row;
	private int col;

	private ArrayList<Vertex> verticies;
	
	public Sticker(Vertex one, Vertex two, Vertex three, Vertex four, Face f, int r, int c) {
		this.verticies = new ArrayList<Vertex>();
		this.verticies.add(one);
		this.verticies.add(two);
		this.verticies.add(three);
		this.verticies.add(four);
		face = f;
		row = r;
		col = c;
	}
	
	public Vertex getTopLeft(){
		return verticies.get(0);
	}
	public Vertex getBottomLeft(){
		return verticies.get(1);
	}
	public Vertex getBottomRight(){
		return verticies.get(2);
	}
	public Vertex getTopRight(){
		return verticies.get(3);
	}
	public StickerColor getCurrentColor(Cube cube) {
		return toGLColor(cube.getColor(face, row, col));
	}
	
	/**
	 * Rotates this square around either the x y or z axis, by an amount in radians
	 * @param axis, the axis you are rotating around, may be 'X', 'Y', or 'Z'
	 * @param radians, the degree in radians you want to rotate around it
	 */
	public void rotate(char axis, float radians){
		for(int i = 0; i < this.verticies.size(); i++){
			Vertex current = this.verticies.get(i);
			current.rotate(axis, radians);
		}	
	}
	
	public static void rotateAll(ArrayList<Sticker> face, char axis, float radians){
		for(int i = 0; i < face.size(); i++){
			Sticker s = face.get(i);
			s.rotate(axis, radians);
		}
	}
	
	public static StickerColor toGLColor(Color col){
		switch(col){
			case RED: return new StickerColor(1f, 0, 0);
			case GREEN: return new StickerColor(0, 1f, 0);
			case BLUE: return new StickerColor(0, 0, 1f);
			case WHITE: return new StickerColor(1f, 1f, 1f);
			case YELLOW: return new StickerColor(1f, 1f, 0f);
			case ORANGE: return new StickerColor(1f, 0.5f, 0f);
			default:
				return null;
		}
	}
}
