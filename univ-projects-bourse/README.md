Information exchange website
==============

**Author: Matschieu**

# Project description

The goal of this project is to implements an information exchange website using a database.

You can read the [project report](doc/rapport.md) in French.

Note: It has been refactored to use Maven and Docker.

# Running the project

## Build

* Build app using Maven:
```
mvn clean install
```

* Buildthe docker image:
```
docker compose build
```

* Running the app using Docker:
```
docker build up
```

Then the app is reachable in a web browser at the URL `http://localhost/Ident`