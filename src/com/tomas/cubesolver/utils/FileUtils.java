package com.tomas.cubesolver.utils;

import java.io.*;
import java.util.*;

import com.tomas.cubesolver.neuralnetwork.NN;
import com.tomas.cubesolver.neuralnetwork.Solve;
import com.tomas.cubesolver.neuralnetwork.TrainData;

/**
 * Handles writing and reading from files
 */
public class FileUtils {
	public static String TRAINING_DATA_FILE_NAME = "noimplemented.csv";
	public static String SAVED_NEURAL_NETWORK_FILE_NAME = "noimplemented.csv-nn.csv";
	
	/**
	 * Writes training data to file
	 * @param type Data type (CUBE or XOR) used for new filename
	 * @param tobeWritten Training data
	 */
	public static void writeTrainingData(String type, ArrayList<TrainData> tobeWritten){
		File f = null;
		PrintWriter pw = null;
		try {
			f = new File(type + TRAINING_DATA_FILE_NAME);
			f.createNewFile();
			pw = new PrintWriter(f);
			for(TrainData d: tobeWritten){
				pw.println(d.getInput() + "," + d.getOutput());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.flush();
		pw.close();
	}
	
	/**
	 * Reads training data from file
	 * @param type Data type (CUBE or XOR) used for the filename
	 * @return Training data
	 */
	public static ArrayList<TrainData> readTrainingData(String type){
		ArrayList<TrainData> data = new ArrayList<TrainData>();
		File f = new File(type + TRAINING_DATA_FILE_NAME);
		try {
			Scanner s = new Scanner(f);
			while(s.hasNextLine()){
				String line = s.nextLine();
				String[] split = line.split(",");
				if(split.length == 2){
					data.add(new TrainData(split[0], split[1]));
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * Writes nn to file
	 * @param nn Neural network
	 * @param type CUBE or XOR used for filename
	 */
	public static void writeNeuralNetwork(NN nn, String type){
		File f = null;
		PrintWriter pw = null;
		try {
			f = new File(type + SAVED_NEURAL_NETWORK_FILE_NAME);
			f.createNewFile();
			pw = new PrintWriter(f);
			
			ArrayList<String> lines = nn.toFile();
			for(String s: lines){
				pw.println(s);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.flush();
		pw.close();
	}
	
	/**
	 * Reads nn from file
	 * @param type CUBE or XOR used for filename
	 * @return Neural network
	 */
	public static NN readNeuralNetwork(String type){
		NN nn = null;
		Solve solv = null;
		File f = new File(type + SAVED_NEURAL_NETWORK_FILE_NAME);
		try {
			Scanner s = new Scanner(f);
			//skip "neural network"
			s.nextLine(); 
			
			//get layer sizes
			String[] sizes = s.nextLine().split(",");
			ArrayList<Integer> size = new ArrayList<Integer>();
			for(String str: sizes){
				size.add(Integer.parseInt(str));
			}
			
			//get edge weights
			String[] edge= s.nextLine().split(",");
			ArrayList<Double> weights = new ArrayList<Double>();
			for(String str: edge){
				weights.add( Double.parseDouble(str) );
			}
			nn = new NN(solv, size, weights);
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return nn;
	}
}
