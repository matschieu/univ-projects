Lecture d'un fichier image au format PCX
==============

**Author: Matschieu**

# Exécution

Depuis un terminal Linux, lancer la commande `make docker` qui se charge de démarrer un docker avec tout ce qu'il faut pour pouvoir compiler le code en 32 bit en utilisant la lib SDL1.2 (contraintes imposées par display.o).

Une fois le container démarré et le code compilé, il suffit de lancer la commande `./pcxreader` pour obtenir l'aide sur l'utilisation du programme.

Pour afficher une image PCX dans une fenêtre, il faut lancer la commande `./pcxreader pcx/<image>`. Des effets peuvent être appliqués à l'image en spécifiant les bons arguments à la commande.
Pour quitter la fenêtre, il faut appuyer sur "echap".

# Sujet

## Description 

Le format PCX est un format d'image qui était utilisé par le logiciel PaintBrush sous Dos. Ce format n'est plus utilisé de nos jours à cause de ses performances médiocres en terme de compression.

Dans un ordinateur, une image est représentée par un tableau de points appelés pixels. Plus la définition de l'image est élevée, et plus celle-ci contient de points. Une image en « 800x600 » contient par exemple 480,000 pixels. En pratique, le tableau de pixel est un tableau à une dimension comportant toutes les lignes de l'image les unes derrière les autres.

A l'écran, un pixel est composé d'une triade de trois composants électroluminescents : un rouge, un vert et un bleu. En faisant varier l'intensité de chacune de ces trois composantes élémentaires, on peut afficher toutes les couleurs du spectre visible. Un pixel peut donc être représenté dans la machine par trois octets donnant le niveau de chacune des trois composantes. Une image en « 800x600 » non compressée comporte donc 800*600*3 = 1,440,000 pixels.

Dans le format PCX qui nous concerne, un pixel est représenté sur un octet qui donne le numéro d'une couleur : on ne peut donc utiliser que 256 couleurs différentes. Les trois composantes de chaque couleur sont données par une palette de 728 octets se trouvant à la fin du fichier :

<table style="text-align: center">
    <tr>
      <td colspan="3">Couleur 0</th>
      <td colspan="3">Couleur 1</th>
      <td colspan="3">Couleur 2</th>
      <td colspan="3">...</th>
      <td colspan="3">Couleur 255</th>
    </tr>
    <tr>
      <td>R</td>
      <td>V</td>
      <td>B</td>
      <td>R</td>
      <td>V</td>
      <td>B</td>
      <td>R</td>
      <td>V</td>
      <td>B</td>
      <td> </td>
      <td> </td>
      <td> </td>
      <td>R</td>
      <td>V</td>
      <td>B</td>
    </tr>
</table>

Dans le tableau représentant la palette, on trouve les trois composantes de la couleur i aux indices 3i, 3i+1et 3i+2.

Pour représenter une image en « 800x600 » avec une palette, il faut donc stocker l'image d'une part et la palette d'autre part : 800*600 + 256*3 = 480,768 octets. Cette première « compression » réduit la taille de l'image par 3 environ mais entraîne une perte de qualité puisqu'il n'y a plus que 256 couleurs disponibles contre 16,777,216 auparavant.

Pour réduire encore la taille de l'image, le format PCX utilise sur les 480,000 octets représentant l'image une compression RLE « RunLenght Encoding ». Il s'agit d'une compression très simple à mettre en oeuvre : elle consiste à compter les octets sucessifs de même valeur, appelés « runs », comme on le ferait pour donner oralement une liste de nombres : pour dicter la liste « 45, 201, 201, 201, 123, 123, 123, 123, 123 » on dira « 45, 3 fois 201, 5 fois 123 ».

En pratique, la décompression d'un octet de l'image se passe de la manière suivante : on lit un octet. Si cet octet est une valeur strictement inférieure à 192, il s'agit d'un octet non compressé. En revanche, si la valeur est supérieure ou égale à 192, on lui soustrait 192 et on obtient le nombre de fois qu'il faudra répéter l'octet suivant.

Par exemple, la suite compressée d'octets suivante
« 23, 25, 198, 201, 195, 56 »
correspond à la suite décompressée
« 23, 25, 201, 201, 201, 201, 201, 201, 56, 56, 56 ».

Un fichier PCX est composé des éléments suivants :
* un « header » de 128 octets décrivant le fichier

