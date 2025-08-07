# BIBLIOTHEQUE D'ALLOCATION DYNAMIQUE DE MEMOIRE

L'archive contient :
* bam.h : la définition des fonctions de notre bibliothèque d'allocation dynamique.
* bam.c : l'implémentation des fonctions de notre bibliothèque d'allocation dynamique.
* malloc.c : la redirection des fonctions d'allocation standard vers les notres.
* main.c : un main permettant de tester notre programme.
* makefile : qui permet de compiler les sources. 

La bibliothèque d'allocation dynamique réalisée durant le projet implémente une version de 
malloc, calloc, realloc et free.
Elle comporte également une fonction d'affichage de l'état de la mémoire : display_memory().

Le principe de cette bibliothèque est de créer au lancement du programme un tableau de structures 
représentant un bloc mémoire. Ce genre de structure est composé de la taille du segment mémoire, 
d'un pointeur vers le début de ce segment et d'un pointeur vers la prochaine structure afin de 
pouvoir réaliser une liste chainée.
Par défaut, une structure ne dispose d'aucune mémoire allouée (aucun appel à sbrk).
Le tableau dont il est question nous fourni un certains nombre de structures qu'il sera possible 
d'utiliser dans le programme (étant donné qu'on ne peut utiliser malloc).

Nous disposons également de deux pointeurs vers ce type de structure représentant deux listes, 
l'une étant la liste des segments comportant de la mémoire allouée par sbrk et étant disponibles, 
l'autre étant la liste des segments alloués par l'une de nos fonctions malloc et free.

Il existe également quelques fonctions permettant de manipuler les structures dont notammennt une 
qui nous renvoi la première structure immédiatement disponible dans le tableau.

**MALLOC :**\
Cette fonction renvoie un bloc mémoire de la taille demandé par l'utilisateur.
Elle parcours la liste des blocs libres (avec une segment mémoire alloué par sbrk). Si aucun bloc ne 
se trouve dans la liste, alors un bloc est créé et un appel à sbrk est réalisé. Sinon 
le premier bloc dont la taille est supérieure ou égale à celle demandé par l'utilisateur est attrapé.
S'il n'en existe pas, un nouveau bloc est créé (appel à sbrk également, à chaque appel de sbrk, on 
pioche une structure dans le tableau).
Si le bloc est trop gros, il est découpé en deux bloc distinct. Le bloc qui sera renvoyé à l'utilisateur 
est alors placé dans la liste des blocs utilisés.
Afin de prouvé que malloc n'ititialise pas le bloc renvoyé, des valeurs aléatoires sont placé dans le 
segment pour chaque octet.
La fonction renvoie NULL en cas d'impossibilité d'allocation de mémoire.

**CALLOC :**\
Cette fonction encapsule en fait un malloc, la différence étant que la valeur 0 est placé dans chaque 
octet du bloc renvoyé afin de l'initialiser.

**FREE :**\
Cette fonction va libérer un bloc alloué par malloc ou calloc.
Elle vérifie d'abord que l'adresse de début de bloc passée en paramètre et correspondant au bloc à 
libérer existe dans la liste des bloc alloué.
Si elle n'existe pas, le programme se termine sur une erreur et affiche l'état de la mémoire.
Si l'adresse passée en paramètre est NULL alors rien ne se passe.
Si le bloc a bien été alloué par malloc ou calloc, alors free va essayer de recoller le bloc à libérer 
avec un bloc existant dans la liste des bloc libre.
Si cette opération est possible, alors on supprime la structure de la liste des blocs utilisés et on 
modifie les informations du bloc auquel on recolle le bloc (taille et éventuellement début du bloc).
Sinon, on déplace la structure de la liste des blocs utilisés vers la liste des blocs libres.

**REALLOC :**\
Cette fonction modifier la taille de l'espace mémoire alloué par un malloc.
Elle encapsule en fait un malloc et un free.
Elle fait d'abord appel à malloc afin de récupérer le nouvel espace mémoire de la taille demandé par 
l'utilisateur, copie ensuite les valeurs de l'ancien espace mémoire vers le nouveau et termine en 
faisant un free sur l'ancien espace mémoire avant de renvoyer l'adresse du nouveau.

Le makefile génère un exécutable permettant de tester plusieurs cas d'utilisation de ces fonctions : plusieurs 
allocations, quelques libération, des réallocations et la libération des derniers tableaux avant de 
se terminer sur une erreur en faisant un free sur un pointeur non alloué (pour tester la capacité de free 
à détecter ce genre de chose).
En déclarant la macro DEBUG, l'exécution affichera à chaque opération l'état de la mémoire gérer par notre 
bibliothèque.
Le makefile permet également d'utiliser notre bibliothèque par défaut pour les programmes dont nous n'avons 
pas les sources (comme il était demandé dans le sujet), il faut cependant mettre en commentaire les macros 
malloc, calloc, realloc et free dans bam.h.
