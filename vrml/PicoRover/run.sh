#!/bin/bash

export LD_LIBRARY_PATH=$(pwd)/lib/$(uname -p)

java -cp $(./classpath.sh) org.teamfrednet.mockups.PicoRover



