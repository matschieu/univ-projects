FROM ubuntu:24.04

LABEL maintainer=Matschieu

WORKDIR /home/www

COPY ./pcx pcx
COPY ./lib lib
COPY ./src src
#COPY ./src/pcxr.h src/pcxr.h
#COPY ./src/pcxr.c pcxr.c
#COPY ./src/effets.c effets.c
COPY ./makefile makefile

ENV DEBIAN_FRONTEND=noninteractive
ENV TZ=Europe/Paris

RUN dpkg --add-architecture i386
RUN apt update
RUN apt install -y gcc-multilib
RUN apt install -y --no-install-recommends libsdl1.2-dev:i386
RUN apt install -y make

#RUN gcc -m32 -Ilib/ -c effets.c pcxr.c
#RUN gcc -m32 lib/*.o *.o -o pcxreader -lSDL `sdl-config --cflags –libs`
RUN make 

CMD ["/bin/bash"]
