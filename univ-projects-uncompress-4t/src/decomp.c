
/* Permet l'affichage pour déboguer */
/*#define DEBUG*/

/* INCLUDES ET DEFINITION */


#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <fcntl.h>
#include <math.h>

#include "pixel.h"
#include "targa.h"

/* Code d'erreur ou de succés renvoyés par les fonctions */
#define ERROR_CODE -1
#define SUCCESS_CODE 0
#define EOF_CODE -2

/*
On modélise un octet sous forme d'un tableau de char de taille ARRAY_SIZE
Un octet représentant une séquence de 8 bits, ARRAY_SIZE vaut 8
*/
#define ARRAY_SIZE 8

/* Dimension de l'image TGA */
#define PICURE_SIZE 512


/* STRUCTURES */

/*
file représente le chemin du fichier à traiter
Octet represente le n-ième octet du fichier qui doit être lu
bit représente le rang du prochain bit à lire
*/
typedef struct Position {
	int byte;
	int bit;
} Position;

/* Arbre quaternaire */
typedef struct Tree {
	PIXEL* pixel;
	int level;
	struct Tree* sons[4];
} Tree;

/* Position du curseur dans le fichier */
typedef struct Point{
	int x;
	int y;
} Point;

/* redéfinition des types pourris qui font chier et ne sont pas explicites */
typedef PIXEL Pixel;
typedef char Byte;


/* DECLARATIONS DE FONCTIONS */


void addASon(Tree*, int, Pixel*);
Tree* newTree(Pixel*, int);
void freeTree(Tree* t);
Byte* newByte();
Position* initPosition();
Pixel* newPixel();
int shiftToLeft(int, Byte*);
int shiftToRight(int, Byte*);
int readByte(char*, Position*, unsigned char*);
int readBit(char*, Position*, int, Byte*);
int byteToInt(Byte* t);
Tree* toTree(char*, Byte*, Position*, int);
int toTGA(Tree*, char*);
int main(int argc, char** argv);
/* void toArray(Tree* , Pixel** , Point* ); */


/* FONCTIONS */


Tree* newTree(Pixel* pix, int l) {
	int i;
	Tree* t = (Tree*)malloc(sizeof(Tree));
	t->pixel = pix;
	t->level = l;
	for(i = 0; i < 4; i++) t->sons[i] = NULL;
	return t;
}

void freeTree(Tree* t) {
	int i;
	for(i = 0; i < 4; i++)
		if (t->sons[i] != NULL) freeTree(t->sons[i]);
	if (t->pixel != NULL) free(t->pixel);
	free(t);
}

Byte* newByte() {
	int eger;
	Byte* t = (Byte*)malloc(ARRAY_SIZE * sizeof(Byte));
	for(eger = 0; eger < ARRAY_SIZE; eger++) t[eger] = -1;
	return t;
}

Position* initPosition() {
	Position* p = (Position*)malloc(sizeof(Position));
	p->byte = 0;
	p->bit = ARRAY_SIZE - 1;
	return p;
}

Pixel* newPixel() {
	Pixel* p = (Pixel*)malloc(sizeof(Pixel));
	return p;
}

/* Décale un octet t de nb bits vers la droite */
int shiftToLeft(int nb, Byte* t) {
	int i;
	if (t == NULL) return ERROR_CODE;
	for(i = ARRAY_SIZE - 1; i >= 0; i--) {
		if (i + nb < ARRAY_SIZE) {
			t[i + nb] = t[i];
			t[i] = 0;
		}
		else t[i] = 0;
	}
	return SUCCESS_CODE;
}

/*
Lit un octet byte dans un fichier f à partir de la position p
Retourne ERROR_CODE si la fonction rencontre une erreur
Retourne SUCCESS_CODE si la fonction s'est déroulé correctement
Retourne EOF_CODE si la fin du fichier est atteinte
*/
int readByte(char* filePath, Position* pos, unsigned char* byte) {
	int fd, r;
	if ((fd = open(filePath, O_RDONLY)) == -1) return ERROR_CODE;
	if (lseek(fd, pos->byte, SEEK_SET) == -1) return ERROR_CODE;
	r = read(fd, byte, sizeof(char));
	if (r < 0) return ERROR_CODE;
	else if (r == 0) return EOF_CODE;
	if (close(fd) == -1) return ERROR_CODE;
	return SUCCESS_CODE;
}

/* Lit nb bits stockés dans t dans un fichier f */
int readBit(char* filePath, Position* pos, int nb, Byte* t) {
	unsigned char byte;
	int i, r;
	if (t == NULL) return ERROR_CODE;
	r = readByte(filePath, pos, &byte);
	if (r == ERROR_CODE) return ERROR_CODE;
	else if (r == EOF_CODE) return EOF_CODE;
	shiftToLeft(nb, t); /* ATTENTION AU SENS */
	for(i = nb - 1; i >= 0; i--) {
		t[i] = byte >> pos->bit & 1;
		if (pos->bit == 0) {
			pos->byte++;
			pos->bit = ARRAY_SIZE - 1;
			r = readByte(filePath, pos, &byte);
			if (r == ERROR_CODE) return ERROR_CODE;
			else if (r == EOF_CODE) return EOF_CODE;
		}
		else pos->bit--;
	}
	return SUCCESS_CODE;
}

