package tdt4140.gr1816.app.api.auth;

public class AuthData {

    private String username;
    private String password;

    public AuthData() {
    }

    public AuthData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}