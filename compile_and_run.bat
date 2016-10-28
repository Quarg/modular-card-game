@echo off
cd source/
javac *.java -d ../out/ -cp ../lib/**
cd ../out/
java TestInstance
cd ../