package com.mmyo.client.rest;

import com.mmyo.client.Direction;
import com.mmyo.client.rest.response.MMyoLocationResponse;
import com.mmyo.client.rest.response.MMyoSpellResponse;
import com.thalmic.myo.Pose;

import retrofit.http.GET;
import retrofit.http.Path;

public interface MMyoService {

    @GET("/")
    String hello();

    @GET("/{id}/{pose}/{direction}")
    MMyoSpellResponse castSpell(
            @Path("id") int id,
            @Path("pose") Pose pose,
            @Path("direction") Direction direction);

    @GET("/{id}/{longitude}/{latitude}")
    MMyoLocationResponse sendLocation(
            @Path("id") int id,
            @Path("longitude") Double longitude,
            @Path("latitude") Double latitude);
}
