package com.tomas.neuralNetwork.search;


/**
 * SearchFacade.java
 * 
 * Contains wrapper to call each search algorithm
 * 
 * January 30th, 2015 
 * @author Aaron Germuth
 */
public class SearchFacade {
	public static SearchNode runSearch(SearchType st, Searchable start, Searchable goal){
		start.eraseMoveHistory();
		switch(st){
			case ASTAR:
				return AStarSearch.runSearch(start,  goal);
			case BFS:
				return BreadthFirstSearch.runSearch(start, goal);
			default:
				return BreadthFirstSearch.runSearch(start, goal);
		}
	}
}
