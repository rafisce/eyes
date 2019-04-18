package com.eyes.eyes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class LastConnectedActivityTest {

    @Test
    public void collectUsers() { //סיפור 12

        Map<String, Object> user = new HashMap<String, Object>();
        ArrayList<LastConected> expected = new ArrayList<>();
        expected.add(new LastConected("avi", "avi@avi.com", "Apr 13,2019", "14/04/19 12:19", "common"));
        Map<String, Object> input = new HashMap<String, Object>();
        ArrayList<LastConected> output = new ArrayList<>();
        user.put("user_email", "avi@avi.com");
        user.put("user_type", "common");
        user.put("user_name", "avi");
        user.put("language", "eng");
        user.put("create_date", "Apr 13,2019");
        user.put("last_connected", "14/04/19 12:19");
        input.put("asdfgh", user);
        UsersActivity usersActivity = new UsersActivity();
        output = usersActivity.collectUsers(input);

        assertEquals(output.get(0).getType(),expected.get(0).getType());
        assertEquals(output.get(0).getEmail(),expected.get(0).getEmail());
        assertEquals(output.get(0).getName(),expected.get(0).getName());
        assertEquals(output.get(0).getJoined(),expected.get(0).getJoined());
        assertEquals(output.get(0).getTime(),expected.get(0).getTime());
    }
}