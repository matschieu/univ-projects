/*
Projet d'ASR2, semestre 2
Matschieu
DUT informatique, 1ère année, groupe E
*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <time.h>
#include "pcxr.h"
#include "display.h"

// D�claration de fonctions
int verifierParametres(int argc, char** argv);
unsigned char* decompresser(FILE* image, int tailleImage, PCXPalette* imagePalette);
unsigned char getOctet(FILE* image);
void afficherAide();

// Programme principal
int main(int argc, char** argv) {

	FILE* image;
	PCXHeader imageHeader;
	PCXPalette imagePalette[TAILLE_PALETTE];
	int tailleImage, hauteur, largeur, i;
	unsigned char* buffer;

	srand(time(NULL));
	//system("clear");
	if ((argc == 1) || (strcmp(argv[1], "-aide") == 0)) {
		afficherAide();
		return 0;
	}

	// Ouverture de l'image
	image = fopen(argv[1], "r");
	if (image == NULL) {
		printf("\nImpossible d'ouvrir le fichier\n\n");
		return 2;
	}

	// V�rification de la validit� des param�tres
	if (verifierParametres(argc, argv) == 1) return 1;
	
	// Lecture du header
	if (fread(&imageHeader, sizeof(PCXHeader), 1, image) == -1) return 1;
	
	//V�rification de la version du fichier PCX
	if ((imageHeader.version != 5) || (imageHeader.encoding != 1)) {
		printf("\nFormat PCX incorrect, seul le format PCX version 5 avec RLE est prit en charge\n\n");
		return 2;
	}
	
	// R�cup�re les dimensions de l'image
	largeur = (int)(imageHeader.xMax + 1) - (int)(imageHeader.xMin);
	hauteur = (int)(imageHeader.yMax + 1) - (int)(imageHeader.yMin);
	tailleImage = largeur * hauteur;
	
	// Lecture de la palette
	if (fseek(image, - (TAILLE_PALETTE * sizeof(PCXPalette)), SEEK_END) == -1 || fread(&imagePalette, TAILLE_PALETTE * sizeof(PCXPalette), 1, image) == -1) return 1;
		
	// D�termine les effets de couleurs � appliquer
	for(i = 2; i < argc; i++) {
		if (strcmp(argv[i], "-aide") == 0) afficherAide();
		if (strcmp(argv[i], "-neg") == 0) effetNegatif(imagePalette);
		if (strcmp(argv[i], "-noir") == 0) effetNoir(imagePalette);
		if (strcmp(argv[i], "-rouge") == 0) effetRougeGris(imagePalette);
		if (strcmp(argv[i], "-bleu") == 0) effetBleuGris(imagePalette);
		if (strcmp(argv[i], "-vert") == 0) effetVertGris(imagePalette);
		if (strcmp(argv[i], "-renb") == 0) rougeEnBleu(imagePalette);
		if (strcmp(argv[i], "-sat") == 0) effetSaturation(imagePalette);
		if (strcmp(argv[i], "-grib") == 0) effetGrisBlanc(imagePalette);
		if (strcmp(argv[i], "-melc") == 0) melangeCouleurs(imagePalette);
	}

	// Lecture du raster
	buffer = decompresser(image, tailleImage, imagePalette);

	// D�termine les effets � appliquer sur le raster
	for(i = 2; i < argc; i++) {
		if (strcmp(argv[i], "-inv") == 0) inversion(buffer, tailleImage);
		if (strcmp(argv[i], "-neige") == 0) effetNeige(buffer, tailleImage);
		if (strcmp(argv[i], "-zoom2") == 0) buffer = agrandir(buffer, 2, &largeur, &hauteur, &tailleImage);
		if (strcmp(argv[i], "-zoom3") == 0) buffer = agrandir(buffer, 3, &largeur, &hauteur, &tailleImage);
	}
		
	// affichage de l'image
	//display(largeur, hauteur, buffer);
	free(buffer);
	
	// Fermeture de l'image
	fclose(image);

	return 0;

}

// V�rification des param�tres de la ligne de commande
int verifierParametres(int argc, char** argv) {
	int i, j, paramValide, cmdValide;
	char* arguments[15] = {"-aide", "-neg", "-noir", "-gris", "-rouge", "-bleu", "-vert", "-renb", "-sat", "-grib", "-melc", "-inv", "-neige", "-zoom2", "-zoom3"};
	cmdValide = 0;
	for(j = 2; j < argc; j++) {
		paramValide = 0;
		for(i = 0; i < 15; i++)
			if (strcmp(argv[j], arguments[i]) == 0) { 
				paramValide = 1;
				break;
			}
		if (paramValide == 0) {
			cmdValide = 1;
			printf("\nErreur, %s : parametre inconnu", argv[j]);
		}
	}
	if (cmdValide == 1) {
		printf("\n");
		return 1;
	}
	return 0;
}

// D�compression du raster
unsigned char* decompresser(FILE* image, int tailleImage, PCXPalette* imagePalette) {
	unsigned char temp;
	int nbRepetition;
	int nbPixelRecup;
	int nbOctetRecup;
	int i;
	unsigned char* buffer;
	(fseek(image, sizeof(PCXHeader), SEEK_SET) == -1);
	buffer = (unsigned char*)calloc(tailleImage * 3, sizeof(char));
	nbPixelRecup = nbOctetRecup = 0;
	while(nbPixelRecup < tailleImage) {
		temp = getOctet(image);
		nbRepetition = (int)temp;
		if (nbRepetition > 192) {
			nbRepetition -= 192;
			temp = getOctet(image);
		}
		else nbRepetition = 1;
			for (i = 0; i < nbRepetition; i++) {
				*(buffer + nbOctetRecup + 0) = imagePalette[temp].bleu;
				*(buffer + nbOctetRecup + 1) = imagePalette[temp].vert;
				*(buffer + nbOctetRecup + 2) = imagePalette[temp].rouge;
				nbOctetRecup += 3;
				nbPixelRecup++;
			}
	}
	return buffer;
}

// Lit et renvoie 1 octet dans le raster
unsigned char getOctet(FILE* image) {
	unsigned char c;
	if(fread(&c, sizeof(char), 1, image) == -1) return 1;
	return c;
}

// Affiche l'aide
void afficherAide() {
	printf("\n\t*** LECTEUR D'IMAGE PCX ***\n");
	printf("\nNOM :\n");
	printf("\tpcxr (PCX Reader)\n");
	printf("\nSYNTAXE :\n");
	printf("\tpcxr [nom de fichier] [options...]\n");
	printf("\nDESCRIPTION :\n");
	printf("\tpcxr permet d'ouvrir une image au format PCX (version 5 avec RLE) dans \n");
	printf("\tune fenetre et de passer des options en parametre pour appliquer des \n");
	printf("\teffets (de couleur ou de forme) a l'image.\n");
	printf("\nOPTIONS :\n");
	printf("\t-aide : affiche l'aide\n");
	printf("\t* Effets utiles :\n");
	printf("\t-neg : applique un effet negatif a l'image\n");
	printf("\t-noir : applique un effet noir et blanc a l'image\n");
	printf("\t-rouge : fait ressortir le rouge de l'image\n");
	printf("\t-vert : fait ressortir le vert de l'image\n");
	printf("\t-bleu : fait ressortir le bleu de l'image\n");
	printf("\t-remb : remplace le rouge fort par du bleu\n");
	printf("\t-inv : inverse l'image (rotation de 180 degres)\n");
	printf("\t-zoom2 : Augmente la taille de l'image par 2\n");
	printf("\t-zoom3 : Augmente la taille de l'image par 3\n");
	printf("\t-neige : applique un effet de neige a l'image\n");
	printf("\t* Effets inutiles :\n");
	printf("\t-sat : applique un effet de saturation a l'image\n");
	printf("\t-grib : applique un effet gris-blanc bizarre a l'image\n");
	printf("\t-melc : melange les couleurs de l'image\n");
	printf("\nINFORMATIONS :\n");
	printf("\tAppuyez sur \"Echap\" pour quitter le lecteur\n");
	printf("\nAUTEUR :\n");	
	printf("\tMathieu D., 2007\n");
	printf("\n");
}
