# GUI based on SCXML state chart

The goal of this project is to display a GUI using statecharts implemented using SCXML.

You can read the [project report](doc/rapport.md) in French.

Note: It has been refactored to use Maven.

## Running the project

* Build app using Maven:
```
mvn clean install
```

* Running the app using Maven:
```
mvn exec:java -Dexec.mainClass="com.github.matschieu.scxml.RobotExec"
```

* Or simply execute the built executable jar located in target 
```
java -jar target/univ-projects-scxml-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```
