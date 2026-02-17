# Needs libx11-dev to compile ; 
# sudo apt install libx11-dev

CC      = gcc
CFLAGS  = -Wall -Werror -ansi -pedantic
LDFLAGS = -lX11

all: tga clean

# $(LDFLAGS) needs to be after the .o files else it will fail on linkage
tga: main.o tga.o analyze.o window.o
	$(CC) main.o tga.o analyze.o window.o $(LDFLAGS) -o tga
analyze.o: analyze/analyze.o
	mv analyze/*.o .
window.o: window/window.o
	mv window/*.o .

.PHONY: clean realclean

clean:
	$(RM) *.o
realclean : clean
	$(RM) tga
