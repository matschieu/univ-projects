
********************
*    Matschieu     *
********************


# FICHIERS

**Scripts :**
* client.sh => script qui compile et exécute le client (par defaut sur la 
machine local, port 5287)
* server.sh => script qui compile et exécute le serveur (sur la machine local,
 port 5287 avec comme racine /home)

**Classes Java :**
* FTPClient.java => client ftp
* FTPRequest.java => contient des constantes pour les commandes existantes
* FTPRequestProcess.java => traitement de chaque commande
* FTPServer.java => serveur ftp
* FTPServerMessage.java => contient tous les messages envoyés par le serveur

# FONCTIONNEMENT DU SERVEUR

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

* Pour compiler le server :
`javac -d classes -sourcepath src src/ftp/server/FTPServer.java`

* Pour lancer le serveur (par défaut sur le port 5287 avec /home comme racine) : 
	* `java -cp classes ftp.server.FTPServer` est équivalent à `./server.sh`
	* `java -cp classes ftp.server.FTPServer <racine> <port>` est équivalent à `./server.sh <racine> <port>`

# FONCTIONNEMENT DU CLIENT **


Le client a 2 threads, l'un qui écoute en permanence des réponses du serveur
et un autre qui attend des commandes de l'utilisateurs et les envoie au 
serveur.
Lorsque la réponse du serveur est un tag `<LIST>`, `<RETR>` ou `<STOR>`, le canal 
de données est ouvert pour transférer un fichier ou une réponse sur ce canal.

* Pour compiler le client :
`javac -d classes -sourcepath src src/ftp/client/FTPClient.java`

* Pour lancer le client (par défaut sur localhost:5287) :
	* `java -cp classes ftp.client.FTPClient` est équivalent à `./client.sh`
	* `java -cp classes ftp.client.FTPClient <serveur> <port>` est équivalent à `./client.sh <server> <port>`

# LIST DES COMMANDES IMPLEMENTEES PAR LE SERVEUR

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

