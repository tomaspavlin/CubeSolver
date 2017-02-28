# CubeSolver
The program CubeSolver is an example of neural network used for solving Rubik's Cube of generic size. Its features includes creating of neural network with any number of hidden layers of any number of input neurons, training the neural network using stochastic back propagation algorithm and it can also graphically dispalay the Rubik's Cube in order to test the the neural network.

## Building
On Unix systems, simply navigate to project directory and run:
```bash
ant compile
```

## Running
On Unix systes, run bash script *run.sh*:
```bash
./run.sh
```

## CLI
After the start of the program, the help with available commands is displayed. Now we will provide an example how to use the program. Read the section *Commands* section for all commands description.

### Example usage
If not changed, there is an default trained neural network that can solve 3x3x3 cube. We will use this network in this simple example.

First, let's load the default neural network first. Type:
```
load CUBE
```
This will load the default neural network that can solve the cube and opens graphical window displaying the cube.

Secondly type:
```
solve
```

  
