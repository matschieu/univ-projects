
# Matschieu 
# L3 info - GR2
# PDC - BAM
# 2008

CC      = gcc
CFLAGS  = -Wall -ansi -pedantic
CFLAGS += -g -Wconversion

all: memory clean

memory: src/main.o src/bam.o
	$(CC) $(CFLAGS) -o $@ $^
libmalloc:
	$(CC) $(CFLAGS) -D_XOPEN_SOURCE=500 -g -fPIC -c src/malloc.c
	$(CC) $(CFLAGS) -D_XOPEN_SOURCE=500 -g -fPIC -c src/bam.c
	$(CC) -shared -Wl,-soname,libmalloc.so -o libmalloc.so malloc.o bam.o
	export LD_PRELOAD=./libmalloc.so

.PHONY: clean realclean
clean:
	$(RM) *.o
	$(RM) src/*.o
realclean : clean
	$(RM) memory libmalloc.so

docker:
	docker build -t memory-img . && docker run --rm -it memory-img /bin/bash
docker-clean:
	docker rmi memory-img
	docker system prune -f --volumes

