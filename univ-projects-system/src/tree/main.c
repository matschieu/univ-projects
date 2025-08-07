
/*
* @author Matschieu
* @version December 2008
*/

#include <stdio.h>
#include <stdlib.h>
#include "tree.h"

int main(int argc, char** argv) {
	int total_dirs = 0;
	int total_files = 0;
	int color = 0;
	char* dir = ".";
	switch(argc) {
		case 2 :
			if (!strcmp(argv[1], "-c")) color = 1;
			else dir = argv[1];
			break;
		case 3 :
			if (!strcmp(argv[1], "-c")) {
				dir = argv[2];
				color = 1;
			}
			if (!strcmp(argv[2], "-c")) {
				dir = argv[1];
				color = 1;
			}
			break;
	}
	explore(dir, 0, "", &total_dirs, &total_files, color);
	printf("\n%d directories, %d files\n", total_dirs, total_files);
	return EXIT_SUCCESS;
}
