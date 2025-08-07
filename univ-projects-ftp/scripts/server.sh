#!/bin/bash
javac -d classes/ -sourcepath src/ src/ftp/server/FTPServer.java && java -cp classes/ ftp.server.FTPServer $1 $2

