Simple talk server and client using TCP protocol
==============

**Author: Matschieu**

# Project description

The goal of this project is to implements a simple server allowing clients to connect and send/receive messages.

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
mvn exec:java -Dexec.mainClass="com.github.matschieu.chat.TalkServer"
```

* Running the app using java command:
```
java -cp target/classes/ com.github.matschieu.chat.TalkServer
```

## Running the client

* Running the app using Maven:
```
mvn exec:java -Dexec.mainClass="com.github.matschieu.chat.TalkClient"
```

* Running the app using java command:
```
java -cp target/classes/ com.github.matschieu.chat.TalkClient
```
