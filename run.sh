#/bin/bash

command="java -cp bin:\
$HOME/Documents/libs/jogamp-all-platforms/jar/jogl-all.jar:\
$HOME/Documents/libs/jogamp-all-platforms/jar/gluegen-rt.jar \
ca.germuth.neural_network.Main"

rlwrap $command
