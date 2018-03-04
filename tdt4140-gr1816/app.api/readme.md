mutation createUser {
	createUser(
    id: "1"
    authProvider: {
      username:"gruppe16"
      password: "gruppe16"
    }
    isDoctor: false
    gender: "apache helicopter"
    age: 22
  ) {
    id
    name
  }
}

mutation signIn {
  signinUser(auth: {
    username: "gruppe16"
    password: "gruppe16"
  }) {
    token
    user {
      id
      name
    }
  }
}
