package com.eyes.eyes;

public class Admin extends User{
    public static final String TYPE = "admin";

    public static String getTYPE() {
        return TYPE;
    }

    public Admin(String name,String email,String password,String language)
    {
        super(name,email,password,language);
    }


}
