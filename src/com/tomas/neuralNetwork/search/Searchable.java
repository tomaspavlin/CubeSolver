package com.tomas.neuralNetwork.search;

import java.util.ArrayList;
/**
 * Searchable.java
 * 
 * Interface to allow an object to be searched
 * 
 * January 30th, 2015 
 * @author Aaron Germuth
 */
public interface Searchable{
	//Enumerate all states from each possible move 
	public ArrayList<Searchable> getChildren();
	//Unique key for each unique state of searchable
	public String getKey();
	//returns the last move taken, or null if no move has been taken
	public String getMoveTaken();
	//erases the searchable's move history before beginning the search
	public void eraseMoveHistory();
	//calculate heuristic value, estimating number of moves until solution
	public double calcHeuristic();
}
