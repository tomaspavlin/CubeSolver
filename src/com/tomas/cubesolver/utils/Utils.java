package com.tomas.cubesolver.utils;

import java.util.*;

import com.tomas.cubesolver.cube.Color;

/**
 * Useful static method used in other classes
 */
public class Utils {
	
	/**
	 * Returns a deep copy of the 2D array provided
	 * @param arr Matrix, must be square
	 * @return Array copy
	 */
	public static Color[][] copy(Color[][] arr){
		int n = arr.length;
		Color[][] copy = new Color[n][n];
		for(int i = 0; i < n; i++)
			copy[i] = Arrays.copyOf(arr[i], n);
		return copy;
	}
	
	/**
	 * Rotates matrix 90 degrees clockwise
	 * @param arr Matrix, must be square
	 * @return Rotated matrix
	 */
	public static Color[][] rotateMatrixClockwise(Color[][] arr){
		final int n = arr.length;
		Color[][] arrCopy = new Color[n][n];
		
		for (int i = 0; i < n; ++i)
	        for (int j = 0; j < n; ++j) 
	        	arrCopy[i][j] = arr[n-j-1][i];

	    return arrCopy;
	}
	
	/**
	 * Returns the number of distinct colors in the provided array
	 */
	public static int numberColours(Color[][] face){
		int number = 0;
		ArrayList<Color> colorsSoFar = new ArrayList<Color>();
		for(int i = 0; i < face.length; i++)
			for(int j = 0; j < face[i].length; j++)
				if(!colorsSoFar.contains(face[i][j])){
					colorsSoFar.add(face[i][j]);
					number++;
				}

		return number;
	}
}
