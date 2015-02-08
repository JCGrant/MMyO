package com.mmyo.client.rest;

import com.mmyo.client.Direction;
import com.mmyo.client.rest.response.Response;
import com.thalmic.myo.Pose;

public class TestClient {

    public static void main(String[] args) {

        MMyoService service = MMyoServiceConstructor.construct();

        Response r = service.castSpell(0, Pose.FIST, Direction.LEFT);
        r.displayInformation();

        r = service.sendLocation(0, 34.54, 23.53);
        r.displayInformation();
    }
}
