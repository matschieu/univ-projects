
#include <stdio.h>
#include <stdlib.h>
#include "drive.h"

int main(int argc, char** argv) {
	printf("Initializing hardware... ");
	fflush(stdout);
	if (!init_master("hardware.ini"))
		return EXIT_FAILURE;
	printf("ok\n");
	return EXIT_SUCCESS;
}

