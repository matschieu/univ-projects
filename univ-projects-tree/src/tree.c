
/*
* @author Matschieu
* @version December 2008
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <sys/types.h>
#include <dirent.h>
#include "tree.h"

int stringcmp(const void* p1, const void* p2) {
	return strcmp(*(char* const*) p1, *(char* const*) p2);
}

void error(char* message) {
	if (message != NULL) fprintf(stderr, "%s\n", message);
	exit(EXIT_FAILURE);
}

#define MAX_FILES 12000
int explore(char* dir_name, int level, char* pref, int* total_dirs, int* total_files, int color) {
	struct dirent* ent;
	DIR* dir = opendir(dir_name);
	char** files = (char**)malloc(MAX_FILES * sizeof(char*));
	int i, nb_files = 0;
	if (dir == NULL) error(strcat("Error : can't read ", dir_name));
	if (files == NULL) error("Error  : can't allocate memory");
	for(i = 0; i < MAX_FILES; i++) files[i] = NULL;	
	while((ent = readdir(dir)) != NULL && nb_files < MAX_FILES) files[nb_files++] = ent->d_name;
	qsort(files, nb_files--, sizeof(char*), stringcmp);
	if (level == 0) printf("%s\n", dir_name);
	for(i = 0; i < MAX_FILES && files[i] != NULL; i++) {
		if (!strcmp(files[i], ".") || !strcmp(files[i], "..") || files[i][0] == '.') continue;
		else {
			char* file_path = (char*)malloc(sizeof(char) * (strlen(dir_name) + strlen(files[i]) + 2));
			struct stat st;
			if (file_path == NULL) error("Error  : can't allocate memory");
			*file_path = '\0';
			file_path = strcat(file_path, dir_name);
			file_path = strcat(file_path, "/");
			file_path = strcat(file_path, files[i]);
			stat(file_path, &st);
			printf("%s", pref);
			printf("%s", (i == nb_files ? "`" : "|"));
			printf("-- ");
			if (S_ISREG(st.st_mode)) {
				(*total_files)++;
				if (color && st.st_mode & S_IXUSR) printf("\033[01;32m");
				printf("%s\n", files[i]);
				if (color && st.st_mode & S_IXUSR) printf("\033[00m");
			}
			else if (S_ISDIR(st.st_mode)) {
				char* new_pref = (char*)malloc(sizeof(char) * (level * 4 + 5));
				if (new_pref == NULL) error("Error  : can't allocate memory");
				(*total_dirs)++;
				if (color) printf("\033[01;34m");
				printf("%s\n", files[i]);
				if (color) printf("\033[00m");
				*new_pref = '\0';
				new_pref = strcat(new_pref, pref);
				new_pref = strcat(new_pref, i == nb_files ? "    " : "|   ");
				explore(file_path, level + 1, new_pref, total_dirs, total_files, color);
				free(new_pref);
			}
			free(file_path);
		}
	}
	free(files);
	if (closedir(dir) == -1) error(strcat("Error : can't close ", dir_name));
	return 1;
}
