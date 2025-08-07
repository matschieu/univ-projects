
#include <stdio.h>
#include <stdlib.h>
#include "drive.h"
#include "super.h"
#include "mbr.h"
#include "mtools.h"
#include "hwinfo.h"

int main(int argc, char** argv) {
	int i;
	unsigned int current_volume = get_current_volume();
	printf("Initializing hardware... ");
	fflush(stdout);
	if (!init_master(get_hw_config()))
		return EXIT_FAILURE;
	printf("ok\n");
	printf("Loading HDA MBR... ");
	fflush(stdout);
	if (!load_mbr()) 
		return EXIT_FAILURE;
	printf("ok\n");
	for(i = 0; i < mbr.mbr_nvol; i++) {
		struct vol_s vol = mbr.mbr_vols[i];
		int nbloc = vol.vol_nbloc;
		load_super(i);
		printf("Partition %d\n", i);
		printf("\tSize: %d bytes (%d blocks)\n", ((nbloc - 1) * HDA_SECTORSIZE), (nbloc - 1));
		printf("\tReal size: %d bytes (%d blocks)\n", (nbloc * HDA_SECTORSIZE), nbloc);
		if (i == current_volume) {
			int used = nbloc - 1 - super.super_nfree;
			printf("\tUsed: %d bytes (%d blocks)\n", (used * HDA_SECTORSIZE), used);
			printf("\tUsed rate: %d%%\n", (used * 100 / (nbloc - 1)));
		}
	}
	return EXIT_SUCCESS;
}
