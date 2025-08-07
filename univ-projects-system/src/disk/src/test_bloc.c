
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "drive.h"
#include "super.h"
#include "mbr.h"
#include "mtools.h"
#include "hwinfo.h"

void display_info(int vol) {
	int nbloc = mbr.mbr_vols[vol].vol_nbloc;
	int used = nbloc - 1 - super.super_nfree;
	printf("====================================\n");
	printf("Size: %d bytes (%d blocks)\n", ((nbloc - 1) * HDA_SECTORSIZE), (nbloc - 1));
	printf("Used: %d bytes (%d blocks) = %d%%\n", (used * HDA_SECTORSIZE), used, (used * 100 / (nbloc - 1)));
	printf("====================================\n");
}

int main(int argc, char** argv) {
	int i, b = 0;
	unsigned int current_volume = get_current_volume();
	int nbloc;
	srand(time(NULL));
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
	printf("Loading super bloc (partition %d)... ", current_volume);
	fflush(stdout);
	if (!load_super(current_volume)) {
		fprintf(stderr, "error\n");
		return EXIT_FAILURE;
	}
	printf("ok\n");
	if (super.super_nfree == 0)
		printf("no free block\n");
	else {
		display_info(current_volume);
		while(new_bloc() != BLOC_NULL) b++; 
		printf("* %d blocks returned by new_bloc()\n", b);
	}
	display_info(current_volume);
	b = 0;
	nbloc = mbr.mbr_vols[current_volume].vol_nbloc;
	for(i = rand() % (nbloc - 1); i > 0; i--) {
		int r = rand() % (nbloc - 2) + 1;
		free_bloc(r);
		b++;
		printf("Blocks %d is now free\n", r);
	}
	printf("* %d random blocks are now free\n", b);
	display_info(current_volume);
	save_super();
	return EXIT_SUCCESS;
}
