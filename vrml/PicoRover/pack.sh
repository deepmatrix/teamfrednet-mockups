#!/bin/bash


if [ "$1" ]&&[ -d bin ]
then
 version="$1"
 targets=$(2>/dev/null ls PicoRover-*.jar )
 if [ -n "${targets}" ]
 then
  if ! rm -f ${targets}
  then
   cat<<EOF>&2
Error dropping stale targets. 
EOF
   exit 1
  fi
 fi
 cd bin
 jar cvmf ../mf.txt ../PicoRover-${version}.jar $(find . -type f | egrep -v '\.svn')
else
 cat<<EOF>&2
usage
  ./pack.sh version
example
  ./pack.sh 1.0.2
EOF
 exit 1
fi
