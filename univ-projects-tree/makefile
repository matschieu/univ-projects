
# MATSCHIEU
# 2008

CC      = gcc
CFLAGS  = -Wall -ansi -pedantic
CFLAGS += -g

all: tree clean

tree: src/main.o src/tree.o
	$(CC) $(CFLAGS) -o $@ $^

.PHONY: clean realclean
clean:
	$(RM) *.o
	$(RM) src/*.o
realclean : clean
	$(RM) tree

DOCKER-IMG = tree-img

docker:
	docker build -t $(DOCKER-IMG) . && docker run --rm -it $(DOCKER-IMG) /bin/bash
docker-clean:
	docker rmi $(DOCKER-IMG)
	docker system prune -f --volumes
