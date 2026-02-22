FROM ubuntu:24.04

LABEL maintainer=Matschieu

WORKDIR /home/www

COPY ./src src
COPY ./makefile makefile

ENV DEBIAN_FRONTEND=noninteractive
ENV TZ=Europe/Paris

RUN dpkg --add-architecture i386
RUN apt update
RUN apt install -y gcc-multilib
RUN apt install -y make

RUN make 
RUN ./memory || [ $? -eq 2 ]

CMD ["/bin/bash"]
