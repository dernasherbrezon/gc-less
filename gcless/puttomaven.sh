#!/bin/bash

/usr/share/maven3/bin/mvn install:install-file -Dfile=./target/gcless-4.0.jar -DpomFile=./pom.xml -DlocalRepositoryPath=../gcless-maven/ -DcreateChecksum=true -Dsources=./target/gcless-4.0-sources.jar
