@echo off

set WIN_CP=.;lib\@batchJarName@.jar;@classpath@
java -cp %WIN_CP% cle.nlp.annotator.console.Console
