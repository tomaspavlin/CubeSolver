package com.tomas.cubesolver.bfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Modified Breadth-first search on any Search object.
 */
public class BFSearcher {
	public static Node runSearch(Search start, Search goal){
		//Openlist is constantly sorted by natural ordering (searchnode.compareTo)
		PriorityQueue<Node> openList = new PriorityQueue<Node>();
		HashMap<String, Node> closedList = new HashMap<String, Node>();
		//add start state
		Node startingNode = new Node(start, 0, null);
		openList.add(startingNode);
		String goalKey = goal.getKey();
		
		while(!openList.isEmpty()){
			Node current = openList.poll();
			String currKey = current.getSearch().getKey();
			closedList.put(currKey, current);
			
			if(currKey.equals(goalKey)){
				return current;
			}
			
			ArrayList<Search> children = current.getSearch().getChildren();
			//search all children
			for(Search child: children){
				String childKey = child.getKey();
				Node childNode = new Node(child, current.getHValue() + 1, current);
				
				//if we already visited that node
				if(closedList.containsKey(childKey)){
					double childG = childNode.getGValue();
					double closedListG = closedList.get(childKey).getGValue();
					//check our previous distance to new distance
					if(childG < closedListG){
						//found shorter path to some node already found
						closedList.remove(childKey);
						openList.add(childNode);
					}
				}else{
					//add unvisited node
					openList.add(childNode);
				}
			}
		}
		
		//goal state not found!
		return null;
	}
}
