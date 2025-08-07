
#include <stdio.h>
#include <stdlib.h>
#include "inode.h"
#include "vol.h"
#include "super.h"

#define INODE_MAGIC		0xdeadbeef
#define N_NBLOC_PER_BLOC	3

void read_inode(unsigned int inumber, struct inode_s* inode) {
	read_bloc_n(current_vol, inumber, (unsigned char*)inode, sizeof(struct inode_s));
	if (inode->ind_magic != INODE_MAGIC) {
		fprintf(stderr, "Error[%s]: invalid inumber (%d)\n", __FUNCTION__, inumber);
		exit(EXIT_FAILURE);
	}
}

void write_inode(unsigned int inumber, const struct inode_s* inode) {
	if (inode->ind_magic != INODE_MAGIC) {
		fprintf(stderr, "Error[%s]: invalid inumber (%d)\n", __FUNCTION__, inumber);
		return;
	}
	write_bloc_n(current_vol, inumber, (unsigned char*)inode, sizeof(struct inode_s));
}

unsigned int create_inode(enum file_type_e type) {
	struct inode_s inode;
	unsigned int inumber;
	int i;
	inode.ind_magic = INODE_MAGIC;
	for(i = 0; i < N_DIRECT; i++)
		inode.ind_direct[i] = 0;
	inode.ind_indirect = 0;
	inode.ind_double_indirect = 0;
	inode.ind_type = type;
	inode.ind_size = 0;
	inumber = new_bloc();
	if (!inumber) {
		fprintf(stderr, "Error[%s]: invalid inumber (%d)\n", __FUNCTION__, inumber);
		return 0;
	}
	write_inode(inumber, &inode);
	return inumber;
}

int delete_inode(unsigned int inumber) {
	struct inode_s inode;
	unsigned int ibloc[N_NBLOC_PER_BLOC];
	unsigned int dibloc[N_NBLOC_PER_BLOC];
	int i;
	read_inode(inumber, &inode);
	free_bloc(inumber);
	free_blocs(inode.ind_direct, N_DIRECT);
	if (inode.ind_indirect) {
		read_bloc_n(current_vol, inode.ind_indirect, (unsigned char*)ibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
		free_bloc(inode.ind_indirect);
		free_blocs(ibloc, N_NBLOC_PER_BLOC);
	}
	if (inode.ind_double_indirect) {
		read_bloc_n(current_vol, inode.ind_double_indirect, (unsigned char*)dibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
		free_bloc(inode.ind_double_indirect);
		for(i = 0; i < N_NBLOC_PER_BLOC; i++) {
			if (dibloc[i]) {
				read_bloc_n(current_vol, dibloc[i], (unsigned char*)ibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
				free_bloc(dibloc[i]);
				free_blocs(ibloc, N_NBLOC_PER_BLOC);
			}
		}
	}
	return 0;
}

unsigned int vbloc_of_fbloc(unsigned int inumber, unsigned int bloc, bool_t do_allocate) {
	struct inode_s inode;
	unsigned int ibloc[N_NBLOC_PER_BLOC];
	unsigned int dibloc[N_NBLOC_PER_BLOC];
	int i, j;
	read_inode(inumber, &inode);
	if (bloc < N_DIRECT) {
		if (inode.ind_direct[bloc] == BLOC_NULL && do_allocate) {
			inode.ind_direct[bloc] = new_bloc();
			write_inode(inumber, &inode);
		}
		return inode.ind_direct[bloc];
	}
	bloc -= N_DIRECT;
	if (bloc < N_NBLOC_PER_BLOC) {
		if (inode.ind_indirect == BLOC_NULL) {
			if (do_allocate) {
				inode.ind_indirect = new_bloc();
				for(i = 0; i < N_NBLOC_PER_BLOC; i++) 
					ibloc[i] = BLOC_NULL;
				write_inode(inumber, &inode);
				write_bloc_n(current_vol, inode.ind_indirect, (unsigned char*)ibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
			}
			return BLOC_NULL;
		}
		read_bloc_n(current_vol, inode.ind_indirect, (unsigned char*)ibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
		if (ibloc[bloc] == BLOC_NULL) {
			if (do_allocate) {
				ibloc[bloc] = new_bloc();
				write_bloc_n(current_vol, inode.ind_indirect, (unsigned char*)ibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
			}
			return BLOC_NULL;
		}
		return ibloc[bloc];
	}
	bloc -= N_NBLOC_PER_BLOC;
	if (bloc < N_NBLOC_PER_BLOC * N_NBLOC_PER_BLOC) {
		if (inode.ind_double_indirect == BLOC_NULL) {
			if (do_allocate) {
				inode.ind_double_indirect = new_bloc();
				for(i = 0; i < N_NBLOC_PER_BLOC; i++) 
					dibloc[i] = BLOC_NULL;
				write_inode(inumber, &inode);
				write_bloc_n(current_vol, inode.ind_double_indirect, (unsigned char*)dibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
			}
			return BLOC_NULL;
		}
		read_bloc_n(current_vol, inode.ind_double_indirect, (unsigned char*)dibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
		for(i = 0; i < N_NBLOC_PER_BLOC; i++) {
			if (dibloc[i] == BLOC_NULL) {
				if (do_allocate) {
					dibloc[i] = new_bloc();
					for(j = 0; j < N_NBLOC_PER_BLOC; j++) 
						ibloc[j] = BLOC_NULL;
					write_bloc_n(current_vol, dibloc[i], (unsigned char*)ibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
					write_bloc_n(current_vol, inode.ind_double_indirect, (unsigned char*)dibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
				}
				return BLOC_NULL;
			}
			if (bloc < N_NBLOC_PER_BLOC) {
				read_bloc_n(current_vol, dibloc[i], (unsigned char*)ibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
				if (ibloc[bloc] == BLOC_NULL) {
					if (do_allocate) {
						ibloc[bloc] = new_bloc();
						write_bloc_n(current_vol, dibloc[i], (unsigned char*)ibloc, N_NBLOC_PER_BLOC * sizeof(unsigned int));
					}
					return BLOC_NULL;
				}
				return ibloc[bloc];
			}
			bloc -= N_NBLOC_PER_BLOC;
		}
	}
	else 
		fprintf(stderr, "Error[%s]: bloc number too big\n", __FUNCTION__);
	return 0;
}