int byteToInt(Byte* t) {
	int i;
	int eger = 0;
	for(i = ARRAY_SIZE - 1; i >= 0; i--)
		eger += t[ARRAY_SIZE - 1 - i] * (int)pow(2, (ARRAY_SIZE - 1 - i));
	return eger;
}

Tree* toTree(char* filePath, Byte* t, Position* pos, int cpt) {
	int i;
	int r = 0;
	Tree* bulle = newTree(NULL, cpt);
	r = readBit(filePath, pos, 1, t);
	if (r == ERROR_CODE) {
		printf("Impossible de lire le fichier\n");
		exit(EXIT_FAILURE);
	}
	if (r == EOF_CODE) return bulle;
	if (t[0] == 1) {
		cpt++;
		for(i = 0; i < 4; i++) {
			bulle->sons[i] = toTree(filePath, t, pos, cpt);
		}
	}
	else if (t[0] == 0) {
		Pixel* pix = newPixel();
		for(i = 0; i < 3; i++) {
			int tmp;
			r = readBit(filePath, pos, 8, t);
			if (r == ERROR_CODE) {
				printf("Impossible de lire le fichier\n");
				exit(EXIT_FAILURE);
			}
			if (r == EOF_CODE) return bulle;
			tmp = byteToInt(t);
			switch(i) {
				case 0 :
					pix->r = tmp;
					break;
				case 1 :
					pix->g = tmp;
					break;
				case 2 :
					pix->b = tmp;
					break;
			}
		}
		bulle->pixel = pix;
	}
	return bulle;
}

void toArray(Tree* t, Pixel pix[512][512], Point pt) {
	int i;
	int j;
	if(t->pixel != NULL) {
		#ifdef DEBUG
		printf("\t\t\t%d\n", t->level);
		#endif
		for(i = pt.y; i < pt.y + PICURE_SIZE / pow(2,t->level); i++)
			for(j = pt.x; j < pt.x + PICURE_SIZE / pow(2,t->level); j++) {
				pix[i][j].r = (t->pixel)->r;
				pix[i][j].g = (t->pixel)->g;
				pix[i][j].b = (t->pixel)->b;
			}
	}
	else {
		Point p;
		p.x = p.y = 0;
		#ifdef DEBUG
		printf("\t\t\t%d\n", t->level);
		#endif
		for(i = 0; i < 4; i++){
			int tmp = t->level + 1;
			switch(i){
				case 0 :
						p.y = pt.y;
						p.x = pt.x;
						#ifdef DEBUG
						printf("Traitement du fils 1\t x = %d\ty = %d", p.x, p.y);
						#endif
					break;
				case 1 :
						p.y = pt.y + PICURE_SIZE / (int)pow(2,tmp);
						p.x = pt.x;
						#ifdef DEBUG
						printf("Traitement du fils 1\t x = %d\ty = %d", p.x, p.y);
						#endif
					break;
				case 2 : 
						p.y = pt.y;
						p.x = pt.x + PICURE_SIZE / (int)pow(2,tmp);
						#ifdef DEBUG
						printf("Traitement du fils 1\t x = %d\ty = %d", p.x, p.y);
						#endif
					break;
				
				case 3 : 
						p.y = pt.y + PICURE_SIZE / (int)pow(2,tmp);
						p.x = pt.x + PICURE_SIZE / (int)pow(2,tmp);
						#ifdef DEBUG
						printf("Traitement du fils 1\t x = %d\ty = %d", p.x, p.y);
						#endif
					break;
			}
			toArray(t->sons[i], pix, p);
		}
	}
}

int toTGA(Tree* tree, char* filePath) {
	int i;
	int j;
	Point pt;
	Pixel pix[PICURE_SIZE][PICURE_SIZE];
	Pixel p;
	p.r = p.g = p.b = 0;
	for(i = 0; i < PICURE_SIZE; i++)
		for(j = 0; j < PICURE_SIZE; j++) pix[i][j] = p;
	pt.x = pt.y = 0;
	toArray(tree, pix, pt);
	return saveTGA(pix, filePath);
}


/* POINT D'ENTREE DU PROGRAMME */


int main(int argc, char** argv) {
	Byte* t = newByte();
	Position* pos = initPosition();
	Tree* tree = toTree(argv[1], t, pos, 0);
	if (toTGA(tree, argv[2]) == 0) return EXIT_FAILURE;
	free(t);
	free(pos);
	freeTree(tree);
	return EXIT_SUCCESS;
}
