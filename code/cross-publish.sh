#!/bin/bash

echo Publishing ProcessRunner...
`dirname $0`/sbt.sh "$@" clean +publish
