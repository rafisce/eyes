package com.eyes.eyes;

public class LastConected {
    private String name;
    private String time;
    private String email;
    private String joined;
    private String type;

    public LastConected(String nam,String tim)
    {
        name=nam;
        time=tim;
    }

    public LastConected(String name, String email, String joined, String time,String type) {
        this.name = name;
        this.time = time;
        this.email = email;
        this.joined = joined;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public String getJoined() {
        return joined;
    }

    public String getName() {
        return name;
    }


    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }
}
