
CC      = gcc
CFLAGS  = -Wall -Werror -ansi -pedantic
CFLAGS += -std=iso9899:1999
CFLAGS += -g

#ROOTDIR	=/home/enseign/ASE
ROOTDIR	=../sim
LIBDIR  = $(ROOTDIR)/lib
INCDIR  = -I$(ROOTDIR)/include -I./include
LIBS    = -L$(LIBDIR) -lhardware
SRCDIR	= src
EXEC    = mkhd frmt dmps vm mknfs dfs test_bloc
OTHS	= drive mbr vol super mtools inode
OBJS	= $(addsuffix .o, $(OTHS))

EXEC	+= if_pfile if_nfile if_cfile if_dfile
OBJS	+= $(addsuffix .o, tools ifile mount)

all: $(EXEC) clean

test_bloc : test_bloc.o $(OBJS)
dfs : dfs.o $(OBJS)
mknfs : mknfs.o $(OBJS)
vm : vm.o $(OBJS)
mkhd : mkhd.o $(OBJS)
dmps : dmps.o $(OBJS)
frmt : frmt.o $(OBJS)

if_cfile : if_cfile.o $(OBJS)
if_nfile : if_nfile.o $(OBJS)
if_dfile : if_dfile.o $(OBJS)
if_pfile : if_pfile.o $(OBJS)

% : %.o
	$(CC) $(CFLAGS) -o $@ $^ $(LIBS)

%.o : src/%.c
	$(CC) $(CFLAGS) -c $^ $(INCDIR)

.PHONY : run clean realclean depend
clean :
	$(RM) *.o
cleanexec : clean
	$(RM) $(EXEC)
realclean : clean cleanexec
	$(RM) vdiskA.bin vdiskB.bin
depend : 
	$(CC) $(CFLAGS) -MM $(INCDIR) $(SRCDIR)/*.c 

