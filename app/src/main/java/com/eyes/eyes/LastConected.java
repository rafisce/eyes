package com.eyes.eyes;

public class LastConected {
    private String name;
    private String time;
    private String email;
    private String joined;
    private String type;
    private String uid;
    private String isActive;
    public String online;

    public LastConected(String nam,String tim,String isActive,String online,String uid)
    {
        name=nam;
        time=tim;
        this.isActive =isActive;
        this.uid = uid;
        this.online = online;
    }

    public LastConected(String name, String email, String joined, String time,String type,String isActive,String online,String uid){
        this.name = name;
        this.time = time;
        this.email = email;
        this.joined = joined;
        this.type = type;
        this.isActive =isActive;
        this.online = online;
        this.uid = uid;
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

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getUid() {
        return uid;
    }

    public String isActive() {
        return isActive;
    }

    public void setActive(String active) { isActive = active; }
}
