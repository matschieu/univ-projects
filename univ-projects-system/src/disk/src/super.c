
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "super.h"
#include "mbr.h"
#include "vol.h"

#define SUPER_MAGIC	0xdeadbeef
#define SUPER		0

struct super_s super;
unsigned int current_vol;

int load_super(unsigned int vol) {
	read_bloc_n(vol, SUPER, (unsigned char*)&super, sizeof(struct super_s));
	current_vol = vol;
	return super.super_magic == SUPER_MAGIC;
}

void save_super() {
	write_bloc_n(current_vol, SUPER, (unsigned char*)&super, sizeof(struct super_s));
}

void init_super(unsigned int vol, char id[MAX_ID], unsigned int serial) {
	struct freeb_s fb;
	super.super_magic = SUPER_MAGIC;
	strcpy(super.super_id, id);
	super.super_serial = serial;
	super.super_firstfree = SUPER + 1;
	super.super_nfree = mbr.mbr_vols[vol].vol_nbloc - 1;
	super.super_root = 0;
	fb.fb_nfree = super.super_nfree;
	fb.fb_nextfree = 0;
	current_vol = vol;
	save_super();
	write_bloc_n(vol, SUPER + 1, (unsigned char*)&fb, sizeof(struct freeb_s));
}

int new_bloc() {
	struct freeb_s fb;
	unsigned int newbloc;
	if (super.super_nfree == 0)
		return BLOC_NULL;
	read_bloc_n(current_vol, super.super_firstfree, (unsigned char*)&fb, sizeof(struct freeb_s));
	newbloc = super.super_firstfree;
	super.super_nfree--;
	if (fb.fb_nfree == 1) {
		super.super_firstfree = fb.fb_nextfree;
		return newbloc;
	}
	fb.fb_nfree--;
	super.super_firstfree++;
	write_bloc_n(current_vol, super.super_firstfree, (unsigned char*)&fb, sizeof(struct freeb_s));
	return newbloc;

}

void free_bloc(unsigned int bloc) {
	struct freeb_s fb;
	if (bloc <= 0)
		return;
	fb.fb_nfree = 1;
	fb.fb_nextfree = super.super_firstfree;
	write_bloc_n(current_vol, bloc, (unsigned char*)&fb, sizeof(struct freeb_s));
	super.super_nfree++;
	super.super_firstfree = bloc;
}

void free_blocs(unsigned int blocs[], unsigned int nbloc) {
	int i;
	for(i = 0; i < nbloc; i++) 
		free_bloc(blocs[i]);
}

