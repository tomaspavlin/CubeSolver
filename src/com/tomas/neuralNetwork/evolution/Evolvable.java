package com.tomas.neuralNetwork.evolution;

import java.util.ArrayList;

import com.tomas.neuralNetwork.trainable.TrainingData;

/**
 * Interface to allow object to be evolved by the genetic algorithm
 * @author Aaron
 *
 */
public interface Evolvable {
	
	public int getFitnessRank();
	public void setFitnessRank(int rank);
	public int getDiversityRank();
	public void setDiversityRank(int rank);
	
	//returns a pre-calculted fitness
	public double getFitness();
	public void calcFitness(ArrayList<TrainingData> training);
	//Convert object to double array
	public Double[] getGenome();
	//Convert back to object from double array
	public void toNetwork();
	//create copy of this evolvable
	public Evolvable copy();
}
