
CC      = gcc
CFLAGS  = -Wall -ansi -pedantic
CFLAGS += -g -I./src
LDFLAGS = -lm

all: uncompress clean

uncompress: src/decomp.o src/targa.o
	$(CC) $(CFLAGS) -o $@ $^ $(LDFLAGS)

.PHONY: clean realclean
clean:
	$(RM) *.o
	$(RM) src/*.o
realclean : clean
	$(RM) uncompress

DOCKER-IMG = 4t-reader-img

docker:
	docker build -t $(DOCKER-IMG) . && docker run --rm -it $(DOCKER-IMG) /bin/bash
docker-clean:
	docker rmi $(DOCKER-IMG)
	docker system prune -f --volumes
