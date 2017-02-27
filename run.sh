#/bin/bash

command="java -cp bin:\
lib/jogamp-all-platforms/jar/jogl-all.jar:\
lib/jogamp-all-platforms/jar/gluegen-rt.jar \
com.tomas.cubesolver.main.Main"

rlwrap $command
