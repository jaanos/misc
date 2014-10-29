#!/bin/bash
LP=".."
CP="racgr/labyrinth;.;$LP/res;$LIBPATH/jar/lwjgl.jar;$LP/jar/lwjgl_test.jar;$LP/jar/lwjgl_util.jar;$LP/jar/lwjgl_fmod3.jar;$LP/jar/lwjgl_devil.jar;$LP/jar/jinput.jar;"
LIBPATH="$LP/native/linux"
if [ "$1" == "-javadoc" ]; then
	javadoc -charset windows-1250 -author -version -private -classpath $CP -link http://java.sun.com/j2se/1.5.0/docs/api/ -link http://www.lwjgl.org/javadoc/ racgr.labyrinth racgr.labmaker
	exit
fi
if [ "$1" == "-compile" ]; then
	javac -cp $CP racgr/labyrinth/*.java racgr/labmaker/*.java
	if [ $? -eq 1 ]; then exit; fi
	shift
fi
if [ "$1" == "-edit" ]; then
	javaw racgr.labmaker.LabMaker $2
	exit
fi
java -cp $CP -Djava.library.path=$LIBPATH racgr.labyrinth.Viewer $1 $2 $3
