package com.eyes.eyes;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    @Test
    public void checkAdmin() {//סיפור 2

        String input = "d67e348477968bde";
        LoginActivity loginActivity = new LoginActivity();
        assertTrue(loginActivity.checkAdmin(input));
    }

    @Test
    public void checkCommon() {
        String input = "a20fa2868b84c751";
        LoginActivity loginActivity = new LoginActivity();
        assertTrue(loginActivity.checkCommon(input));
    }

    @Test
    public void checkWorker() {
        String input = "13e87047f7db51b2";
        LoginActivity loginActivity = new LoginActivity();
        assertTrue(loginActivity.checkWorker(input));
    }

    @Test
    public void checkActive() {
        LoginActivity loginActivity = new LoginActivity();
        assertTrue(loginActivity.checkActive("true"));
    }

    @Test
    public void checkNotActive() {
        LoginActivity loginActivity = new LoginActivity();
        assertTrue(loginActivity.checkNotActive("false"));
    }

    @Test
    public void desCheck() {
    }
}