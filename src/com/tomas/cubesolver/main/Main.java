package com.tomas.cubesolver.main;

import java.awt.BorderLayout;
import java.util.*;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.tomas.cubesolver.bfs.BFSearcher;
import com.tomas.cubesolver.bfs.Node;
import com.tomas.cubesolver.cube.Cube;
import com.tomas.cubesolver.cube.CubePrinter;
import com.tomas.cubesolver.cube.XOR;
import com.tomas.cubesolver.graphics.CubeCanvas;
import com.tomas.cubesolver.neuralnetwork.BackPropagation;
import com.tomas.cubesolver.neuralnetwork.NN;
import com.tomas.cubesolver.neuralnetwork.TrainData;
import com.tomas.cubesolver.utils.FIleUtils;

import javax.swing.JFrame;

/**
 * This class is an entry point for the CubeSolver.
 * It defines CLI so that user can use other classes and packages of the application.
 */

public class Main {
	final static int PIXEL_WIDTH = 480;
	final static String CONF_FILENAME = "config.conf";
	static int NUM_INPUT;
	final static int NUM_OUTPUT = 6 * 2;
	
	
	private static Cube cube;
	private static NN nn;
	private static String nnType;
	
	
	private static int CUBE_SIZE;
	private static int TRAINDATA_MAX_MOVES;
	private static int TRAINDATA_COUNT_FOR_EACH;
	
