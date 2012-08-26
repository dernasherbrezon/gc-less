#!/bin/bash

mvn install:install-file -Dfile=./target/gcless-1.0.jar -DpomFile=./pom.xml -DlocalRepositoryPath=../gcless-maven/ -DcreateChecksum=true -Dsources=./target/gcless-1.0-sources.jar
