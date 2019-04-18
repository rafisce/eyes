package com.eyes.eyes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DestinationsActivityTest {

    @Test
    public void collectDestinations() {//סיפור 11

        ArrayList<Destination> expected = new ArrayList<>();
        expected.add(new Destination("blue","0"));
        ArrayList<String> input = new ArrayList<>();
        input.add("blue");
        ArrayList<Destination> output ;

        DestinationsActivity destinationsActivity = new DestinationsActivity();
        output = destinationsActivity.collectDestinations(input);

        assertEquals(output.get(0).getDest(),expected.get(0).getDest());
        assertEquals(output.get(0).getNum(),expected.get(0).getNum());



    }
}