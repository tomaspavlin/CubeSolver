#/bin/bash

command="java -cp bin:\
$HOME/Documents/libs/jogamp-all-platforms/jar/jogl-all.jar:\
$HOME/Documents/libs/jogamp-all-platforms/jar/gluegen-rt.jar \
com.tomas.neuralNetwork.Main"

rlwrap $command
