package com.tomas.cubesolver.cube;

import com.tomas.cubesolver.neuralnetwork.Solve;
/**
 * This implements XOR operation and it is for testing purposes on nn
 */
public class XOR implements Solve{
	@Override
	public double[] mapTrainingInput(String trainInput) {
		String[] inputs = trainInput.split(" ");
		double[] in = new double[inputs.length];
		for(int i = 0; i < inputs.length; i++){
			in[i] = Double.parseDouble(inputs[i]);
		}
		return in;
	}

	@Override
	public double[] mapTrainingOutput(String trainOutput) {
		double[] out = new double[1];
		out[0] = Double.parseDouble(trainOutput);
		return out;
	}
}
