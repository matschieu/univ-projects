
#ifndef __MBR_H__
#define __MBR_H__

#define MAX_VOL		8
#define MBR_MAGIC	0xDEADBEEF

enum vol_type_e { VOL_BASE, VOL_ANNEX, VOL_OTHER };

struct vol_s {
	unsigned int vol_cylinder;
	unsigned int vol_sector;
	unsigned int vol_nbloc;
	enum vol_type_e vol_type;
};

struct mbr_s {
	struct vol_s mbr_vols[MAX_VOL];
	unsigned int mbr_nvol;
	unsigned int mbr_magic;
};

extern struct mbr_s mbr;

int load_mbr();
void save_mbr();

#endif
