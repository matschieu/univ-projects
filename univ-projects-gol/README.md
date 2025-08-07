Graphical game of life
==============

**Author: Matschieu**

# Project description

The goal of this project is to implement a graphical window to run a game of life.

It has been refactored to use Maven.

# Running the project

* Build app using Maven:
```
mvn clean install
```

* Running the app using Maven:
```
mvn exec:java -Dexec.mainClass="com.github.matschieu.gol.GameOfLife"
```

* Running the app using java command (after having copied dependency in target folder with Maven):
```
mvn dependency:copy-dependencies
java -cp target/classes/:target/dependency/* com.github.matschieu.gol.GameOfLife
```

* Or simply execute the built executable jar located in target 
```
java -jar target/univ-projects-gol-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```
