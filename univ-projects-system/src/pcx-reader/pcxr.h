/*
Projet d'ASR2, semestre 2
Matschieu
DUT informatique, 1ère annee, groupe E
*/

#define TAILLE_PALETTE 256

// D�claration de strcutures
typedef struct {
	unsigned char manufacturer;
	unsigned char version;
	unsigned char encoding;
	unsigned char bpp;
	short xMin;
	short yMin;
	short xMax;
	short yMax;
	short hdpi;
	short vdpi;
	unsigned char colorMap[48];
	unsigned char reserved;
	unsigned char nbPlanes;
	short bpl;
	short paletteInfo;
	short hScreenSize;
	short vScreenSize;
	unsigned char filler[54];
} PCXHeader;
typedef struct {
	unsigned char rouge;
	unsigned char vert;
	unsigned char bleu;
} PCXPalette;

// D�claration de fonctions
void effetNegatif(PCXPalette* palette);
void effetNoir(PCXPalette* palette);
void effetRougeGris(PCXPalette* palette);
void effetVertGris(PCXPalette* palette);
void effetBleuGris(PCXPalette* palette);
void rougeEnBleu(PCXPalette* palette);
void inversion(unsigned char* buffer, int tailleImage);
void effetNeige(unsigned char* buffer, int tailleImage);
void effetSaturation(PCXPalette* palette);
void effetGrisBlanc(PCXPalette* palette);
void melangeCouleurs(PCXPalette* palette);
unsigned char* agrandir(unsigned char* buffer, int ratio, int* tailleX, int* tailleY, int* tailleImage);
