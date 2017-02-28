# CubeSolver
The program CubeSolver is an example of neural network used for solving Rubik's Cube of generic size programmed in Java. Its features includes creating of neural network with any number of hidden layers of any number of input neurons, training the neural network using stochastic back propagation algorithm and it can also test the trained neural network.

This document is user documentation of the program. For technical documentation, run **gen-javadoc.sh** and look into created **javadoc** directory.

## Compiling
On Unix systems, simply navigate to project directory and run:
```bash
> ant compile
```

The project can be also opened and compiled using Eclipse.

## Running
On Unix systems, run bash script *run.sh*:
```bash
> ./run.sh
```

## CLI
After the start of the program, the help with available commands is displayed. Now we will provide an example how to use the program. Read the section [Commands](#commands) section for all commands description.

### Example usage
If not changed, there is an default trained neural network that can solve 3x3x3 cube. We will use this network in this simple example.

First, let's load the default neural network first. Type:
```
load CUBE
```
This will load the default neural network that can solve the cube.

Secondly type:
```
solve
```
This command will shutter the cube using 1 to 5 moves (this number can be chaged, see [Configuration](#configuration) section) and display the cube configuration into console (see [Solve](#solve) section for more info). It computes the recommended move using the neural network and this move is performed by pressing **enter** (see [Solve](#solve) section for more options). This repeats infinitely unless the cube is not solved. Although the program solves the cube in most cases, it does not solve it in every case. By that reason, it is possible to get out of the [Solve](#solve) interface by typing **Q**.

Try to perform the *solve* command several times and then type **quit** to quit the program.

### Commands
More detailed documentation on the commands user can type after starting the program follows. 

#### create (XOR|CUBE)
Creates a new neural network that can solve either Rubik's cube or XOR operation.

Type *create XOR* for creating neural network for XOR operation and *create CUBE* for creating neural network that can solve Rubik's cube. Then you will be asked for choosing number of hidden neurons and hidden layers and the neural network will be created. This network will consist of random neuron weights and will not be trained yet.

#### load (XOR|CUBE)
Loads saved neural network from file. The file name can be configured in configuration file (see [Configuration](#configuration)). There is one default file for each of the XOR and CUBE type neural networks. Type one of *load XOR* or *load CUBE* to perform this command.

#### save
It checks the loaded or created neural network type (one of XOR or CUBE) and saves it to corresponding file which name can be configured (see [Configuration](#configuration)). The network has to be loaded or created first.

#### traindata (XOR|CUBE)
Generates file contating training data that are used later in *trainbest* and *train* commands. The train data for XOR are trivial and are generated automatically, the train data for cube are more complex and parameters can be changed in configuration file (see [Configuration](#configuration)).

#### trainbest
Creates and trains many neural network of the type as the loaded or creating network has (cube or xor, number of hidden neurons and layers) and chooses the best one. Parameters of the training can be configured (see Configuration). The training is accomplihed using stochastic back propagation.

#### train
This command is best to use with already trained network and it trains it more using stochastic back propagation algorithm. It can be also configured (see [Configuration](#configuration)).

#### solve
After succesfully training neural network for computing xor operation or solving the cube, it is time to test it using only this command.

If the neural network is for computing xor operation, it asks for input and prints computed output. If the network is for solving cube, the behaviour is following.

It scruble new plain Rubik's cube with random number of moves (the highest number can be configured, see [Configuration](#configuration)), and displays the cube configuration to console.

The 3D cube is represented using it's 2D network. An example of 3x3x3 cube representation follows:
```
          R G G         
          B B R         
          O O O         

  W O G   W W W   B Y O 
  B O G   W W W   B R O 
  B O G   W W G   R R Y 

          R R Y         
          G G G         
          O B B         

          Y R R         
          Y Y Y         
          B Y Y 
```

The letters corresponds to the stickers colors (red, green, blue, orange, yellow and white) and they group into the 6 cube edges as:
```
     back
left  up  right
     front
     down
```

There are 6 faces on the cube, each face can be rotated in 2 directions so it makes 12 movements we can do in every solving step. Although the cube size can be configured, the number of these movements is most reasonable for 3x3, maybe 2x2 cubes. These 12 movements are noted using the letter characterizing cube face (R,L,U,D,F,B) and sufixed with apostroph for counter clock wise movements. It means we have these movements: R, L, U, D, F, B, R', L', U', D', F' and B'. This notation is commonly used in Rubik's cube solving community.

The neural network express confidence using each movement in percents when the best confidence has most percents. These percents are displayed and the movement with the biggest confidence is reccomended to user. The user can then apply this recommendation just by pressing **enter** or choosing different movement by **typing its name**.

The same procedure as described is repeaded util the cube is solved or until the solve command is quited by typing **Q**.

#### help
Displays help.

#### quit
Exits program.

## Configuration
The commands above can be configured in the file **config.conf**. The file is well commented.
