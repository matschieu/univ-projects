# Needs libx11-dev to compile ; 
# sudo apt install libx11-dev

CC      = gcc
CFLAGS  = -Wall -Werror -ansi -pedantic
LDFLAGS = -lX11

all: tgareader clean

# $(LDFLAGS) needs to be after the .o files else it will fail on linkage
tgareader: src/main.o src/tga.o src/analyze/analyze.o src/window/window.o
	$(CC) src/main.o src/tga.o src/analyze/analyze.o src/window/window.o $(LDFLAGS) -o tga

.PHONY: clean realclean

clean:
	$(RM) src/*.o
	$(RM) src/*/*.o
realclean : clean
	$(RM) tga

DOCKER-IMG = tga-reader-img

docker:
	xhost +local:docker
	docker build -t $(DOCKER-IMG) . && docker run --rm -it -e DISPLAY=$$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix -v /dev/dri:/dev/dri $(DOCKER-IMG) /bin/bash
	xhost -local:docker	
docker-clean:
	docker rmi $(DOCKER-IMG)
	docker system prune -f --volumes
