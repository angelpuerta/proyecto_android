package org.duckdns.einyel.trabajo_grupal.database.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.duckdns.einyel.trabajo_grupal.model.Comment;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Call;

public class CommentsRemoteRepo extends BaseRemote {

    public Flowable<List<Comment>> commentsFromEvent(Long event) {
        return create(RemoteComments.class, "http://einyel.duckdns.org/android/").commentsFromEvent(event);
    }

    public Call<Void> addComment(Comment comment) {
        return create(RemoteComments.class, "http://einyel.duckdns.org/android/").addComment(comment);
    }

}
