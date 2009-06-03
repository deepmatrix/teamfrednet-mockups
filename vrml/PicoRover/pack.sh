#!/bin/bash

if [ "$1" ]
then
 version="$1"
 jar cvmf mf.txt PicoRover-${version}.jar PicoRover.* PicoRoverApplet.* 
else
 cat<<EOF
usage
  ./pack.sh version
example
  ./pack.sh 1.0.2
EOF
 exit 1
fi
