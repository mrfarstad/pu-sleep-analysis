### Lag en bruker i GraphiQL:

```
mutation createUser {
  createUser(
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

### Logg inn med brukeren:

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

### Slett brukeren:

```
mutation {
  deleteUser(auth: {
    username: "test"
    password: "test"
  })
}
```
