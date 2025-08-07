
# MATSCHIEU
# 2008

CC      = gcc
CFLAGS  = -Wall -ansi -pedantic
CFLAGS += -g

tree: main.o tree.o bam.o
	$(CC) $(CFLAGS) -o $@ $^

.PHONY: clean realclean
clean:
	$(RM) *.o
realclean : clean
	$(RM) tree

