package com.mmyo.client.rest;

import com.mmyo.client.Direction;
import com.thalmic.myo.Pose;

public class MMyoSpellResponse implements Response {
    int id;
    Pose pose;
    Direction direction;

    public void displayInformation() {
        System.out.println(id + " " + pose + " " + direction);
    }
}
