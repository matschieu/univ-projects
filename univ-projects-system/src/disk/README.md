Gestion de disque dur
==============

**Author: Matschieu**

# TRAVAIL REALISE

* Bibliothèque inode (inode.h & inode.c)
* Les fichiers if_*.c sont fournis par l'enseignant mais s'appuient sur la 
bibliothèque "inode" et permettent donc de la tester
* Les autres fichiers proviennent des tps précédents

# INFO

Le disque virtuel fourni contient une partition prête à être utilisée !
Le script autotest permet de tester les commandes if_nfile, if_cfile, if_pfile 
et if_dfile en affichant après l'exécution de chaque commande l'état de la 
partition (dfs).

# EXECUTABLE

* `if_nfile`\
Créer un nouveau fichier sur le volume courant (variable d'environnement 
$CURRENT_VOLUME) et affiche son inode.
* `if_cfile`\
Copie le fichier dont l'inode est passée en paramètre et affiche l'inode
du nouveau fichier créé.
* `if_dfile`\
Supprime le fichier dont l'inode est passée en paramètre.
* `if_pfile`\
Affiche le contenu du fichier dont l'inode est passée en paramètre.


# AUTRES EXECUTABLES

* `mknfs`\
Ce programme initialise le super bloc de la partition dont le numéro se trouve 
dans la variable d'environnement $CURRENT_VOLUME.
* `dfs`\
Ce programme affiche les tailles de chaque partitions du disque + le taux 
d'occupation de la partition dont le numéro se trouve dans $CURRENT_VOLUME.
* `test_bloc`\
Ce programme test l'allocation et la libération des bloc sur le volume dont 
le numéro se trouve dans $CURRENT_VOLUME en effectuant les opérations suivantes :
	* affichage de l'occupation du volume
	* allocation de tous les blocs libres
	* affichage de l'occupation du volume
	* libération d'un nombre aléatoire de blocs (blocs aléatoires également)
	* allocation de tous les blocs libres
Pour vérifier le bon fonctionnement de la bibliothèque, il faut utiliser deux fois
ce programme à la suite afin de vérifier que l'allocation se fait bien sur des 
blocs non contigus.
* `mkhd`\
L'exécution de ce programme créer deux nouveaux disque dur virtuels.
* `frmt`\
Ce programme prend un paramètre optionnel qui est la valeur (nombre entier) 
qui servira au formatage du disque. L'ensemble du disque est formaté
Exemple : "./frmt" ou "./frmt 5"
* `dmps`\
Ce programme prend en paramètres deux entiers représentant respectivement le
numéro du cylindre et le numéro du secteur à afficher.
L'affichage produit affiche le contenu du secteur sous forme numérique et 
sous forme de caractères. 
Exemple : "./dmps 0 1"
* Ces 3 programmes se terminent sur une erreur si l'une des opérations qu'ils 
effectuent sur le disque échouent ou si l'accès à un numéro de cylindre/secteur
inexistant est effectué.
* `vm`\
Ce programme lance un gestionnaire de volume permettant de créer, supprimer
et lister les partitions du disque maître. La sauvegarde du MBR permet de 
sauvegarder définitivement les modifications effectuées sur le disque.
Une fois le programme lancé, la commande help permet d'afficher les commandes
disponibles.

# MAKEFILE

* `make`\
compile l'ensemble des exécutables puis nettoie tous les fichiers .o (appel à make clean).
* `make clean`\
permet de nettoyer les fichiers .o.
* `make cleanexec`\
permet de nettoyer tous les exécutables et fichiers .o
* `make realclean`\
permet de nettoyer l'ensemble des fichiers .o, les exécutables et les fichiers représentant les disques durs créés par l'exécution des programmes.
