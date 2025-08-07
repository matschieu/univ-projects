
#ifndef __SUPER_H__
#define __SUPER_H__

#define MAX_ID		8
#define BLOC_NULL	0

struct super_s {
	unsigned int super_magic;
	char super_id[MAX_ID];
	unsigned int super_serial;
	unsigned int super_firstfree;
	unsigned int super_nfree;
	unsigned int super_root;
};

struct freeb_s {
	unsigned int fb_nfree;
	unsigned int fb_nextfree;
};

extern struct super_s super;
extern unsigned int current_vol;

int load_super(unsigned int vol);
void save_super();
void init_super(unsigned int vol, char id[MAX_ID], unsigned int serial);
int new_bloc();
void free_bloc(unsigned int bloc);
void free_blocs(unsigned int blocs[], unsigned int nbloc);

#endif
