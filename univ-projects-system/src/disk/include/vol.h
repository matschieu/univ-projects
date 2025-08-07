
#ifndef __VOL_H__
#define __VOL_H__

unsigned int cylinder_of_bloc(unsigned int vol, unsigned int bloc);
unsigned int sector_of_bloc(unsigned int vol, unsigned int bloc);
void read_bloc(unsigned int vol, unsigned int bloc, unsigned char* buffer);
void read_bloc_n(unsigned int vol, unsigned int bloc, unsigned char* buffer, unsigned int buf_size);
void write_bloc(unsigned int vol, unsigned int bloc, unsigned char* buffer);
void write_bloc_n(unsigned int vol, unsigned int bloc, unsigned char* buffer, unsigned int buf_size);
void format_bloc(unsigned int vol);

#endif
