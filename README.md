# Gruppe 16 - TDT4140
> Programvareutvikling

### About
In the subject Software Development TDT4140, is the task to create an 
infrastructure for Smart Life and Health. We have created an application to analyze sleep.
Sleep Analysis is an application for gathering data concerning health,
with a focus on sleep, but also including information on walking and heart rate.
We want to examine the correlation between these health aspects, and how sleep
can be improved.

### Database

To start mongodb
```bash
$ docker-compose up -d
```

### Build project

```
$ mvn -f tdt4140-gr1816/pom.xml install
```

### Run API

```bash

$ mvn -f tdt4140-gr1816/app.api/pom.xml jetty:run
```
Then open http://localhost:8080


### Build and run UI

```bash
$ mvn -f tdt4140-gr1816/app.ui/pom.xml clean compile assembly:single
$ cd tdt4140-gr1816/app.ui
$ java -jar target/tdt4140-gr1816.app.ui-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```


### Code formatting

We use [google-java-format](https://github.com/google/google-java-format) to format the source code.
There is a custom plugin for eclipse [here](https://github.com/google/google-java-format#eclipse)

```bash
$ mvn verify # Verify your code
$ mvn com.coveo:fmt-maven-plugin:format # This will format the code
```
##Links

Maven
https://maven.apache.org/what-is-maven.html

Docker
https://www.docker.com/what-docker

MongoDB
https://www.mongodb.com/what-is-mongodb

GraphQL
https://graphql.org/learn/

Jacoco
http://www.eclemma.org/jacoco/

Mockito
http://site.mockito.org/

JavaFX
https://docs.oracle.com/javafx/2/overview/jfxpub-overview.htm

JUnit
https://junit.org/junit5/

## Roles

- ___Scrum master___: Ole Kristian Vingdal
- ___QA Lead___: Martin Rebne Farstad
- ___Developer___: Anders Ottersland Granås
- ___Developer___: Sondre Grav Skjåstad
- ___Developer___: Ingeborg Sætersdal Sollid
- ___Developer___: Odin Ugedal
- ___Developer___: Mathias Wahl
