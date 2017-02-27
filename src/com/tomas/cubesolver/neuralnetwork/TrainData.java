package com.tomas.cubesolver.neuralnetwork;

/**
 * Class representing one line of training data
 * that are generated and saved to file using
 * other classes
 */
public class TrainData {
	private String input;
	private String output;
	
	public TrainData(String in, String out){
		input = in;
		output = out;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
}
