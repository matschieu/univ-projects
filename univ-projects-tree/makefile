
# MATSCHIEU
# 2008

CC      = gcc
CFLAGS  = -Wall -ansi -pedantic
CFLAGS += -g

tree: src/main.o src/tree.o src/bam.o
	$(CC) $(CFLAGS) -o $@ $^

.PHONY: clean realclean
clean:
	$(RM) src/*.o
realclean : clean
	$(RM) tree

