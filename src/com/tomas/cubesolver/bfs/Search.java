/**
 * The Search interface is implemented by both Cube and XOR and allows it to be used in BFSearcher to find the solution
 * so that it could generate training data
 */
package com.tomas.cubesolver.bfs;

import java.util.ArrayList;
/**
 * Interface to allow an object to be searched
 */
public interface Search{
	/**
	 * Enumerate all states from each possible move 
	 */
	public ArrayList<Search> getChildren();
	/**
	 * Unique key for each unique state of search
	 */
	public String getKey();
	/**
	 * returns the last move taken, or null if no move has been taken
	 */
	public String getMoveTaken();
	/**
	 * erases the search move history before beginning the search
	 */
	public void eraseMoveHistory();
	/**
	 * calculate heuristic value, estimating number of moves until solution
	 */
	public double calcHeuristic();
}
