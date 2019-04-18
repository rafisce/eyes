package com.eyes.eyes;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    @Test
    public void checkAdmin() {//סיפור 2

        String input = "admin";
        LoginActivity loginActivity = new LoginActivity();
        assertTrue(loginActivity.checkAdmin(input));
    }
}