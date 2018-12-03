package org.duckdns.einyel.trabajo_grupal.database.remote;

import org.duckdns.einyel.trabajo_grupal.model.Comment;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RemoteComments {

    @GET("comments/{event}")
    Flowable<List<Comment>> commentsFromEvent(@Path("event") Long event);

    @POST("comments")
    Call<Void> addComment(@Body Comment comment);

}
