package com.tomas.neuralNetwork;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.tomas.neuralNetwork.evolution.Evolvable;
import com.tomas.neuralNetwork.evolution.EvolvableNeuralNetwork;
import com.tomas.neuralNetwork.evolution.GeneticAlgorithm;
import com.tomas.neuralNetwork.gui.MyGLCanvas;
import com.tomas.neuralNetwork.search.SearchFacade;
import com.tomas.neuralNetwork.search.SearchNode;
import com.tomas.neuralNetwork.search.SearchType;
import com.tomas.neuralNetwork.solvable.XOR;
import com.tomas.neuralNetwork.solvable.cube.Cube;
import com.tomas.neuralNetwork.trainable.NeuralNetwork;
import com.tomas.neuralNetwork.trainable.StochasticBackPropagation;
import com.tomas.neuralNetwork.trainable.TrainingData;

import javax.swing.JFrame;

/**
 * CPSC 371 Project Phase 3
 * Main.java
 * 
 * April 17th, 2015 
 * @author Aaron Germuth
 */

//TODO
//implement phase1 and phase2 marks
//large number of epochs like 1000 for optimal
//over a million iterations
//less hidden neurons, 30 prob too much
//higher learning rate 0.1
//implement momentum, weight decay, intialization (easiest one)

//Phase 3
//run genetic algorithm on population of 2 2 1 networks
	//implement probability of selection
	//
//implement other things
//start to run experiments the smart way
//write document
//hand in

//TODO
//delete these comments
//adjust numbers in Genetic Algorithm
public class Main {
	final static int PIXEL_WIDTH = 480;
	final static String CONF_FILENAME = "config.conf";
	static int NUM_INPUT;
	final static int NUM_OUTPUT = 6 * 2;
	
	
	private static Cube cube;
	private static NeuralNetwork nn;
	private static String nnType;
	
	
	private static int CUBE_SIZE;
	private static int TRAINDATA_MAX_MOVES;
	private static int TRAINDATA_COUNT_FOR_EACH;
	
	public static void main(String[] args) {
		Conf.load(CONF_FILENAME);
		
		
		GeneticAlgorithm.setPOPULATION_SIZE(Conf.i("POPULATION_SIZE"));
		GeneticAlgorithm.setITERATIONS(Conf.i("ITERATIONS"));
		GeneticAlgorithm.setPOPULATION_ELITE_PERCENT(Conf.f("POPULATION_ELITE_PERCENT"));
		GeneticAlgorithm.setPOPULATION_MUTATION_PERCENT(Conf.f("POPULATION_MUTATION_PERCENT"));
		GeneticAlgorithm.setPOPULATION_CROSSOVER_PERCENT(Conf.f("POPULATION_CROSSOVER_PERCENT"));

		FileHandler.TRAINING_DATA_FILE_NAME = Conf.s("TRAINING_DATA_FILE_NAME");
		FileHandler.SAVED_NEURAL_NETWORK_FILE_NAME = Conf.s("SAVED_NEURAL_NETWORK_FILE_NAME");
		
		TRAINDATA_MAX_MOVES = Conf.i("TRAINDATA_MAX_MOVES");
		TRAINDATA_COUNT_FOR_EACH = Conf.i("TRAINDATA_COUNT_FOR_EACH");
		
		CUBE_SIZE = Conf.i("CUBE_SIZE");
		NUM_INPUT = 6 * 6 * CUBE_SIZE * CUBE_SIZE;
		
		double learn_rate = Conf.f("LEARNING_RATE");
		int train_iter = Conf.i("TRAINING_ITERATIONS");
		int train_epoch= Conf.i("TRAINING_EPOCHS");
		

		printHelp();
		
		
		String type;
		
		Scanner s = new Scanner(System.in);
		Scanner lineScanner;
		
		while(s.hasNextLine()){	
			
			
			lineScanner = new Scanner(s.nextLine().trim());
			
			if(lineScanner.hasNext())
			switch(lineScanner.next().toLowerCase()){
				case "create":
					if(lineScanner.hasNext())
						createNeuralNetwork(lineScanner.next(), s);
					else
						printHelp();
					break;
				case "traindata":
					if(lineScanner.hasNext())
						trainData(lineScanner.next());
					else
						printHelp();
					break;
				case "trainmore":
					if(nn == null){
						System.out.println("You have not created or loaded a neural network yet");
						break;
					}
					type = nn.getSolvable() instanceof XOR ? "XOR": "CUBE";
					
					trainNeuralNetwork(type, learn_rate, train_iter, train_epoch);
					break;
				case "train":
					if(nn == null){
						System.out.println("You have not created or loaded a neural network");
						break;
					}
					type = nn.getSolvable() instanceof XOR ? "XOR": "CUBE";

					trainOldNeuralNetwork(type, learn_rate, train_iter, train_epoch);
					break;
				case "solve":
					if(nn == null){
						System.out.println("You have not created or loaded a neural network yet");
						break;
					}
					testNeuralNetwork(s); break;
				case "save":
					System.out.println("Saving");
					if(nn != null){
						FileHandler.writeNeuralNetwork(nn, (nn.getSolvable() instanceof XOR) ? "XOR": "CUBE");
					}else{
						System.out.println("No current neural network");
					}
					System.out.println("Saved succesfully");
					break;
				case "load":
					if(!lineScanner.hasNext()){
						printHelp();
						break;
					}
					
					nnType = lineScanner.next();
					nn = FileHandler.readNeuralNetwork(nnType); 
					
					if(nnType.equals("XOR")){
						nn.setSolveable(new XOR());
					}else{
						createCube(CUBE_SIZE);
						nn.setSolveable(cube);
					}
					System.out.println("Loaded successfully");
					break;
				case "quit":
					System.exit(0); break;

				case "help":
					printHelp();
					break;
				
				default:
					printHelp();
					break;
				
			}
			

		}
		
		s.close();
		System.exit(0);
	}
	
