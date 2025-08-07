
#include <stdio.h>
#include <stdlib.h>
#include "drive.h"
#include "super.h"
#include "mbr.h"
#include "mtools.h"

int main(int argc, char** argv) {
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
	printf("Creating a new filesystem on partition %d... ", current_volume);
	fflush(stdout);
	init_super(current_volume, "Matschieu", 50287);
	printf("ok\n");
	return EXIT_SUCCESS;
}
