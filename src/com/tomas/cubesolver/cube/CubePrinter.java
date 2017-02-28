package com.tomas.cubesolver.cube;

import com.tomas.cubesolver.cube.*;

/**
 * This class defines only one static method for printing cube as a string
 */
public class CubePrinter {
	private CubePrinter(){}
		
	/**
	 * Prints cube as a string so the user could see its configuration
	 * the six colors are displayed as six letters W,Y,B,R,G,O.
	 * The 2D network of the 3D cube is displayed with it's sides in following
	 * schema:
	 *      back
	 * left up   right
	 *      front
	 *      down
	 */
	public static void printCube(Cube cube){
		int v = cube.getsize();
		Color[][] arr = new Color[v*3][v*4];
		
		mapFace(cube.back,arr,v*1,v*0);
		mapFace(cube.left,arr,v*0,v*1);
		mapFace(cube.up,arr,v*1,v*1);
		mapFace(cube.right,arr,v*2,v*1);
		mapFace(cube.front,arr,v*1,v*2);
		mapFace(cube.down,arr,v*1,v*3);
		
		for(int j = 0; j < v*4; ++j){
			if(j%v == 0)
				System.out.println();
			for(int i = 0; i < v*3; ++i)
			{
				if(i%v == 0)
					System.out.print("  ");
				Color c = arr[i][j];
				if(c == null){
					System.out.print(" ");
				} else {
					System.out.print(c);
				}
				
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
		
	}
	
	/**
	 * this ethod maps cube face into color array that is used for displaying the cube 2d network
	 * @param face must be square
	 * @param arr must be of the same type as face
	 * @param x offset x
	 * @param y offset y
	 */
	private static <T> void mapFace(T[][] face, T[][] arr, int x, int y){
		for(int i = 0; i < face.length; i++)
			for(int j = 0; j < face.length; ++j){
				arr[i+x][j+y] = face[j][i];
			}
	}
	
}