| Byte | Item         | Size | Description / Comments |
|------|--------------|------|-------------------------|
| 0    | Manufacturer | 1    | Constant Flag, 10 = ZSoft .pcx |
| 1    | Version      | 1    | Version information : 0 = PC Paintbrush 2.5 ; 2 = 2.8 avec palette ; 3 = 2.8 sans palette ; 4 = Paintbrush Windows ; 5 = Paintbrush 3.0+ (inclut 24-bit PCX) |
| 2    | Encoding     | 1    | 1 = PCX run-length encoding |
| 3    | BitsPerPixel | 1    | Bits par pixel (par plan) : 1, 2, 4 ou 8 |
| 4    | Window       | 8    | Dimensions de l’image : Xmin, Ymin, Xmax, Ymax |
| 12   | HDpi         | 2    | Résolution horizontale en DPI |
| 14   | VDpi         | 2    | Résolution verticale en DPI |
| 16   | Colormap     | 48   | Palette de couleurs (voir texte) |
| 64   | Reserved     | 1    | Doit être mis à 0 |
| 65   | NPlanes      | 1    | Nombre de plans de couleur |
| 66   | BytesPerLine | 2    | Nombre d’octets par ligne et par plan (pair obligatoire) |
| 68   | PaletteInfo  | 2    | Interprétation de la palette : 1 = Couleur/NB, 2 = Niveaux de gris |
| 70   | HscreenSize  | 2    | Taille horizontale de l’écran (pixels) |
| 72   | VscreenSize  | 2    | Taille verticale de l’écran (pixels) |
| 74   | Filler       | 54   | Remplissage pour atteindre 128 octets (mettre à 0) |

* le raster : c'est la suite d'octets compressée représentant l'image
* un octet de valeur 12 (décimal)
* la palette (768 octets)

## Travail demandé

* récupérerer les arguments présents sur la ligne de commande (grâce aux paramètres argc et argv de votre main) afin de connaître le nom du fichier à ouvrir
* ouvrir le fichier
* récupérer l'header dans une structure pour l'analyser afin de ne traîter que les fichiers PCX d'un numéro de version égal à 5, encodés en RLE, avec un nombre de bits par pixels égal à 8 et un nombre de plans égal à 1
* récupérer les dimensions taillex et tailley de l'image
* récupérer la palette à la fin du fichier
* décoder le fichier dans un tableau de taille taillex*tailley*3 que vous aurez pris soin d'allouer dynamiquement (attention, les couleurs doivent être données dans l'ordre BVR)
* appeler l'affichage de l'image grâce à la méthode d'affichage présente dans display.o
* faire tout le nécessaire pour quitter le programme proprement...

## Elements fournis :

* un ensemble d'images servant de jeu de test
* un fichier header « display.h »
* une bibliothèque « display.o » contenant la fonction d'affichage : int display(int taillex, int tailley, char *buffer);

Liste (non exhaustive) des Fonctions C standart utiles :
printf : affichage
malloc, calloc : allocation dynamique de mémoire
free : libération de la mémoire
fopen : ouverture d'un fichier
fseek : déplacement dans un fichier
fread : lecture dans un fichier
fclose : fermeture d'un fichier
...

Vous trouverez la documentation de ces fonctions dans les pages man.

## Bonus !

Si vous le souhaitez, vous pouvez ajouter quelques fonctionnalités à votre lecteur d'image : afficher l'image en « noir et blanc » est par exemple très simple à réaliser avec une palette...
Voici le calcul permettant d’obtenir les nouvelles composantes d’une couleur :
R’ = (R+V+B) / 3
V’ = (R+V+B) / 3
B’ = (R+V+B) / 3
Pour savoir si vous devez réaliser un effet, servez vous des options de la ligne de commande.

## Indices :

* La taille de l'image est donnée par Xmax-Xmin+1 et Ymax-Ymin+1
* Un FILE* est un pointeur sur une structure permettant de gérer un fichier. Ne vous souciez pas de ce qui se trouve dans cette structure : vous devez seulement récupérer ce pointeur à l'ouverture du fichier et le passer en paramètres aux autres fonctions gérant les fichiers.
* Pour lire les informations de l'header, utilisez une structure !
* Pour quitter la fenêtre d'affichage, utilisez la touche « escape ».
* Pour compiler, vous utiliserez les lignes suivantes (si votre programme se nomme lecteur.c) :

```
gcc –o lecteur.o –c lecteur.c
gcc –o lecteur lecteur.o display.o `sdl-config --cflags –libs`
```
