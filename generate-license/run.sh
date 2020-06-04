#!/usr/bin/env bash

mvn clean install
java -jar target/generate-license.jar
