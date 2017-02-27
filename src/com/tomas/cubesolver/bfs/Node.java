package com.tomas.cubesolver.bfs;
/**
 * The object bfs work with. It contains the object that implements Search interface,
 * the parent, to remember the path information and g and h Value from BFS 
 */
public class Node implements Comparable<Node>{
	private Search search;
	private Node parent;
	private double gValue;
	private double hValue;
	
	public Node(Search sable, double hValue, Node parent){
		this.search = sable;
		this.hValue = hValue;
		this.gValue = sable.calcHeuristic();
		this.parent = parent;
	}

	/**
	 * Order based off F value, smallest first
	 */
	@Override
	public int compareTo(Node o) {
		return new Double(this.getFValue()).compareTo(o.getFValue());
	}	
	
	//getters
	public Search getSearch() {
		return search;
	}
	public double getGValue() {
		return gValue;
	}
	public double getHValue() {
		return hValue;
	}
	public double getFValue() {
		return gValue + hValue;
	}
	public Node getParent() {
		return parent;
	}
}