	private static void createNeuralNetwork(String type, Scanner scan){
		
		System.out.println("Please Enter Hidden Layer Config: ('12 12' makes two layers of size 12)");
		System.out.println("Recommend 36 for Cube and 2 for XOR");
		String[] strin = scan.nextLine().split(" ");
		int[] sizes = new int[strin.length];
		for(int i = 0; i < strin.length; i++){
			sizes[i] = Integer.parseInt(strin[i]);
		}
		if(type.equals("CUBE")){
			createCube(CUBE_SIZE);
			nn = new NeuralNetwork(cube, NUM_INPUT, NUM_OUTPUT, sizes.length, sizes);
			nnType = "CUBE";
		}else{
			nn = new NeuralNetwork(new XOR(), 2, 1, sizes.length, sizes);
			nnType = "XOR";
		}
		System.out.println("Neural Network Created");
	}
	

	
	// generates training data and save it to file
	// TODO filename
	private static void trainData(String type) {
		ArrayList<TrainingData> lines = new ArrayList<TrainingData>();
		if (type.equals("CUBE")) {
			HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
			for (int k = 1; k <= TRAINDATA_MAX_MOVES; k++) {
				for (int i = 0; i < TRAINDATA_COUNT_FOR_EACH; i++) {

					System.out.printf("Training cube (%d/%d, %d/%d)\n", k, TRAINDATA_MAX_MOVES, i + 1, TRAINDATA_COUNT_FOR_EACH);
					Cube experimentCube = new Cube(CUBE_SIZE);
					experimentCube.scrambleCube(k);
					if (experimentCube.isSolved()) {
						continue;
					}
					// replace unnecessary characters: ' ' ',' '[' ']
					String state = experimentCube.getKey().replaceAll(" |,|\\[|\\]", "");

					// unique key->value pairs
					if (!visited.containsKey(state)) {
						visited.put(state, true);

						SearchNode sn = SearchFacade.runSearch(SearchType.ASTAR, experimentCube,
								new Cube(CUBE_SIZE));
						String moveToMake = getPath(sn).split(" ")[0];
						lines.add(new TrainingData(state, moveToMake));
					}
				}
			}
		} else if (type.equals("XOR")) {
			lines.add(new TrainingData("-1 -1","-1"));
			lines.add(new TrainingData( "1 -1", "1"));
			lines.add(new TrainingData("-1 1",  "1"));
			lines.add(new TrainingData( "1 1", "-1"));
		}
		
		FileHandler.writeTrainingData(type, lines);
		System.out.println("Done creating training data");
	}

	private static void trainNeuralNetwork(String type, double LEARN_RATE,
			int train_iter, int train_epoch){
		ArrayList<TrainingData> training = FileHandler.readTrainingData(type);
		ArrayList<TrainingData> testing = FileHandler.readTrainingData(type);

		double err = StochasticBackPropagation.runForMinimum(nn, training, testing, 
				LEARN_RATE, train_iter, train_epoch);
		
		System.out.println("Neural Network Trained Successfully with " + err + " error");

	}
	
