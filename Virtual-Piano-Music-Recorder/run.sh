#!/bin/sh
# Compiles and runs the Virtual Piano on Linux / macOS
cd "$(dirname "$0")"
javac -d out -sourcepath src src/piano/Main.java && java -cp out piano.Main
