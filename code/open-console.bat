@echo off

echo Firing up the Scala REPL ...
call "%~dp0\sbt.bat" %* console
