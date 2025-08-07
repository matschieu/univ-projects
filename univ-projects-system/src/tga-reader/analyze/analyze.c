
/* @author Matschieu */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "analyze.h"

void display_help(Option_t options[], int options_size, char** argv) {
	int i;
	printf("Usage : %s [options] \n", argv[0]);
	printf("Options\n");
	printf("\t-h / --help :\n\t\tPrint this help message\n");
	for(i = 0; i < options_size; i++) {
		switch(options[i].parameter_type) {
			case PARAM_NONE : 
				printf("\t-%c / --%s :\n\t\t%s\n", options[i].short_name, options[i].long_name, options[i].description);
				break;
			case PARAM_STRING : 
			case PARAM_LONG : 
			case PARAM_DOUBLE : 
				printf("\t-%c=<%s> / --%s=<%s> :\n\t\t%s\n", options[i].short_name, options[i].parameter_description, options[i].long_name, options[i].parameter_description, options[i].description);		
		}
	}
	exit(EXIT_SUCCESS);
}

void display_error(char* message, char* opt) {
	fprintf(stderr, "Error : %s %s\n", message, opt);
	printf("You should use --help option for more information\n");
	exit(EXIT_FAILURE);
}

void long_parameter_analize(Option_t options[], int options_size, int argc, char** argv, int* i) {
	int j;
	for(j = 0; j < options_size; j++) {
		if (!strcmp(argv[*i], "--help")) display_help(options, options_size, argv);
		if (!strcmp(argv[*i] + 2, options[j].long_name)) {
			if (options[j].parameter_type != PARAM_NONE && (*i + 1) >= argc) 
				display_error("parameter is missing after option", argv[*i]);
			switch(options[j].parameter_type) {
				case PARAM_NONE :
					*((char*)options[j].parameter_ptr) = *i;
					break;
				case PARAM_STRING :
					options[j].parameter_ptr = argv[++(*i)];
					break;
				case PARAM_LONG :
					*((int*)options[j].parameter_ptr) = atoi(argv[++(*i)]);
					break;
				case PARAM_DOUBLE :
					*((double*)options[j].parameter_ptr) = atof(argv[++(*i)]);
					break;
			}
			return;
		}
	}
	display_error("parameter incorrect", argv[*i]);
}

void little_parameter_analize(Option_t options[], int options_size, int argc, char** argv, int* i, int idx) {
	int j;
	if (!argv[*i][idx]) return;
	for(j = 0; j < options_size; j++) {
		if (argv[*i][idx] == 'h') display_help(options, options_size, argv);
		if (argv[*i][idx] == options[j].short_name) {
			if (options[j].parameter_type != PARAM_NONE && *(argv[*i] + idx + 1))
				display_error("parameter incorrect ", argv[*i]);
			if (options[j].parameter_type != PARAM_NONE && ((*i) + 1) >= argc) 
				display_error("parameter is missing after option", argv[*i]);
			switch(options[j].parameter_type) {
				case PARAM_NONE :
					*((char*)options[j].parameter_ptr) = *i;	
					little_parameter_analize(options, options_size, argc, argv, i, idx + 1);
					break;
				case PARAM_STRING :
					options[j].parameter_ptr = (void*)argv[++(*i)];
					break;
				case PARAM_LONG :
					*((int*)options[j].parameter_ptr) = atoi(argv[++(*i)]);
					break;
				case PARAM_DOUBLE :
					*((double*)options[j].parameter_ptr) = atof(argv[++(*i)]);
					break;
			}
			return;
		}
	}
	display_error("parameter incorrect", argv[*i]);
}

void analyze_command_line_arguments(Option_t options[], int options_size, int argc, char** argv) {
	int i;
	for(i = 1; i < argc; i++) {
		if (!strcmp(argv[i], "--help") || !strcmp(argv[i], "-h")) display_help(options, options_size, argv);
		if (argv[i][0] == '-' && argv[i][1] == '-')
			long_parameter_analize(options, options_size, argc, argv, &i);
		else if (argv[i][0] == '-' && argv[i][1] != '-')
			little_parameter_analize(options, options_size, argc, argv, &i, 1);
		else display_error("parameter incorrect ", argv[i]);
	}
}
