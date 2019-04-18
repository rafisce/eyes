package com.eyes.eyes;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Test
    public void checkDest() { //סיפור 4
        String input = "some destination";
        MainActivity mainActivity = new MainActivity();
        assertTrue(mainActivity.checkDest(input));
    }

    //
    @Test
    public void changeLanguage() {//סיפור 5
        String input1 = "eng";
        String input2 = "heb";
        String expected1 = "heb";
        String expected2 = "eng";

        MainActivity mainActivity = new MainActivity();
        assertEquals(mainActivity.changeLanguage(input1),expected1);
        assertEquals(mainActivity.changeLanguage(input2),expected2);
    }
}