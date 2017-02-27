package com.tomas.cubesolver.neuralnetwork;
/**
 * The Solve object is one that can be mapped to a input and output, such as 
 * the input and output neurons of a neural network. For example the Cube 
 * or XOR can both be mapped.
 */
public interface Solve {
	public double[] mapTrainingInput(String trainInput);
	public double[] mapTrainingOutput(String trainOutput);
}
