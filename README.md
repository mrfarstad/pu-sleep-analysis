# Gruppe 16 - TDT4140
> Programvareutvikling

### About

TBA 🎓

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

Lag en bruker i GraphiQL:

```
mutation createUser {
  createUser(
    id: "1337"
    name: "test"
    authProvider: {
      username: "test",
      password: "test"
    },
    isDoctor: true,
    gender: "male"
    age: 22,
    ) {

    	username
  }
}
```

Logg inn med brukeren:

```
mutation {
  signinUser(auth: {
    username: "test"
    password: "test"
  }) {
    token
  }
}
```
just werkz^{tm}

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
