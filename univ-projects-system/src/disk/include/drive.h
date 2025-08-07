
#ifndef __DRIVE_H__
#define __DRIVE_H__

int init_master(const char* ini_file);
void read_sector(unsigned int cylinder, unsigned int sector, unsigned char* buffer);
void read_sector_n(unsigned int cylinder, unsigned int sector, unsigned char* buffer, unsigned int buf_size);
void write_sector(unsigned int cylinder, unsigned int sector, const unsigned char* buffer); 
void write_sector_n(unsigned int cylinder, unsigned int sector, const unsigned char* buffer, unsigned int buf_size);
void format_sector(unsigned int cylinder, unsigned int sector, unsigned int nsector, unsigned int value);
void chk_geometry();

#endif
