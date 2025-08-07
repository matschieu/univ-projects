
/* @author Matschieu */

#ifndef __ANALYZE_H__
#define __ANALYZE_H__

enum Parameter_Type { PARAM_NONE, PARAM_STRING, PARAM_LONG, PARAM_DOUBLE };

struct Option {
	char short_name;
	char* long_name;
	enum Parameter_Type parameter_type;
	void* parameter_ptr;
	char* description;
	char* parameter_description;
};

typedef struct Option Option_t;

void analyze_command_line_arguments(struct Option options[], int options_size, int argc, char** argv);

#endif
