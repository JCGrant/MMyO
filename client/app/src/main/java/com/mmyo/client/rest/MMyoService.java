package com.mmyo.client.rest;

import com.mmyo.client.Direction;
import com.thalmic.myo.Pose;

import retrofit.http.GET;
import retrofit.http.Path;

public interface MMyoService {
    @GET("/{pose}/{direction}")
    MMyoSpellResponse castSpell(@Path("pose") Pose pose, @Path("direction") Direction direction);
}
