package org.duckdns.einyel.trabajo_grupal.database;

import android.util.Log;

import org.duckdns.einyel.trabajo_grupal.database.local.CommentsLocalRepo;
import org.duckdns.einyel.trabajo_grupal.database.remote.CommentsRemoteRepo;
import org.duckdns.einyel.trabajo_grupal.model.Comment;

import java.io.IOException;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsRepoImpl {

    private CommentsLocalRepo localRepo;

    private CommentsRemoteRepo remoteRepo;

    public CommentsRepoImpl(CommentsLocalRepo commentsLocalRepo, CommentsRemoteRepo commentsRemoteRepo) {
        this.localRepo = commentsLocalRepo;
        this.remoteRepo = commentsRemoteRepo;
    }

    public Flowable<List<Comment>> commentsFromEvent(Long id) {
        return Flowable.merge(remoteRepo.commentsFromEvent(id).subscribeOn(Schedulers.io())
                        .doOnNext(comments ->
                                localRepo.addComments(comments)
                        ),
                localRepo.commentsFromEvent(id).subscribeOn(Schedulers.io()));
    }

    public void addComment(Comment comment) {
        remoteRepo.addComment(comment).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    new Runnable() {

                        @Override
                        public void run() {
                            localRepo.addComment(comment);
                        }
                    };
                    Log.i(getClass().getName(), "Añadido " + comment.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                Log.e(getClass().getName(), "Error on comment " + comment.toString());

            }
        });
    }

    public CommentsRemoteRepo getRemoteRepo() {
        return remoteRepo;
    }

    public CommentsLocalRepo getLocalRepo() {
        return localRepo;
    }


}