	private static void trainOldNeuralNetwork(String type, double LEARN_RATE,
			int train_iter, int train_epoch){
		ArrayList<TrainingData> training = FileHandler.readTrainingData(type);
		ArrayList<TrainingData> testing = FileHandler.readTrainingData(type);

		double err = StochasticBackPropagation.runOld(nn, training, testing, 
				LEARN_RATE, train_iter, train_epoch);
		
		System.out.println("Neural Network Trained Successfully with " + err + " error");
	}
	
	private static void testNeuralNetwork(Scanner scan){
		Random r = new Random();
		
		if(nn.getSolvable() instanceof Cube){
			int moves = r.nextInt(TRAINDATA_MAX_MOVES) + 1;
			cube.scrambleCube(moves);
			System.out.println("Cube scrumbled with " + moves + " moves.");
			
			while(!cube.isSolved()){
				String cubeState = cube.getKey().replaceAll(" |,|\\[|\\]", "");
				double[] moveArr = nn.feedForward( cube.mapTrainingInput(cubeState) );

				String move = cube.getMoveString(moveArr);
				String moveLong = cube.getMoveLongString(moveArr);
				
				System.out.println("Neural Network Says: \n" + moveLong);
				System.out.println("What move to perform (" + move + "). Type Q to exit.");
				
				String resp = scan.nextLine().trim().toUpperCase();
				if(resp.equals(""))
					resp = move;
				
				if(resp.equals("Q")){
					break;
				} else{
					cube.turn(resp);						
				}
			}
			
			if(cube.isSolved()){
				System.out.println("Solved!!!");
			}
		}else{
			System.out.println("Enter an input (for ex. -1 1)");
			double[] input = new double[2];
			input[0] = scan.nextDouble();
			input[1] = scan.nextDouble();
			double[] move = nn.feedForward(input);
			System.out.println("Neural Network Says " + move[0]);
		}
		
		if(cube != null){
			cube.setSolved();
		}
	}
	
	//Create cube and initialize openGL window
	private static void createCube(int size){
		cube = new Cube(size);
		GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities( glprofile );
        MyGLCanvas rd = new MyGLCanvas(capabilities, PIXEL_WIDTH, PIXEL_WIDTH, cube);

        final JFrame jframe = new JFrame( "Rubik's Cube Simulator" ); 
        jframe.setResizable(false);
        jframe.getContentPane().add( rd, BorderLayout.CENTER);
        jframe.setSize( PIXEL_WIDTH, PIXEL_WIDTH );
        jframe.setVisible( true );
        java.awt.EventQueue.invokeLater(new Runnable(){
        	@Override
        	public void run(){
        		jframe.toFront();
        		jframe.repaint();
        	}
        });
	}
	
	//Takes path from AStarSearch and creates string of moves
	public static String getPath(SearchNode lastNode){
		ArrayList<String> moves = new ArrayList<String>();
		SearchNode currNode = lastNode;
		while(currNode != null){
			String move = currNode.getSearchable().getMoveTaken();
			//initial state will have no move taken
			if(move != null){
				moves.add(move.replace("1", ""));
			}
			currNode = currNode.getParent();
		}
		StringBuilder sb = new StringBuilder();
		for(int i = moves.size() - 1; i >= 0; i--){
			sb.append(moves.get(i));
			sb.append(" ");
		}
		return sb.toString();
	}
	
	
	private static void printHelp() {
		System.out.println("   ____      _          ____        _                 ");
		System.out.println("  / ___|   _| |__   ___/ ___|  ___ | |_   _____ _ __  ");
		System.out.println(" | |  | | | | '_ \\ / _ \\___ \\ / _ \\| \\ \\ / / _ \\ '__| ");
		System.out.println(" | |__| |_| | |_) |  __/___) | (_) | |\\ V /  __/ |    ");
		System.out.println("  \\____\\__,_|_.__/ \\___|____/ \\___/|_| \\_/ \\___|_|    ");
		System.out.println("                                                      ");
		System.out.println("Enter 'traindata (XOR|CUBE)'	to create training data");
		System.out.println("Enter 'create (XOR|CUBE)'		to create a neural network");

		System.out.println("Enter 'load (XOR|CUBE)'		to load an neural network from file");
		System.out.println("Enter 'save'			to save current neural network to XORNeuralNetwork.csv");
		
		System.out.println("Enter 'trainbest'			to train many neural networks and use best");
		System.out.println("Enter 'train'			to train current neural network");
		System.out.println("Enter 'solve'			to test the current neural network");
		System.out.println("Enter 'help' 			to receive this help");
		System.out.println("Enter 'quit' 			to quit the program");
	}
	
}