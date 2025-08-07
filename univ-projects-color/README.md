GUI to optimize the color choice
==============

**Author: Matschieu**

# Project description

The goal of this project is to display a GUI to optimize the color choice.

You can read the [project report](doc/rapport.md) in French.

Note: It has been refactored to use Maven.

# Running the project

* Build app using Maven:
```
mvn clean install
```

* Running the app using Maven:
```
mvn exec:java -Dexec.mainClass="com.github.matschieu.color.selector.GoodColorSelector"
```

* Running the app using java command:
```
java -cp target/classes/ com.github.matschieu.color.selector.GoodColorSelector
```

* Or simply execute the built executable jar located in target 
```
java -jar target/univ-projects-color-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```
