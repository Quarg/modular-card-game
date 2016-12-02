@echo off
cd %~p0source/
javac *.java -d ../out/ -cp ../lib/* 
cd ../out/
java -cp .;../lib/* -Djava.library.path=.;../natives/ TestInstance
java -cp .;../lib/* -Djava.library.path=.;../natives/ GameShell