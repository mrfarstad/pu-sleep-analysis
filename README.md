# Gruppe 16 - TDT4140
> Programvareutvikling

### About

Sleep Analysis is an application for gathering data concerning health,
with a focus on sleep, but also including information on walking and heart rate.
We want to examine the correlation between these health aspects, and how sleep
can be improved.

### Database


To start mongodb
```bash
$ docker-compose up -d
```

### Run API

```bash
cd tdt4140-gr1816/app.api
mvn jetty:run
```

Then open http://localhost:8080

### Code formatting

We use [google-java-format](https://github.com/google/google-java-format) to format the source code.
There is a custom plugin for eclipse [here](https://github.com/google/google-java-format#eclipse)

```bash
$ mvn verify # Verify your code
$ mvn com.coveo:fmt-maven-plugin:format # This will format the code
```


## Roles

- ___Scrum master___: Ole Kristian Vingdal
- ___QA Lead___: Martin Rebne Farstad
- ___Developer___: Anders Ottersland Granås
- ___Developer___: Sondre Grav Skjåstad
- ___Developer___: Ingeborg Sætersdal Sollid
- ___Developer___: Odin Ugedal
- ___Developer___: Mathias Wahl
