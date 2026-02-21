
#include <stdio.h>
#include <stdlib.h>
#include "hwinfo.h"
#include "drive.h"

int main(int argc, char** argv) {
	int i;
	int value = 0;
	if (argc > 2) {
		fprintf(stderr, "Error: too much arguments [%s value]\n", argv[0]);
		return EXIT_FAILURE;
	}
	if (argc == 2)
		value = atoi(argv[1]);
	printf("Initializing hardware... ");
	fflush(stdout);
	if (!init_master("hardware.ini"))
		return EXIT_FAILURE;
	printf("ok\n");
	printf("Format disk[HDA] (val=%d)... ", value);
	fflush(stdout);
	for(i = 0; i < HDA_MAXCYLINDER; i++)
		format_sector(i, 0, HDA_MAXSECTOR, value);
	printf("ok\n");
	return EXIT_SUCCESS;
}

