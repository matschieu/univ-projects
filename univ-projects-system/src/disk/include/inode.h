
#ifndef __INODE_H__
#define __INODE_H__

#include "hwinfo.h"
#include "tools.h"

#define BLOC_SIZE       HDA_SECTORSIZE  
#define DATA_BLOC_SIZE  BLOC_SIZE

#define N_DIRECT	1

enum file_type_e {FILE_FILE, FILE_DIRECTORY, FILE_SPECIAL};

struct inode_s {
	unsigned int ind_magic;
	unsigned int ind_direct[N_DIRECT];
	unsigned int ind_indirect;
	unsigned int ind_double_indirect;
	enum file_type_e ind_type;
	unsigned int ind_size;
};

void read_inode(unsigned int inumber, struct inode_s* inode);
void write_inode(unsigned int inumber, const struct inode_s* inode);
unsigned int create_inode(enum file_type_e type);
int delete_inode(unsigned int inumber);
unsigned int vbloc_of_fbloc(unsigned int inumber, unsigned int bloc, bool_t do_allocate);

#endif
