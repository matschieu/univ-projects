Analyse d'arguments et traitement d'image TGA
==============

**Author: Matschieu**

# MAKEFILE

* `make`\
compile l'ensemble des exécutables puis nettoie tous les fichiers .o (appel à make clean).
Attention, il est nécessaire d'avoir la lib de développement X11 installée.
* `make clean`\
permet de nettoyer les fichiers .o.
* `make realclean`\
permet de nettoyer l'ensemble des fichiers .o et les exécutables.
* `make docker`\
démarre une image Docker qui compile le pogramme.
* `make docker-clean`\
permet de nettoyer l'image Docker.

# Exécution

Afficher l'aide :

```
./tgareader -h
```

Afficher une image dans une fenêtre :

```
./tgareader -d -i img/lena.tga
```

Afficher une image dans une fenêtre avec un effet négatif :

```
./tgareader -d -i img/lena.tga -n
```

Copie une image avec un effet négatif dans un autre fichier :

```
./tgareader -d -i img/lena.tga -o img/lena_negatif.tga -n
```

# Sujet - Partie 1

## Analyse des arguments en ligne de commande

Le but de ce TP est de créer une bibliothèque réutilisable d’analyse des arguments que l’on peut passer en ligne de commande à un programme.
Il existe des conventions respectées par de nombreux programmes concernant la syntaxe des arguments de ligne de commande. Par exemple :

* les options peuvent prendre deux forme : une forme courte avec un trait d’union et un caractère (par ex. : `-v`) et une forme longue avec deux traits d’union et un mot
(par ex. : `--verbose`).
* l’option `-h` / `--help` permet d’afficher un message d’aide sur les différentes options possibles pour le programme.
* certains arguments peuvent prendre un paramètre de type entier, flottant ou chaı̂ne de caractères. On peut séparer le nom de l’option de son paramètre par un caractère espace ou un caractère =.

## Codage

On peut penser à décrire chaque argument possible dans une structure, que l’on passe en paramètre à une fonction qui sera chargée d’analyser le contenu de la variable argv récupérée dans la fonction `main()`.
Pour chaque argument, la structure décrira :
* le nom court de l’option,
* le nom long de l’option,
* le type de paramètre attendu après l’option,
* l’emplacement en mémoire où la fonction d’analyse pourra placer la valeur du paramètre,
* un message d’aide concernant l’option,
* un message d’aide concernant le paramètre de l’option.

L’énumération suivante permet d’indiquer quel est le type du paramètre d’une option (aucun paramètre, chaı̂ne de caractères, entier, flottant) :

```
enum ParameterType
{
PARAM NONE, PARAM STRING, PARAM LONG,
PARAM DOUBLE
};
```

On utilisera la structure suivante pour décrire les options :

```
struct Option
{
char shortName ;
char *longName ;
ParameterType parameterType ;
void *parameterPtr ;
char *description ;
char *parameterDescription ;
};
```

Si l’option ne prend pas de paramètre, la variable `parameterPtr` doit pointer vers une variable booléenne qui sera modifiée pour indiquer si l’option concernée est apparue ou non sur la ligne de commande.
La fonction `analyzeCommandLineArguments()` devra réaliser l’analyse complète des arguments de la ligne de commande. Elle devra avoir le prototype suivant :

```
void analyzeCommandLineArguments(struct
Option[] options, int optionsSize, int argc,
char** argv) ;
```

Votre bibliothèque devra avoir les fonctionnalités suivantes :
* Les options peuvent apparaı̂tre dans n’importe quel ordre sur la ligne de commande. Ainsi, on peut appeler un programme avec les options `-c -v` ou avec les options `-v -c` et attendre le même fonctionnement.
* L’option `-h` / `--help` est implicite : on ne doit pas la décrire dans le tableau d’options. Si elle apparaı̂t dans la liste des options, elle doit afficher un message constitué des différentes options et de leur message d’aide associé (option et paramètre) puis le programme doit se terminer automatiquement.
* Si une erreur d’analyse des arguments apparaı̂t, une description de l’erreur doit être affichée puis le programme doit se terminer en rappelant d’utiliser `--help` pour obtenir de l’aide sur la manière d’appeler le programme.
* Quand des options courtes sont utilisées, on peut les concaténer : `-cv` est équivalent à `-c -v`. Si une des options attend un paramètre, elle devra se trouver en dernière
position. C’est par exemple ce que l’on trouve quand on utilise `tar` ainsi : `tar -cvzf fichier.tar.gz ...`.

# Sujet - Partie 2

## Traitement d’images

Une image peut être vue comme un tableau à deux dimensions contenant des pixels (les points de l’image). Un pixel est un triplet contenant les valeurs entières attribuées aux composantes rouge, vert et bleu. En associant les composantes, on obtient la couleur réelle d’un point de l’image.
On peut utiliser la structure suivante pour représenter un pixel avec 8 bits pour chaque composante (24 bits par pixel) :

```
struct Pixel {
	unsigned char r, g, b ;
};
```

Une image peut être représentée par la structure suivante :

```
struct Image {
unsigned int width ;
unsigned int height ;
Pixel* pixels ;
};
```

