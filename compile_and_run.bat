@echo off
cd %~p0source/

dir /s /B *.java > ../sources.txt

javac @../sources.txt -d ../out/ -cp ../lib/*

cd ../out/
::java -cp .;../lib/* -Djava.library.path=.;../natives/ TestInstance
::java -cp .;../lib/* -Djava.library.path=.;../natives/ GameShell