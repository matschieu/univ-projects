
/* @author Matschieu */

#include <stdio.h>
#include <stdlib.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include "tga.h"
#include "analyze/analyze.h"
#include "window/window.h"

int main(int argc, char** argv) {
	TGA_header_t header;
	Image_t pic;
	int fd1, fd2;
	int i, j;

	int verbose, display;
	char* input, * output;
	int effects[8];

	Option_t opts[12];
		
	verbose = display = 0;

	i = 0;
	opts[i].short_name = 'i';
	opts[i].long_name = "input";
	opts[i].parameter_type = PARAM_STRING;
	opts[i].parameter_ptr = NULL;
	opts[i].description = "specify the picture to load";
	opts[i].parameter_description = "the path of the TGA file in input";
	i++;
	opts[i].short_name = 'o';
	opts[i].long_name = "output";
	opts[i].parameter_type = PARAM_STRING;
	opts[i].parameter_ptr = NULL;
	opts[i].description = "specify the picture to save";
	opts[i].parameter_description = "the path of the TGA file in output";
	i++;
	opts[i].short_name = 'v';
	opts[i].long_name = "verbose";
	opts[i].parameter_type = PARAM_NONE;
	opts[i].parameter_ptr = &verbose;
	opts[i].description = "display each operation executed";
	opts[i].parameter_description = NULL;
	i++;
	opts[i].short_name = 'd';
	opts[i].long_name = "display";
	opts[i].parameter_type = PARAM_NONE;
	opts[i].parameter_ptr = &display;
	opts[i].description = "display the picture in a window";
	opts[i].parameter_description = NULL;
	i++;
	opts[i].short_name = 'g';
	opts[i].long_name = "grey";
	opts[i].parameter_type = PARAM_NONE;
	opts[i].parameter_ptr = &effects[0];
	opts[i].description = "apply a white and black effect on the picture";
	opts[i].parameter_description = NULL;
	i++;
	opts[i].short_name = 'n';
	opts[i].long_name = "negatif";
	opts[i].parameter_type = PARAM_NONE;
	opts[i].parameter_ptr = &effects[1];
	opts[i].description = "apply a negatif effect on the picture";
	opts[i].parameter_description = NULL;
	i++;
	opts[i].short_name = '1';
	opts[i].long_name = "blue";
	opts[i].parameter_type = PARAM_NONE;
	opts[i].parameter_ptr = &effects[2];
	opts[i].description = "apply a blue filter on the picture";
	opts[i].parameter_description = NULL;
	i++;
	opts[i].short_name = '2';
	opts[i].long_name = "red";
	opts[i].parameter_type = PARAM_NONE;
	opts[i].parameter_ptr = &effects[3];
	opts[i].description = "apply a red filter on the picture";
	opts[i].parameter_description = NULL;
	i++;
	opts[i].short_name = '3';
	opts[i].long_name = "green";
	opts[i].parameter_type = PARAM_NONE;
	opts[i].parameter_ptr = &effects[4];
	opts[i].description = "apply a green filter on the picture";
	opts[i].parameter_description = NULL;
	i++;
	opts[i].short_name = '4';
	opts[i].long_name = "blue-grey";
	opts[i].parameter_type = PARAM_NONE;
	opts[i].parameter_ptr = &effects[5];
	opts[i].description = "apply a white and black effect on the picture except for blue color";
	opts[i].parameter_description = NULL;
	i++;
	opts[i].short_name = '5';
	opts[i].long_name = "red-grey";
	opts[i].parameter_type = PARAM_NONE;
	opts[i].parameter_ptr = &effects[6];
	opts[i].description = "apply a white and black effect on the picture except for red color";
	opts[i].parameter_description = NULL;
	i++;
	opts[i].short_name = '6';
	opts[i].long_name = "green-grey";
	opts[i].parameter_type = PARAM_NONE;
	opts[i].parameter_ptr = &effects[7];
	opts[i].description = "apply a white and black effect on the picture except for green color";
	opts[i].parameter_description = NULL;

	/* Analyze command line parameters */
	if (verbose) printf("# Analize arguments from command line\n");
	analyze_command_line_arguments(opts, i + 1, argc, argv);

	input = opts[0].parameter_ptr;
	output = opts[1].parameter_ptr;
	
	/* Open files */
	if (input == NULL) error("no input file", "");
	if (verbose) printf("# Open input file %s\n", input);
	if ((fd1 = open(input, O_RDONLY)) == -1) error("can't open file", input);
	if (output != NULL) {
		if (verbose) printf("# Open output file %s\n", output);
		if ((fd2 = open(output, O_WRONLY | O_CREAT, S_IRWXU)) == -1) error("can't open file", output);
	}
	/* Read file */
	if (verbose) printf("# Read header from TGA file %s\n", input);
	read_tga_header(fd1, &header);
	pic.width = header.width;
	pic.height = header.height;
	if (!header.picture_id && !header.color_table && header.picture_type == 2) {
		if (verbose) printf("# Read picture from TGA file %s\n", input);
		read_picture(fd1, &pic, header);
	}
	else error("can't read this file - bad TGA format", "");

printf("%d\n\n\n", display);	
	for(i = 1; i < argc; i++) {
		printf("==========\n");
		for(j = 0; j < 12; j++) {
				printf("%d\n", effects[j]);
			if (effects[j] == i) {
				switch(j) {
					case 0 : black_white(pic);
						 break;
					case 1 : negative(pic);
						 printf("toto\n");
						 break;
					case 2 : blue(pic);
						 break;
					case 3 : red(pic);
						 break;
					case 4 : green(pic);
						 break;
					case 5 : blue_grey(pic);
						 break;
					case 6 : red_grey(pic);
						 break;
					case 7 : green_grey(pic);
						 break;
					default : printf("def\n");
				}
				break;
			}
		}
	}	

		
	if (output != NULL) {
		if (verbose) printf("# Write header to TGA file %s\n", output);
		write_tga_header(fd2, header);
		if (verbose) printf("# Write picture to TGA file %s\n", output);
		write_picture(fd2, &pic, header);
	}	
	
	if (verbose) printf("# Close file %s\n", input);
	close(fd1);
	if (output != NULL) {
		if (verbose) printf("# Close file %s\n", output);
		close(fd2);
	}
	
	if (display) {
		if (verbose) printf("# Open window\n");
		createWindow(0, 0, pic.width, pic.height);
		for (i = 0; i < pic.width; ++i)
			for (j = 0; j < pic.height; ++j) {
				Pixel_t p = pic.pixels[i * pic.width + j];
				putPixel(j, i, p.r, p.g, p.b);
			}
		sleep(10);
		if (verbose) printf("# Close window\n");
		destroyWindow();
	}

	if (verbose) printf("# End of program\n");
	return EXIT_SUCCESS;
}
