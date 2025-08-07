
#include <stdio.h>
#include <stdlib.h>
#include "vol.h"
#include "mbr.h"
#include "drive.h"
#include "hwinfo.h"

unsigned int cylinder_of_bloc(unsigned int vol, unsigned int bloc) {
	int first_sector = mbr.mbr_vols[vol].vol_sector;
	int first_cylinder = mbr.mbr_vols[vol].vol_cylinder;
	return first_cylinder + (first_sector + bloc) / SECTOR_REK_CYLINDER;
}

unsigned int sector_of_bloc(unsigned int vol, unsigned int bloc) {
	int first_sector = mbr.mbr_vols[vol].vol_sector;
	return (first_sector + bloc) % SECTOR_REK_CYLINDER;
}

void read_bloc(unsigned int vol, unsigned int bloc, unsigned char* buffer) {
	if (vol > mbr.mbr_nvol || bloc >= mbr.mbr_vols[vol].vol_nbloc) {
		fprintf(stderr, "Error[%s]: invalid volume/bloc number (%d,%d)\n", __FUNCTION__, vol, bloc);
		return;
	}
	read_sector(cylinder_of_bloc(vol, bloc), sector_of_bloc(vol, bloc), buffer);
}

void read_bloc_n(unsigned int vol, unsigned int bloc, unsigned char* buffer, unsigned int buf_size) {
	if (vol > mbr.mbr_nvol || bloc >= mbr.mbr_vols[vol].vol_nbloc) {
		fprintf(stderr, "Error[%s]: invalid volume/bloc number (%d,%d)\n", __FUNCTION__, vol, bloc);
		return;
	}
	read_sector_n(cylinder_of_bloc(vol, bloc), sector_of_bloc(vol, bloc), buffer, buf_size);
}

void write_bloc(unsigned int vol, unsigned int bloc, unsigned char* buffer) {
	if (vol > mbr.mbr_nvol || bloc >= mbr.mbr_vols[vol].vol_nbloc) {
		fprintf(stderr, "Error[%s]: invalid volume/bloc number (%d,%d)\n", __FUNCTION__, vol, bloc);
		return;
	}
	write_sector(cylinder_of_bloc(vol, bloc), sector_of_bloc(vol, bloc), buffer);
}

void write_bloc_n(unsigned int vol, unsigned int bloc, unsigned char* buffer, unsigned int buf_size) {
	if (vol > mbr.mbr_nvol || bloc >= mbr.mbr_vols[vol].vol_nbloc) {
		fprintf(stderr, "Error[%s]: invalid volume/bloc number (%d,%d)\n", __FUNCTION__, vol, bloc);
		return;
	}
	write_sector_n(cylinder_of_bloc(vol, bloc), sector_of_bloc(vol, bloc), buffer, buf_size);
}

void format_bloc(unsigned int vol) {
	if (vol > mbr.mbr_nvol) {
		fprintf(stderr, "Error[%s]: invalid volume number (%d)\n", __FUNCTION__, vol);
		return;
	}
	format_sector(cylinder_of_bloc(vol, 0), sector_of_bloc(vol, 0), mbr.mbr_vols[vol].vol_nbloc, 0);
}

