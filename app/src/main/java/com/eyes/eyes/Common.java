package com.eyes.eyes;

public class Common extends User
{
    public static final String TYPE = "common";

    public String getTYPE() {
        return TYPE;
    }

    public Common(String name,String email,String password,String language)
    {
        super(name,email,password,language);
    }
}
