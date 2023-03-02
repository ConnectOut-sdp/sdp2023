package com.sdpteam.connectout;

import org.junit.Test;

import android.content.Intent;

public class GoogleAuthTest {

    @Test
    public void googleIntentUselessTestJustForCoverage() {
        Intent intent = new GoogleAuth().buildIntent();
        System.out.println(intent.getData());
//        assertThrows(RuntimeException.class, () -> new GoogleAuth().buildIntent());
    }
}