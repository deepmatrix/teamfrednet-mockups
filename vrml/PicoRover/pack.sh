#!/bin/bash

if [ "$1" ]&&[ -d bin ]
then
 version="$1"
 cd bin
 jar cvmf ../mf.txt ../PicoRover-${version}.jar $(find . -type f | egrep -v '\.svn')
else
 cat<<EOF
usage
  ./pack.sh version
example
  ./pack.sh 1.0.2
EOF
 exit 1
fi
