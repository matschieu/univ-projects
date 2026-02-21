
# Matschieu 
# L3 info - GR2
# PDC - BAM
# 2008

CC      = gcc
CFLAGS  = -Wall -ansi -pedantic
CFLAGS += -g

memory: src/main.o src/bam.o
	$(CC) $(CFLAGS) -o $@ $^
libmalloc:
	$(CC) $(CFLAGS) -Werror -D_XOPEN_SOURCE=500 -g -c src/malloc.c
	$(CC) $(CFLAGS) -Werror -D_XOPEN_SOURCE=500 -g -c src/bam.c
	$(CC) -shared -Wl,-soname,libmalloc.so -o libmalloc.so src/malloc.o src/bam.o
	export LD_PRELOAD=./libmalloc.so

.PHONY: clean realclean
clean:
	$(RM) src/*.o
realclean : clean
	$(RM) memory libmalloc.so