	/**
	 * Load configuration from the conf file and controls users commands.
	 * @param args Program args
	 */
	public static void main(String[] args) {
		Conf.load(CONF_FILENAME);
		

		FIleUtils.TRAINING_DATA_FILE_NAME = Conf.s("TRAINING_DATA_FILE_NAME");
		FIleUtils.SAVED_NEURAL_NETWORK_FILE_NAME = Conf.s("SAVED_NEURAL_NETWORK_FILE_NAME");
		
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
					solveNeuralNetwork(s); break;
				case "save":
					System.out.println("Saving");
					if(nn != null){
						FIleUtils.writeNeuralNetwork(nn, (nn.getSolvable() instanceof XOR) ? "XOR": "CUBE");
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
					nn = FIleUtils.readNeuralNetwork(nnType); 
					
					if(nnType.equals("XOR")){
						nn.setSolveable(new XOR());
					}else if(nnType.equals("CUBE")){
						createCube(CUBE_SIZE);
						nn.setSolveable(cube);
					} else {
						printHelp();
						break;
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
	
	/**
	 * Create new neural network according to type and saves it to variable
	 * @param type one of CUBE or XOR
	 * @param scan input scanner
	 */
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
			nn = new NN(cube, NUM_INPUT, NUM_OUTPUT, sizes.length, sizes);
			nnType = "CUBE";
		}else{
			nn = new NN(new XOR(), 2, 1, sizes.length, sizes);
			nnType = "XOR";
		}
		System.out.println("Neural Network Created");
	}
	

	/**
	 * Generates training data and saves it into file
	 * @param type one of CUBE or XOR
	 */
	private static void trainData(String type) {
		ArrayList<TrainData> lines = new ArrayList<TrainData>();
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

					if (!visited.containsKey(state)) {
						visited.put(state, true);

						Node sn = BFSearcher.runSearch(experimentCube,
								new Cube(CUBE_SIZE));
						String moveToMake = getPath(sn).split(" ")[0];
						lines.add(new TrainData(state, moveToMake));
					}
				}
			}
		} else if (type.equals("XOR")) {
			lines.add(new TrainData("-1 -1","-1"));
			lines.add(new TrainData( "1 -1", "1"));
			lines.add(new TrainData("-1 1",  "1"));
			lines.add(new TrainData( "1 1", "-1"));
		}
		
		FIleUtils.writeTrainingData(type, lines);
		System.out.println("Done creating training data");
	}

	/**
	 * Trains many neural networks according to network type in nn variable and choose the best one.
	 * @param type One of CUBE and XOR
	 * @param LEARN_RATE Strength of back propagation iteration
	 * @param train_iter Number of iterations per one nn
	 * @param train_epoch Number of nn tested
	 */
	private static void trainNeuralNetwork(String type, double LEARN_RATE,
			int train_iter, int train_epoch){
		ArrayList<TrainData> training = FIleUtils.readTrainingData(type);
		ArrayList<TrainData> testing = FIleUtils.readTrainingData(type);

		double err = BackPropagation.runForMinimum(nn, training, testing, 
				LEARN_RATE, train_iter, train_epoch);
		
		System.out.println("Neural Network Trained Successfully with " + err + " error");

	}
	
	/**
	 * Trains current neural network so it would be better
	 * @param type One of CUBE and XOR
	 * @param LEARN_RATE Strength of back propagation iteration
	 * @param train_iter Number of iterations per one log output
	 * @param train_epoch Number of log outputs
	 */
	private static void trainOldNeuralNetwork(String type, double LEARN_RATE,
			int train_iter, int train_epoch){
		ArrayList<TrainData> training = FIleUtils.readTrainingData(type);
		ArrayList<TrainData> testing = FIleUtils.readTrainingData(type);

		double err = BackPropagation.runOld(nn, training, testing, 
				LEARN_RATE, train_iter, train_epoch);
		
		System.out.println("Neural Network Trained Successfully with " + err + " error");
	}
	
	/**
	 * Graphically test the nn network (for CUBE) or through CLI (for XOR)
	 * @param scan User input scanner
	 */
	private static void solveNeuralNetwork(Scanner scan){
		Random r = new Random();
		
		if(nn.getSolvable() instanceof Cube){
			int moves = r.nextInt(TRAINDATA_MAX_MOVES) + 1;
			cube.scrambleCube(moves);
			System.out.println("Cube scrumbled with " + moves + " moves.");
			
			while(!cube.isSolved()){
				CubePrinter.printCube(cube);
				
				String cubeState = cube.getKey().replaceAll(" |,|\\[|\\]", "");
				double[] moveArr = nn.feedForward( cube.mapTrainingInput(cubeState) );

				String move = cube.getMoveString(moveArr);
				String moveLong = cube.getMoveLongString(moveArr);
				
				System.out.println("Neural network recommends: \n" + moveLong);
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

				CubePrinter.printCube(cube);
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
	
	
	/** 
	 * Creates cube and initialize openGL window
	 * @param size Cube side size - e.g. 3 for 3x3x3 cube
	 */
	private static void createCube(int size){
		cube = new Cube(size);
		GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities( glprofile );
        CubeCanvas rd = new CubeCanvas(capabilities, PIXEL_WIDTH, PIXEL_WIDTH, cube);

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
	
	/**
	 * Takes path from BFSearch and creates string of moves
	 * @return String of moves
	 */
	public static String getPath(Node lastNode){
		ArrayList<String> moves = new ArrayList<String>();
		Node currNode = lastNode;
		while(currNode != null){
			String move = currNode.getSearch().getMoveTaken();
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
	
	/**
	 * Shows help to stdout
	 */
	private static void printHelp() {
		System.out.println("   ____      _          ____        _                 ");
		System.out.println("  / ___|   _| |__   ___/ ___|  ___ | |_   _____ _ __  ");
		System.out.println(" | |  | | | | '_ \\ / _ \\___ \\ / _ \\| \\ \\ / / _ \\ '__| ");
		System.out.println(" | |__| |_| | |_) |  __/___) | (_) | |\\ V /  __/ |    ");
		System.out.println("  \\____\\__,_|_.__/ \\___|____/ \\___/|_| \\_/ \\___|_|    ");
		System.out.println("                                                      ");
		System.out.println("Neural network basics commands:");
		System.out.println(" create (XOR|CUBE)      Create a new neural network");
		System.out.println(" load (XOR|CUBE)        Load an neural network from file");
		System.out.println(" save                   Save the current neural network into the file");
		System.out.println();
		
		System.out.println("Training neural network commands:");		
		System.out.println(" traindata (XOR|CUBE)   Generate training data");
		System.out.println(" trainbest              Train many neural networks and use the best");
		System.out.println(" train                  Train current neural network");
		System.out.println();

		System.out.println("Solving cube using the neural network command:");
		System.out.println(" solve                  Solve the cube using the neural network,");
		System.out.println("                        that must to be created or loaded already");
		System.out.println();
		
		System.out.println(" help                   Show this help");
		System.out.println(" quit                   Quit CubeSolver");
		System.out.println();
		System.out.println("What command you want to perform?");
	}
	
}