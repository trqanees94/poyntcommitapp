package com.example.tariq.githubcommitapp;

import android.widget.TableLayout;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {


    @Test
    public void isStringEmpty() {
        MainActivity mainActivity = new MainActivity();
        boolean actual = mainActivity.isStringEmpty("");
        boolean expexted = true;
        assertEquals(expexted, actual);
    }

    @Test
    public void callGithubAPI() {
        MainActivity mainActivity = new MainActivity();
        String[][] actual = mainActivity.callGithubAPI("");
        String[][] expexted = null;
        assertEquals(expexted, actual);
    }


}

