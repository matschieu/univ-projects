
#include <stdio.h>
#include <stdlib.h>
#include "mbr.h"
#include "drive.h"
#include "hwinfo.h"
#include <string.h>

struct mbr_s mbr;

int load_mbr() {
	if (sizeof(struct mbr_s) > HDA_SECTORSIZE) {
		fprintf(stderr, "Error[%s]: invalid MBR\n", __FUNCTION__);
		return 0;
	}
	read_sector_n(0, 0, (unsigned char*)&mbr, sizeof(struct mbr_s));
	if (mbr.mbr_magic == MBR_MAGIC && mbr.mbr_nvol <= MAX_VOL)
		return 1;
	fprintf(stderr, "Error[%s]: MBR initialization (0 partition)\n", __FUNCTION__);
	mbr.mbr_magic = MBR_MAGIC;
	mbr.mbr_nvol = 0;
	return 0;
}

void save_mbr() {
	write_sector_n(0, 0, (unsigned char*)&mbr, sizeof(struct mbr_s));
}

