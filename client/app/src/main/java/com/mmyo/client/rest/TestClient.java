package com.mmyo.client.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.mmyo.client.Direction;
import com.thalmic.myo.Pose;

import java.util.Date;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class TestClient {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.5.172:5000")
                .setConverter(new GsonConverter(gson))
                .build();

        MMyoService service = restAdapter.create(MMyoService.class);

        Response r = service.castSpell(0, Pose.FIST, Direction.LEFT);
        r.displayInformation();

        r = service.sendLocation(0, 34.54, 23.53);
        r.displayInformation();
    }
}
