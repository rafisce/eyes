package com.eyes.eyes;

public class Report {

    String name;
    String number;
    String uri;
    String date;

    public Report(String name, String number, String uri, String date) {
        this.name = name;
        this.number = number;
        this.uri = uri;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getUri() {
        return uri;
    }

    public String getDate() {
        return date;
    }
}
