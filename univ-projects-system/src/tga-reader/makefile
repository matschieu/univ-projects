
# Matschieu

CC      = gcc
CFLAGS  = -Wall -Werror -ansi -pedantic -m32
LDFLAGS = -lX11

all: tga clean

tga: main.o tga.o analyze.o window.o
analyze.o: analyze/analyze.o
	mv analyze/*.o .
window.o: window/window.o
	mv window/*.o .

.PHONY: clean realclean

clean:
	$(RM) *.o
realclean : clean
	$(RM) tga
