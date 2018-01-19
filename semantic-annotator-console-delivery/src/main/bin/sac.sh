#!/usr/bin/env bash

CLASSPATH=".:lib/@batchJarName@.jar:@classpath@"
java -cp ${CLASSPATH} cle.nlp.annotator.console.Console