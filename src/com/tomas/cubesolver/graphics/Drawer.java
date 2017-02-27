package com.tomas.cubesolver.graphics;

import java.util.ArrayList;

import com.tomas.cubesolver.cube.Cube;
import com.tomas.cubesolver.cube.Face;

/**
 * Static class containing drawing operations for a NxNxN rubik's cube
 */
public class Drawer {
	
	//total size of a face of the rubiks cube
	private static final float TOTAL_FACE_LENGTH = 24f;
	//total size of the spaces between each face of a rubiks cube
	private static final float TOTAL_SPACE_LENGTH = 4f;
	//specifies how large the cube, ie distance from middle to each face
	private static final float TOTAL_CUBE_SIZE = 14f;
	
	/**
	 * in order to create a cube, we can simply draw one face of the cube
	 * 6 times and rotate a different way each time
	 */
	public static ArrayList<Sticker> createPuzzleModel(Cube cube) {
		ArrayList<Sticker> myFaces = new ArrayList<Sticker>();

		myFaces.addAll(drawFace(Face.UP, cube));
		
		ArrayList<Sticker> front = drawFace(Face.FRONT, cube);
		Sticker.rotateAll(front, 'X', (float) Math.PI / 2);
		myFaces.addAll(front);
		
		ArrayList<Sticker> back = drawFace(Face.BACK, cube);
		Sticker.rotateAll(back, 'X', (float) -Math.PI / 2);
		myFaces.addAll(back);
		
		ArrayList<Sticker> left = drawFace(Face.LEFT, cube);
		Sticker.rotateAll(left, 'Z', (float) (Math.PI / 2));
		myFaces.addAll(left);
		
		ArrayList<Sticker> right = drawFace(Face.RIGHT, cube);
		Sticker.rotateAll(right, 'Z', (float) (-Math.PI / 2));
		myFaces.addAll(right);
		
		ArrayList<Sticker> bot = drawFace(Face.DOWN, cube);
		Sticker.rotateAll(bot, 'X', (float) Math.PI);
		myFaces.addAll(bot);

		return myFaces;
	}

	/**
	 * draws a single face of the cube
	 */
	private static ArrayList<Sticker> drawFace(Face face, Cube cube) {
		int size = cube.getsize();
		
		ArrayList<Sticker> faceShapes = new ArrayList<Sticker>();
		
		//how large each tile of the face is
		float tileLength = (TOTAL_FACE_LENGTH / size);

		//there are size + 1 spaces, since there is one at either corner
		float spaceLength = TOTAL_SPACE_LENGTH / (size + 1);

		float height = TOTAL_CUBE_SIZE;
		float topCornerX = -TOTAL_CUBE_SIZE;
		float topCornerZ = -TOTAL_CUBE_SIZE;

		topCornerX += spaceLength;
		topCornerZ += spaceLength;

		float currentTopRightX = topCornerX;
		float currentTopRightZ = topCornerZ;

		// iterate down
		for (int i = 0; i < size; i++) {
			// reset top right coordinates after each row
			currentTopRightX = topCornerX;

			// iterate across
			for (int j = 0; j < size; j++) {

				// make face
				Sticker current = new Sticker(
						new Vertex(currentTopRightX, height, currentTopRightZ), new Vertex(
								currentTopRightX, height, currentTopRightZ + tileLength),
						new Vertex(currentTopRightX + tileLength, height, currentTopRightZ
								+ tileLength), new Vertex(currentTopRightX + tileLength, height,
								currentTopRightZ), face, i, j);
				faceShapes.add(current);

				currentTopRightX += tileLength + spaceLength;
			}

			// once we finish a row, we need to move down to next row
			currentTopRightZ += tileLength + spaceLength;
		}

		return faceShapes;
	}
}
