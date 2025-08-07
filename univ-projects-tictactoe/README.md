Tic Tac Toe game
==============

**Author: Matschieu**

# Project description

The goal of this project is to implements the game Tic Tac Toe.

Note: It has been refactored to use Maven.

# Running the project

* Build app using Maven:
```
mvn clean install
```

* Running the app using Maven:
```
mvn exec:java -Dexec.mainClass="com.github.matschieu.tictactoe.Game" -Dexec.args="-1"
```

* Running the app using java command:
```
java -cp target/classes com.github.matschieu.tictactoe.Game <-1|-2>
```
