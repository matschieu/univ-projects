#ifndef __WINDOW_H__
#define __WINDOW_H__


#include "X11/Xlib.h"
#include "X11/Xutil.h"

/* Ouvre une fenetre graphique, en (x,y) de largeur width et de hauteur height */
int createWindow(int x,int y,int width,int height);

/* Ferme la fenetre graphique */
void destroyWindow();

/* dessine un pixel a l'ecran */
void putPixel(int x,int y, unsigned char red, unsigned char green, unsigned char blue);

/* Redessine la fenetre courante */
void redrawWindow();

/* Efface la fenetre courante */
void clearGraph();

#endif
