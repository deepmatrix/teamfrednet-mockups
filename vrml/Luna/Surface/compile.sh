#!/bin/bash

if [ ! -d bin ]
then
 mkdir bin
 cp -p *.wrl *.jpg *.gif bin
fi

javac -cp $(./classpath.sh) -d bin *.java

