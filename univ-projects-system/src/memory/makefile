
# Matschieu 
# L3 info - GR2
# PDC - BAM
# 2008

CC      = gcc
CFLAGS  = -Wall -ansi -pedantic
CFLAGS += -g

main: main.o bam.o
	$(CC) $(CFLAGS) -o $@ $^
libmalloc:
	$(CC) $(CFLAGS) -Werror -D_XOPEN_SOURCE=500 -g -c malloc.c
	$(CC) $(CFLAGS) -Werror -D_XOPEN_SOURCE=500 -g -c bam.c
	$(CC) -shared -Wl,-soname,libmalloc.so -o libmalloc.so malloc.o bam.o
	export LD_PRELOAD=./libmalloc.so

.PHONY: clean realclean
clean:
	$(RM) *.o
realclean : clean
	$(RM) main libmalloc.so

