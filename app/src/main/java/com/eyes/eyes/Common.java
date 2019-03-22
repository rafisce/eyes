package com.eyes.eyes;

public class Common extends User
{
    public static final String TYPE = "common";

    public static String getTYPE() {
        return TYPE;
    }

    public Common(String name,String email,String password,String language)
    {
        super(name,email,password,language);
    }
}
