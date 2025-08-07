
/*
* @author Matschieu
* @version December 2008
*/

#ifndef __TREE_H__
#define __TREE_H__

int stringcmp(const void* p1, const void* p2);
void error(char* message);
int explore(char* dir_name, int level, char* pref, int* total_dirs, int* total_files, int color);

#endif
