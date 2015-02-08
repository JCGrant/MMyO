package com.mmyo.client.rest.response;

public class MMyoLocationResponse implements Response {
    int id;
    Double longitude;
    Double latitude;

    public void displayInformation() {
        System.out.println(id + " " + longitude + " " + latitude);
    }
}
