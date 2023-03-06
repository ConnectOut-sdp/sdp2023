package com.sdpteam.connectout.event;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;


public class EventBuilderTest {

    private static EventBuilder testEventBuilder = (new EventBuilder()).setTitle("Test event").setLat(37.7749).setLng(-122.4194).setDescription("This is a test event.");

    @Test
    public void buildRetunNonNullWithEmptyConstructor() {
        EventBuilder builder = new EventBuilder();
        builder.setTitle("Test event");
        builder.setLat(37.7749);
        builder.setLng(-122.4194);
        builder.setDescription("This is a test event.");
        Event event = builder.build();
        assertNotNull(event);
    }

    @Test
    public void EmptyConstructorIsNotNull() {
        assertNotNull(new EventBuilder());
    }

    @Test
    public void setTitleCorrectlyAfterBuild() {
        EventBuilder builder = new EventBuilder();
        builder.setTitle("Test event");
        Event event = builder.build();
        assertThat(event.getTitle(), is("Test event"));
    }

    @Test
    public void setLatitudeCorrectlyAfterBuild() {
        EventBuilder builder = new EventBuilder();
        builder.setLat(37.7749);
        Event event = builder.build();
        assertThat(event.getLat(), is(37.7749));
    }

    @Test
    public void setLongitudeCorrectlyAfterBuild() {
        EventBuilder builder = new EventBuilder();
        builder.setLng(-122.4194);
        Event event = builder.build();
        assertThat(event.getLng(), is(-122.4194));
    }

    @Test
    public void setDescriptionCorrectlyAfterBuild() {
        EventBuilder builder = new EventBuilder();
        builder.setDescription("This is a test event.");
        Event event = builder.build();
        assertThat(event.getDescription(), is("This is a test event."));
    }

    @Test
    public void buildReturnNonNullWithParamConstructor() {
        LatLng latLng = new LatLng(37.7749, -122.4194);
        EventBuilder builder = new EventBuilder(latLng);
        builder.setTitle("Test event");
        builder.setDescription("This is a test event.");
        Event event = builder.build();
        assertNotNull(event);
    }

    @Test
    public void setTitleReturnSameBuilder() {
        EventBuilder builder = new EventBuilder();
        assertSame(builder, builder.setTitle("Test event"));

    }

    @Test
    public void setLatReturnSameBuilder() {
        EventBuilder builder = new EventBuilder();
        assertSame(builder, builder.setLat(37.7749));
    }

    @Test
    public void setLongReturnSameBuilder() {
        EventBuilder builder = new EventBuilder();
        assertSame(builder, builder.setLng(-122.4194));
    }

    @Test
    public void setDescriptionReturnSameBuilder() {
        EventBuilder builder = new EventBuilder();
        assertSame(builder, builder.setDescription("This is a test event."));
    }

    @Test
    public void getTitleReturnExpectedString() {
        assertThat(testEventBuilder.getTitle(), is("Test event"));

    }

    @Test
    public void getDescriptionReturnExpectedString() {
        assertThat(testEventBuilder.getDescription(), is("This is a test event."));

    }

    @Test
    public void getLatReturnExpectedDouble() {
        assertThat(testEventBuilder.getLat(), is(37.7749));
    }

    @Test
    public void getLngReturnExpectedDouble() {
        assertThat(testEventBuilder.getLng(), is(-122.4194));
    }

}