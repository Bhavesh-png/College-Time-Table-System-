package com.college.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private String secret;

    public User(String username, String password, String secret) {
        this.username = username;
        this.password = password;
        this.secret = secret;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
}
