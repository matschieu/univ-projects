# Adventure game in a terminal

The goal of this project is to run an adventure game in CLI displayed inside a terminal

You can read the [project report](doc/rapport.md) in French.

Note: It has been refactored to use Maven.

## Running the project

* Build app using Maven:
```
mvn clean install
```

* Running the app using Maven:
```
mvn exec:java -Dexec.mainClass="com.github.matschieu.Run"
```
