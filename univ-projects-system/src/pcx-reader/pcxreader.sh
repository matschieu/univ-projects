#!/bin/bash

xhost +local:docker
docker build -t pcxreader-img . && docker run --rm -it -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix -v /dev/dri:/dev/dri pcxreader-img /bin/bash
xhost -local:docker
docker rmi pcxreader-img

