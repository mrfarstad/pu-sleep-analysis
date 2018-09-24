# Sleep analysis
> Group 16 - TDT4140; sleep analysis

## About
In the subject Software Development TDT4140, is the task to create an 
infrastructure for Smart Life and Health. We have created an application to analyze sleep.
Sleep Analysis is an application for gathering data concerning health,
with a focus on sleep, but also including information on walking and heart rate.
We want to examine the correlation between these health aspects, and how sleep
can be improved.

This project consists of three main parts: The API, the client application and a data exporter.
The API is based on [GraphQL](http://graphql.org/), an API-level query language. It is implemented using
[graphql-java](https://github.com/graphql-java/graphql-java) together with [jetty://](https://www.eclipse.org/jetty/).
The client application is written in java, utilizing [JavaFX](https://en.wikipedia.org/wiki/JavaFX). The data exporter
is written in [golang](https://golang.org)

To store persistent data we use [MongoDB](https://www.mongodb.com/what-is-mongodb). For development
purposes it is possible to use it via a simple docker container.
We currently use (and provide docs with) `docker-compose` to do this as easy as possible.

## Getting started

### Services

The list of required services, and their setup can be found inside the
`docker-compose.yml`-file. This is currently only `mongodb`, used for storage.

```bash
$ docker-compose up -d
```

### Build project

Using the command line, the project can be built using maven:
```bash
$ mvn -f tdt4140-gr1816/pom.xml install
```

To build the docker image for production, use the following command (see Dockerfile for more information)
```
$ docker build -t gruppe16/gruppe16 .
```

### Run API

```bash
$ mvn -f tdt4140-gr1816/app.api/pom.xml jetty:run
```
When the API is running, open [http://localhost:8080](http://localhost:8080) to open the API explorer.
The API explorer allows the user to discover what the API provides, and works like simple sandbox for testing.
This makes the API more or less self explanatory.

When running in production, we recommend to use docker. An example setup can be found inside the
`production-example` folder.


### Build and run UI

```bash
$ mvn -f tdt4140-gr1816/app.ui/pom.xml clean compile assembly:single
$ cd tdt4140-gr1816/app.ui
$ java -jar target/tdt4140-gr1816.app.ui-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```


### Code formatting

We use [google-java-format](https://github.com/google/google-java-format) to format the source code.
There is a custom plugin for eclipse [here](https://github.com/google/google-java-format#eclipse)


To format the code, run the following command:
```bash
$ mvn fmt:format
```

To verify the code formatting and other metrics, we use maven:

```bash
$ mvn verify
```

# Data exporter
More information about the data exporter can be found insde the
`fitbit-exporter` folder.

## Dependencies

*  [Maven](https://maven.apache.org/what-is-maven.html)
*  [Docker](https://www.docker.com/what-docker)
*  [MongoDB](https://www.mongodb.com/what-is-mongodb)
*  [GraphQL](https://graphql.org/learn/)
*  [Jacoco](http://www.eclemma.org/jacoco/)
*  [Mockito](http://site.mockito.org/)
*  [JavaFX](https://docs.oracle.com/javafx/2/overview/jfxpub-overview.htm)
*  [JUnit](https://junit.org/junit5/)
*  [GraphiQL](https://github.com/graphql/graphiql)
*  [go-graphql-client (fork)](https://github.com/odinuge/go-graphql)

## Roles

- ___Scrum master___: Ole Kristian Vingdal
- ___Developer & QA Lead___: Martin Rebne Farstad
- ___Developer___: Anders Ottersland Granås
- ___Developer___: Sondre Grav Skjåstad
- ___Developer___: Ingeborg Sætersdal Sollid
- ___Developer___: Odin Ugedal
- ___Developer___: Mathias Wahl
