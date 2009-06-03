#!/bin/bash

if [ ! -d bin ]
then
 mkdir bin
 cp -p PicoRover.wrl bin
fi

javac -cp $(./classpath.sh) -d bin *.java

