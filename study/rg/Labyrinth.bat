@echo off
set lp=..
rem set lp=C:\lwjgl
set cp=racgr\labyrinth;.;%lp%\res;%lp%\jar\lwjgl.jar;%lp%\jar\lwjgl_test.jar;%lp%\jar\lwjgl_util.jar;%lp%\jar\lwjgl_fmod3.jar;%lp%\jar\lwjgl_devil.jar;%lp%\jar\jinput.jar;
set libpath=%lp%\native\win32
if "%1" == "-javadoc" (
	javadoc -charset windows-1250 -author -version -private -classpath %cp% -link http://java.sun.com/j2se/1.5.0/docs/api/ -link http://www.lwjgl.org/javadoc/ racgr.labyrinth racgr.labmaker
	goto end
)
if "%1" == "-compile" (
	javac -cp %cp% racgr\labyrinth\*.java racgr\labmaker\*.java
	if errorlevel 1 goto end
	shift
)
if "%1" == "-edit" (
	javaw racgr.labmaker.LabMaker %2
	goto end
)
java -cp %cp% -Djava.library.path=%libpath% racgr.labyrinth.Viewer %1 %2 %3
:end