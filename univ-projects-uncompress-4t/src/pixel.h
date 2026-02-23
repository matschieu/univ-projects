#ifndef __PIXEL_H__
#define __PIXEL_H__

typedef struct {
	/*int img[512][512];*/
	unsigned char r,g,b;
} PIXEL;
/*PIXEL matrice[512][512];*/
void color(int x, int y,int l, int n);
char lecture(int *n, int *cpt);
/*int n;*/
int bin2int(int tab[8]);
void decomp(int x,int y,int l, int n);
#endif
