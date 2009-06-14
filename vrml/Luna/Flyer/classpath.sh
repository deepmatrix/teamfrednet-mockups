#!/bin/bash

wd=$(cd $(dirname $0); pwd)

find ${wd} -type f -name '*.jar' | tr '\n' ':' 
echo
