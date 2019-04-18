package com.eyes.eyes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ReportsActivityTest {

    @Test
    public void collectReports() { //סיפור 10
        Map<String, Object> report = new HashMap<String, Object>();
        ArrayList<Report> expected = new ArrayList<>();
        expected.add(new Report("avi", "1", "www.some.com", "14/04/19 12:19"));
        Map<String, Object> input = new HashMap<String, Object>();
        ArrayList<Report> output = new ArrayList<>();
        report.put("number", "1");
        report.put("user", "avi");
        report.put("uri", "www.some.com");
        report.put("record_date", "14/04/19 12:19");
        input.put("0", report);
        input.put("@","@");
        ReportsActivity reportsActivity = new ReportsActivity();
        output = reportsActivity.collectReports(input);

        assertEquals(output.get(0).date,expected.get(0).date);
        assertEquals(output.get(0).name,expected.get(0).name);
        assertEquals(output.get(0).number,expected.get(0).number);
        assertEquals(output.get(0).uri,expected.get(0).uri);
    }
}