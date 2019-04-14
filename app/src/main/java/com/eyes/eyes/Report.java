package com.eyes.eyes;

public class Report {

    String name;
    String time;
    String uri;

    public Report(String name, String time, String uri) {
        this.name = name;
        this.uri = uri;
        this.time = time;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
