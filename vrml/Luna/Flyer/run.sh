#!/bin/bash

wd=$(cd $(dirname $0); pwd)
name=$(uname -s)
mach=$(uname -p)
if [ 'Darwin' = "${name}" ]
then
 mach='uni'
fi
lib_path=${wd}/lib/${name}/${mach}
if [ -d "${lib_path}" ]
then
 echo "Library directory '${lib_path}'."
 java -Djava.library.path=${lib_path} -cp $(./classpath.sh) de.jreality.vr.ViewerVR ${wd}/Luna.wrl
else
 echo "Library directory not found '${lib_path}'."
 exit 1
fi
