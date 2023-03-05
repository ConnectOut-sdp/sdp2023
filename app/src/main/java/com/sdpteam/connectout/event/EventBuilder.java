package com.sdpteam.connectout.event;

public class EventBuilder {

    public EventBuilder() {
    }

    public EventBuilder(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    private String title;
    private double lat;
    private double lng;
    private String description;

    public Event build() {
        return new Event(title, lat, lng, description);
    }

    public String getTitle() {
        return title;
    }

    public EventBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public EventBuilder setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EventBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public EventBuilder setLng(double lng) {
        this.lng = lng;
        return this;
    }
}