Il existe de nombreux formats de fichiers permettant de mémoriser une image sur le disque. Le format TGA est un format simple dans lequel on trouve les valeurs des composantes des pixels côte à côte dans le fichier si on choisit de ne pas compresser l’image. Un fichier TGA est un fichier binaire qui commence par un en-tête de 18 octets décrivant le reste du fichier.
L’en-tête a la forme suivante :
* octet 1 : la taille de la zone ”image ID” dans la suite du fichier (peut être égale à zéro),
* octet 2 : indique l’existence ou non d’une table de couleurs dans la suite du fichier (0 pour aucune table et 1 pour l’utilisation d’une table). Une table de couleurs permet de définir un tableau avec l’ensemble des couleurs utilisées dans l’image et ensuite de ne plus rappeler toutes les composantes d’un pixel mais uniquement le numéro de la couleur dans la table.
* octet 3 : indique le type d’image. Ce champs doit être égal à 2 pour avoir une image non compressée avec des pixels à 3 composantes (True Color).
* octets 4-8 : description du format de la table de couleurs. Si on n’utilise pas de table de couleurs, ces 5 octets doivent être égaux à 0.
* octets 9-10 : origine en X de l’image si elle doit être affichée sur un terminal avec une origine en bas à gauche de l’écran. Souvent initialisés à 0.
* octets 11-12 : origine en Y de l’image si elle doit être affichée sur un terminal avec une origine en bas à gauche de l’écran. Souvent initialisés à 0.
* octets 13-14 : largeur de l’image en pixels. L’octet de poids faible est l’octet 13.
* octets 15-16 : hauteur de l’image en pixels. L’octet de poids faible est l’octet 15.
* octet 17 : nombre de bits par pixel (8, 16, 24 ou 32). Dans une image avec 32 bits par pixel, on a les 3 composantes rouge, vert et bleu sur 8 bits plus 8 bits pour représenter la transparence du pixel (souvent appelée canal alpha).
* octet 18 : descripteur d’image :
** bits 0-3 : bits non utilisés pour notre application. Peuvent être initialisés à 0.
** bit 4 : indique l’ordre des colonnes de l’image dans le fichier. 0 pour gauche à droite et 1 pour droite à gauche.
** bit 5 : indique l’ordre des lignes de l’image dans le fichier. 0 pour bas en haut et 1 pour haut en bas.
** bits 6-7 : initialisés à 0.

Si on n’utilise pas la zone Image ID, qu’on n’a pas de table de couleurs et qu’on n’utilise aucune extension du format TGA, les données des pixels se trouvent directement après les 18 octets de l’en-tête.
Vous trouverez sur le Campus la spécification complète du format de fichier TGA.

## Codage

Vous devez écrire un programme qui permet de :
* Ouvrir un fichier TGA et créer une représentation de l’image en mémoire. Utilisez un éditeur hexadécimal (`hexedit` ou `okteta` par exemple) pour analyser l’en-tête des fichiers. Attention, les composantes des pixels sont données dans l’ordre bleu, vert, rouge dans les fichiers.
* Appliquer un flou sur l’image en moyennant chaque pixel avec ses quatre voisins directs. Attention, il est nécessaire de placer les nouveaux pixels dans une seconde image pour ne pas perdre les valeurs des pixels de l’image initiale.
* Afficher une image dans une fenêtre en utilisant les fonctions des fichiers `window.h` et `window.c` qui vous sont fournis (inspirez vous de l’exemple joint avec les deux fichiers).
* Sauvegarder une image en mémoire dans un fichier au format TGA.

Vous utiliserez votre bibliothèque d’analyse des arguments en ligne de commande pour traiter
les options suivantes :
* `-i` / `--input` pour spécifier le nom du fichier d’entrée. Cette option est obligatoire.
* `-o` / `--output` pour spécifier le nom du fichier de sortie. Cette option est facultative. Si elle n’est pas présente, l’image de sortie n’est pas sauvegardée.
* `-v` / `--verbose` pour que le programme affiche un message à chaque étape de son exécution (ouverture de l’image d’entrée, application du filtre, écriture de l’image de sortie, ...). Sans cette option, le programme doit rester muet.
* `-b` / `--blur` pour que le programme applique un flou sur l’image d’entrée.
* `-d` / `--display` pour afficher l’image de sortie et attendre que l’utilisateur appuie sur une touche avant de terminer le programme.

Vous pouvez coder d’autres traitements sur l’image si vous le souhaitez. Vous ajouterez des options en ligne de commande qui permettent de les lancer. On peut penser à chaı̂ner plusieurs traitements dans l’ordre où ils apparaissent sur la ligne de commande. Vous pouvez pour cela vous inspirer du fonctionnement du programme convert inclus dans la bibliothèque ImageMagick.

## Accès aux pixels

Dans la structure Image, le tableau de pixels n’a pas été déclaré comme un tableau à deux dimensions car cela nous aurait obligé à déclarer au moins une des deux dimensions dans le code. Nous avons donc déclaré le tableau de pixels comme un tableau à une dimension. Le pixel (i, j) de l’image se trouvera dans la case d’indice `i * width + j` de la variable `pixels`.
Pour ne pas utiliser cette formule partout dans le programme, vous pouvez créer une fonction `getPixel()` et une procédure `setPixel()` avec les prototypes suivants :

```
Pixel* getPixel(Image* image, unsigned int i,
unsigned int j) ;
void setPixel(Image* image, unsigned int i,
unsigned int j, unsigned char r, unsigned
char g, unsigned char b) ;
```

Vous utiliserez ces fonctions à chaque fois que vous devez accéder à un pixel de l’image.

## Lecture / écriture des fichiers

Vous utiliserez la fonction `open()` (et pas `fopen()`) pour ouvrir les fichiers en lecture ou écriture. Vous utiliserez la fonction `read()` pour lire le contenu des fichiers et la fonction `write()` pour l’écriture.