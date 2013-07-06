@echo off

echo Publishing ProcessRunner...
call "%~dp0sbt.bat" %* clean +publish
