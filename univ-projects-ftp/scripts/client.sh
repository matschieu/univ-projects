#!/bin/bash
javac -d classes/ -sourcepath src/ src/ftp/client/cli/FTPClient.java && java -cp classes ftp.client.cli.FTPClient $1 $2
