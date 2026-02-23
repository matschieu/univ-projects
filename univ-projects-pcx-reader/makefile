CC      = gcc
CFLAGS  = -Wall -Werror -ansi -pedantic
CFLAGS  += -m32
LDFLAGS = -lSDL `sdl-config --cflags –libs`

all: pcxreader clean

pcxreader: objects
	$(CC) $(CFLAGS) ./lib/display.o *.o $(LDFLAGS) -o pcxreader
objects:
	$(CC) $(CFLAGS) -I./lib -I./src -c src/pcxr.c src/effets.c

.PHONY: clean cleanall

clean:
	$(RM) *.o
cleanall : clean
	$(RM) pcxreader

DOCKER-IMG = 4t-reader-img

docker:
	xhost +local:docker
	docker build -t $(DOCKER-IMG) . && docker run --rm -it -e DISPLAY=$$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix -v /dev/dri:/dev/dri $(DOCKER-IMG) /bin/bash
	xhost -local:docker
docker-clean:
	docker rmi $(DOCKER-IMG)
	docker system prune -f --volumes
