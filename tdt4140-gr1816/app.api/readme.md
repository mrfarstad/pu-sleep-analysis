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

mutation {
  signinUser(auth: {
    username: "test"
    password: "test"
  }) {
    token
  }
}
