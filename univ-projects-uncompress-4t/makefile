
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
