
/* @author Matschieu */

#include <stdio.h>
#include <stdlib.h>
#include "analyze.h"

int main(int argc, char** argv) {
	char fuck, kill, destr;
	double mat;
	char* ass;
	Option_t opts[5];
	
	fuck = kill = destr = 0;
	
	opts[0].short_name = 'f';
	opts[0].long_name = "fuck";
	opts[0].parameter_type = PARAM_NONE; 
	opts[0].parameter_ptr = &fuck;
	opts[0].description = "Fuck the world";
	opts[0].parameter_description = NULL;
	
	opts[1].short_name = 'k';
	opts[1].long_name = "kill";
	opts[1].parameter_type = PARAM_NONE;
	opts[1].parameter_ptr = &kill;
	opts[1].description = "Kill all people";
	opts[1].parameter_description = NULL;
	
	opts[2].short_name = 'd';
	opts[2].long_name = "destr";
	opts[2].parameter_type = PARAM_NONE;
	opts[2].parameter_ptr = &destr;
	opts[2].description = "Destroy a car in the street";
	opts[2].parameter_description = NULL;
	
	opts[3].short_name = 'a';
	opts[3].long_name = "ass";
	opts[3].parameter_type = PARAM_STRING;
	opts[3].parameter_ptr = NULL;
	opts[3].description = "Kiss my ass";
	opts[3].parameter_description = "nb";
	
	opts[4].short_name = 'm';
	opts[4].long_name = "mathieu";
	opts[4].parameter_type = PARAM_DOUBLE;
	opts[4].parameter_ptr = &mat;
	opts[4].description = "mat";
	opts[4].parameter_description = "p";
			
	analyze_command_line_arguments(opts, 5, argc, argv);
	
	ass = opts[3].parameter_ptr;
	
	printf("fuck %s\n", (fuck ? "yes" : "no"));
	printf("kill %s\n", (kill ? "yes" : "no"));
	printf("destr %s\n", (destr ? "yes" : "no"));
	printf("ass %s=%s\n", (ass ? "yes" : "no"), ass);
	printf("mathieu %s=%f\n", (mat ? "yes" : "no"), mat);
	
	return EXIT_SUCCESS;
}
