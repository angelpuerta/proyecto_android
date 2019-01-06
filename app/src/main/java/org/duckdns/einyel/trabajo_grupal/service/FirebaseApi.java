package org.duckdns.einyel.trabajo_grupal.service;


import org.duckdns.einyel.trabajo_grupal.model.Comment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FirebaseApi {

    @GET("qr/checkCode/{id}/{code}")
    Call<Void> checkCode(@Path("id") Long eventid, @Path("code") String code);

    @POST("qr/addComment")
    @FormUrlEncoded
    Call<Void> addComment(@Field("comment") Comment comment, @Field("code") String code);

}
