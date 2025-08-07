
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include "hwinfo.h"
#include "drive.h"

#define OCTAL_DUMP 1
#define ASCII_DUMP 1

void dumps(unsigned int cylinder, unsigned int sector) {
	int i, j;
	unsigned char* buffer = (unsigned char*)malloc(HDA_SECTORSIZE);
	if (!buffer) {
		fprintf(stderr, "Error[%s]: malloc\n", __FUNCTION__);
		exit(EXIT_FAILURE);
	}
	read_sector(cylinder, sector, buffer);
	printf("* Dump result [cylinder %d, sector %d]\n", cylinder, sector);
	for (i = 0; i < sizeof(buffer); i += 16) {
		printf("%.8o", i);
		if (OCTAL_DUMP) {
			for(j = 0; j < 8; j++)
				printf(" %.2x", buffer[i + j]);
			printf(" - ");
			for( ; j<16; j++)
				printf(" %.2x", buffer[i + j]);
			printf("\n");
		}
		if (ASCII_DUMP) {
			printf("%8c", ' ');
			for(j = 0; j < 8; j++)
				printf(" %1c ", isprint(buffer[i + j]) ? buffer[i + j]:' ');
			printf(" - ");
			for( ; j < 16; j++)
				printf(" %1c ", isprint(buffer[i + j]) ? buffer[i + j]:' ');
			printf("\n");
		}
	}
}

int main(int argc, char** argv) {
	if (argc != 3) {
		if (argc < 3)
			fprintf(stderr, "Error: argument(s) missing [%s cylinder sector]\n", argv[0]);
		if (argc > 3)
			fprintf(stderr, "Error: too much arguments [%s cylinder sector]\n", argv[0]);
		return EXIT_FAILURE;
	}
	printf("Initializing hardware... ");
	fflush(stdout);
	if (!init_master("hardware.ini"))
		return EXIT_FAILURE;
	printf("ok\n");
	dumps(atoi(argv[1]), atoi(argv[2]));
	return EXIT_SUCCESS;
}

