FROM ubuntu:24.04

LABEL maintainer=Matschieu

WORKDIR /home/www

COPY ./include include
COPY ./sim sim
COPY ./src src
COPY ./makefile makefile
COPY ./autotest.sh autotest.sh
COPY ./hardware.ini hardware.ini
COPY ./vdiskA.bin vdiskA.bin
COPY ./vdiskB.bin vdiskB.bin

ENV DEBIAN_FRONTEND=noninteractive
ENV TZ=Europe/Paris

RUN apt update
RUN apt install -y gcc-multilib
RUN apt install -y make

RUN chmod +x autotest.sh

RUN make 
RUN ./autotest.sh

CMD ["/bin/bash"]
