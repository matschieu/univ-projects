FTP server and client
==============

**Author: Matschieu**

# Project description

The goal of this project is to implement a FTP server and its both CLI and web client.

Note: It has been refactored to use Maven.

# Running the project

## Build

* Build app using Maven:
```
mvn clean install
```

## Running the server

* Running the app using Maven:
```
mvn exec:java -Dexec.mainClass="com.github.matschieu.ftp.server.FTPServer" -Dexec.args="<host> <port>"
```

* Running the app using java command (after having copied dependency in target folder with Maven):
```
java -cp target/classes/ com.github.matschieu.ftp.server.FTPServer <host> <port>
```

## Running the CLI client

* Running the app using Maven:
```
mvn exec:java -Dexec.mainClass="com.github.matschieu.ftp.client.cli.FTPClient" -Dexec.args="<host> <port>"
```

* Running the app using java command (after having copied dependency in target folder with Maven):
```
java -cp target/classes/ com.github.matschieu.ftp.client.cli.FTPClient <host> <port>
```

## Running the web client using Docker

```
docker build . -t ftpweb
docker run --rm -p 80:8080 ftpweb
```

Then the application is available at `http://localhost`. See below for more information (in French).

# Project description

## Fichiers

**Scripts :**
* client.sh : script qui compile et exécute le client (par defaut sur la 
machine local, port 5287)
* server.sh : script qui compile et exécute le serveur (sur la machine local,
 port 5287 avec comme racine /home)

**Packages**
* ftp.server contient les classes relatives au serveur
* ftp.client.cli contient les classes relatives au client en ligne de commande
* ftp.client.web contient les classes relatives au client web (servlet)

**Classes Java :**
* FTPClient.java : client ftp
* FTPRequest.java : contient des constantes pour les commandes existantes
* FTPRequestProcess.java : traitement de chaque commande
* FTPServer.java : serveur ftp
* FTPServerMessage.java : contient tous les messages envoyés par le serveur

## Fonctionnement du serveur

Le serveur utilise 2 threads au lancement : l'un attend des connexions, l'autre
permet d'entrer des commandes pour intéragir avec le serveur.
Lorsqu'un client se connecte, un nouveau thread est créé pour recevoir et 
exécuter les commandes du client.

Le serveur utilise un canal de contrôle pour recevoir des commandes et quelques 
réponses et un canal de données pour le transfert de fichier conformément à la 
RFC959 (FTP).
La manière dont les fichiers sont transférés (flux de données) n'est cependant 
pas conforme à la RFC : un tag est envoyé au client, le client répond lorsqu'il
est prêt à recevoir et enfin le serveur envoi le fichier. Ceci permet d'éviter 
que le serveur ne ferme le canal avant que le client lise les données.
Pour éviter que le programme client ne se bloque, un timeout a été ajouté au 
canal de données, si au bout d'un certains temps le client n'ouvre pas le canal
de données, le serveur arrête d'attendre l'ouverture. Ainsi il est possible 
d'utiliser telnet comme client bien qu'il n'ait pas de canal de donnée, il sera
juste impossible de recevoir/envoyer des fichiers (ou d'obtenir la réponse du 
LIST).

Le serveur respecte la RFC 959 (FTP) également pour tous les codes de retour.

Chaque commande reçue est analysée via une expression régulière pour vérifier
sa validité et ainsi sélectionner la réponser envoyée au client.

Pour obtenir la liste des commandes possibles pour intéragir avec le serveur, 
il faut entrer la commande "help".

## fonctionnement du client

Le client a 2 threads, l'un qui écoute en permanence des réponses du serveur
et un autre qui attend des commandes de l'utilisateurs et les envoie au 
serveur.
Lorsque la réponse du serveur est un tag `<LIST>`, `<RETR>` ou `<STOR>`, le canal 
de données est ouvert pour transférer un fichier ou une réponse sur ce canal.

## Liste des commandes implémentées par le serveur

* `USER` : "mathieu" ou "anonymous"
* `PASS` : "1234" pour "mathieu"
* `LIST`
* `RETR`
* `STOR`
* `QUIT`
* `PWD`
* `CWD`
* `CDUP`
* `HELP` : pour obtenir l'aide sur les commandes

# Fonctionnement du client FTP web par servlet

Pour simplifier la compréhension du code des servlets et générer une source HTML
propre, 2 objets sont dédiés à la génération de la page HTML :
* HTMLPage qui permet d'ajouter des headers/footer à la page et ajouter des 
lignes
* FTPClientHTMLPage qui étend HTMLPage et qui permet de construire les pages 
HTML du client FTP. On peut ainsi ajouter du texte aux messages d'information,
au corps de la page, à l'historique... avant de pouvoir construire de manière
définitive la page en écrivant le code HTML dans le flux.

Cette méthode permet d'avoir des servlet ne contenant aucun code HTML, de générer
des sources HTML propres (bien indentées) mais aussi de réutiliser des morceaux
de code (HTML ou non) commun à plusieurs pages ce qui facilite également la 
maintenance.

Le rendu visuel (obtenu via une feuille de style CSS) permet à l'utilisateur de
naviguer simplement dans les dossiers et de visualiser les fichiers. Toutes les
erreurs sont gérées et un message d'erreur est affichée dès qu'une erreur survient.

Enfin, aucune modification du serveur n'a du être opérée pour faire fonctionner 
ce client sous forme de servlet. Les seules modifications résultent d'une 
volonté d'améliorer le code du serveur (par exemple afficher les dossiers puis les 
fichiers, le tout dans l'ordre alphabétique lors d'un LIST).

Pour utiliser le client (depuis le navigateur web) : `http://localhost`
Les valeurs à saisir ensuite dans le cas ou tout est en local sont :
* serveur = adresse IP de la machine (obtennue avec ifconfig sur Linux) sur laquelle tourne le serveur (différente de localhost lorsqu'exécuté sur Docker)
* port = 5287
* ident = "mathieu" ou "anonymous"
* MDP = "1234" ou aucun

