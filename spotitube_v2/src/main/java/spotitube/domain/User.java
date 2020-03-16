package spotitube.domain;

import java.util.Random;

public class User {
    private String user;
    private String password;
    private String token;

    public String getUser() {
        return user;
    }

    public void setUsername(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        if (token == null) {
            token = tokenGenerator();
        }
        return token;
    }

    private String tokenGenerator() {
        final int AMOUNT_OF_TOKEN_SEGMENTS = 3;
        String token = "";
        for (int i= 0; i < AMOUNT_OF_TOKEN_SEGMENTS; i++) {
            Random rand = new Random();
            int n = rand.nextInt(9999) + 1000;
            token += "-" + n;
        }
        token = token.substring(1, token.length());
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
