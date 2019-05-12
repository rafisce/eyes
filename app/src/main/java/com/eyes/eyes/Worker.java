package com.eyes.eyes;

import com.eyes.eyes.User;

public class Worker extends User {
    public static final String TYPE = "worker";

    public String getTYPE() {
        return TYPE;
    }

    public Worker(String name,String email,String password,String language)
    {
        super(name,email,password,language);
    }
}
