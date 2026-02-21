/* @author Matschieu */

#include "pcxr.h"
#include <stdlib.h>

// Applique un effet négatif à l'image
void effetNegatif(PCXPalette* palette) {
	int i;
	for(i = 0; i < TAILLE_PALETTE; i++) {
		palette[i].vert = 256 - palette[i].vert;
		palette[i].rouge = 256 - palette[i].rouge;
		palette[i].bleu = 256 - palette[i].bleu;
	}
}

// Applique un effet noir et blanc à l'image
void effetNoir(PCXPalette* palette) {
	int i;
	for(i = 0; i < TAILLE_PALETTE; i++) {
		palette[i].bleu = palette[i].vert = palette[i].rouge = (palette[i].vert + palette[i].rouge + palette[i].bleu) / 3;
	}
}

// Fait ressortir le rouge de l'image
void effetRougeGris(PCXPalette* palette) {
	char temp;
	int i;
	double tauxRouge;
	for(i = 0; i < TAILLE_PALETTE; i++) {
		tauxRouge = (double)palette[i].rouge / (palette[i].rouge + palette[i].vert + palette[i].bleu);
		if (tauxRouge < 0.375) {
			palette[i].vert = palette[i].rouge = palette[i].bleu = (palette[i].bleu + palette[i].rouge + palette[i].vert) / 3;
		}
	}
}

// Fait ressortir le vert de l'image
void effetVertGris(PCXPalette* palette) {
	char temp;
	int i;
	double tauxVert;
	for(i = 0; i < TAILLE_PALETTE; i++) {
		tauxVert = (double)palette[i].vert / (palette[i].rouge + palette[i].vert + palette[i].bleu);
		if (tauxVert < 0.375) {
			palette[i].vert = palette[i].rouge = palette[i].bleu = (palette[i].bleu + palette[i].rouge + palette[i].vert) / 3;
		}
	}
}

// Fait ressortir le bleu de l'image
void effetBleuGris(PCXPalette* palette) {
	char temp;
	int i;
	double tauxBleu;
	for(i = 0; i < TAILLE_PALETTE; i++) {
		tauxBleu = (double)palette[i].bleu / (palette[i].rouge + palette[i].vert + palette[i].bleu);
		if (tauxBleu < 0.375) {
			palette[i].vert = palette[i].rouge = palette[i].bleu = (palette[i].bleu + palette[i].rouge + palette[i].vert) / 3;
		}
	}
}

// Remplace le rouge fort par du bleu
void rougeEnBleu(PCXPalette* palette) {
	char temp;
	int i;
	double tauxRouge;
	for(i = 0; i < TAILLE_PALETTE; i++) {
		tauxRouge = (double)palette[i].rouge / (palette[i].rouge + palette[i].vert + palette[i].bleu);
		if (tauxRouge > 0.345235) {
			temp = palette[i].bleu;
			palette[i].bleu = palette[i].rouge;
			palette[i].rouge = palette[i].vert;
			palette[i].vert = temp;
		}
	}	
}

// Inverse l'image
void inversion(unsigned char* buffer, int tailleImage) {
	char temp;
	int i, j, n;
	n = tailleImage * 3;
	for(i = 0; i < n / 2; i += 3)
		for(j = 0; j < 3; j++) {
			temp = *(buffer + i + j);
			*(buffer + i + j) = *(buffer + n - (3 - j) - i);
			*(buffer + n - (3 - j) - i) = temp;
		}
}

// Applique un effet de neige à l'image
void effetNeige(unsigned char* buffer, int tailleImage) {
	int i;
	char couleur;
	for(i = 0; i < tailleImage * 3; i += 3)
		if (rand() % 10 == 1)  {
			couleur = rand() % 50 + 205;
			*(buffer + i) = *(buffer + i + 1) = *(buffer + i + 2) = couleur; 
		}

}

// Applique un effet de saturation à l'image
void effetSaturation(PCXPalette* palette) {
	int i;
	for(i = 0; i < TAILLE_PALETTE; i++) {
		palette[i].vert = (palette[i].vert + palette[i].rouge + palette[i].bleu);
		palette[i].rouge = (palette[i].vert + palette[i].rouge + palette[i].bleu);
		palette[i].bleu = (palette[i].vert + palette[i].rouge + palette[i].bleu);
	}
}

// Applique un effet gris-blanc à l'image
void effetGrisBlanc(PCXPalette* palette) {
	int i;
	for(i = 0; i < TAILLE_PALETTE; i++) {
		palette[i].bleu = palette[i].vert = palette[i].rouge = (palette[i].vert + palette[i].rouge + palette[i].bleu);
	}
}

// Mélange les couleurs de l'image
void melangeCouleurs(PCXPalette* palette) {
	int i, alea;
	PCXPalette temp;
	for(i = 0; i < TAILLE_PALETTE; i++) {
		alea = rand() % TAILLE_PALETTE;
		temp = palette[alea];
		palette[alea] = palette[i];
		palette[i] = temp;
	}
}

// Agrandit l'image
unsigned char* agrandir(unsigned char* buffer, int ratio, int* tailleX, int* tailleY, int* tailleImage) {
	unsigned char* bufferTemp;
	int i, j, k;
	int idx1, idx2, idx3;
	*tailleX *= ratio;
	*tailleY *= ratio;
	*tailleImage = *tailleX * *tailleY;
	bufferTemp = (unsigned char*)calloc(*tailleX * *tailleY * 3, sizeof(char));
	idx1 = idx2 = idx3 = 0;
	while(idx1 < *tailleX * *tailleY * 3) {
		for(i = 1; i < 1 + ratio; i++) {
			for(j = 0; j < *tailleX * 3 / ratio; j += 3){
				for(k = 0; k < ratio; k++) {
					*(bufferTemp + idx1 + k * 3 + 0) = *(buffer + idx2);
					*(bufferTemp + idx1 + k * 3 + 1) = *(buffer + idx2 + 1);
					*(bufferTemp + idx1 + k * 3 + 2) = *(buffer + idx2 + 2);
				}
				idx1 += 3 * ratio;
				idx2 += 3;
			}
			if (i % ratio != 0) idx2 = idx3;
		}
		idx3 = idx2;
	}
	free(buffer);
	return bufferTemp;
}

/*
// Agrandit l'image (x2)
unsigned char* agrandir(unsigned char* buffer, int* tailleX, int* tailleY) {
	unsigned char* bufferTemp;
	int i, j;
	int idx1, idx2, idx3;
	*tailleX *= 2;
	*tailleY *= 2;
	bufferTemp = (unsigned char*)calloc(*tailleX * *tailleY * 3, sizeof(char));
	idx1 = idx2 = idx3 = 0;
	while(idx1 < *tailleX * *tailleY * 3) {
		for(i = 1; i < 3; i++) {
			for(j = 0; j < *tailleX * 3 / 2; j += 3){
				*(bufferTemp + idx1 + 0) = *(bufferTemp + idx1 + 3) = *(buffer + idx2);
				*(bufferTemp + idx1 + 1) = *(bufferTemp + idx1 + 4) = *(buffer + idx2 + 1);
				*(bufferTemp + idx1 + 2) = *(bufferTemp + idx1 + 5) = *(buffer + idx2 + 2);
				idx1 += 6;
				idx2 += 3;
			}
			if (i % 2 == 1) idx2 = idx3;
		}
		idx3 = idx2;
	}
	free(buffer);
	return bufferTemp;
	
}
*/
