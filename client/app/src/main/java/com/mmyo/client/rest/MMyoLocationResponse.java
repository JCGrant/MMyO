package com.mmyo.client.rest;

public class MMyoLocationResponse implements Response {
    int id;
    Double longitude;
    Double latitude;

    public void displayInformation() {
        System.out.println(id + " " + longitude + " " + latitude);
    }
}
