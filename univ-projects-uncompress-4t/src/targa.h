#ifndef __TARGA_H__
#define __TARGA_H__

#include "pixel.h"

/*
les deux fonctions renvoient
0 en cas d'erreur
1 sinon
*/

int loadTGA(char* filename, PIXEL img[512][512]);
int saveTGA(PIXEL img[512][512], char* filename);

#endif
