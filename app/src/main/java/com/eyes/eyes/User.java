package com.eyes.eyes;

public class User {

    private String name;
    private String Email;
    private String password;
    private String language;

    public  User(){

    }

    public User(String name, String email, String password, String language) {
        this.name = name;
        this.Email = email;
        this.password = password;
        this.language = language;
    }
    public User(String name, String email, String password) {
        this.name = name;
        this.Email = email;
        this.password = password;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


/*
package com.eyes.eyes;

public class User {

    private String name;
    private String Email;
    private String password;
    private String language;

    public  User(){

    }

    public User(String name, String email, String password, String language) {
        this.name = name;
        this.Email = email;
        this.password = password;
        this.language = language;
    }
    public User(String name, String email, String password) {
        this.name = name;
        this.Email = email;
        this.password = password;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

 */