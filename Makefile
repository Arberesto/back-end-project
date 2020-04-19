SHELL := /bin/bash

all: start_jar
start_jar: 
	java -jar ./target/taskmanager-1.0-SNAPSHOT.jar
