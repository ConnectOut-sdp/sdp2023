package com.sdpteam.connectout.utils;

import org.junit.Rule;
import org.junit.Test;

import android.content.Context;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

public class MyListViewTest {
    @Rule
    public ActivityScenarioRule<TestActivity> testRule = new ActivityScenarioRule<>(TestActivity.class);

    @Test
    public void constructor1Works() {
        testRule.getScenario().onActivity(activity -> {
            Context context = activity.getBaseContext();
            MyListView myListViewTest = new MyListView(context, null, 0);
            myListViewTest.onMeasure(0, 0);
        });
    }

    @Test
    public void constructor2Works() {
        testRule.getScenario().onActivity(activity -> {
            Context context = activity.getBaseContext();
            MyListView myListViewTest = new MyListView(context, null);
            myListViewTest.onMeasure(0, 0);
        });
    }

    @Test
    public void constructor3Works() {
        testRule.getScenario().onActivity(activity -> {
            Context context = activity.getBaseContext();
            MyListView myListViewTest = new MyListView(context);
            myListViewTest.onMeasure(0, 0);
        });
    }
}
