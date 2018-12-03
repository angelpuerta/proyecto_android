package org.duckdns.einyel.trabajo_grupal.database;

import android.util.Log;

import org.duckdns.einyel.trabajo_grupal.database.local.CommentsLocalRepo;
import org.duckdns.einyel.trabajo_grupal.database.remote.CommentsRemoteRepo;
import org.duckdns.einyel.trabajo_grupal.model.Comment;

import java.io.IOException;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CommentsRepoImpl {

    CommentsLocalRepo localRepo;

    CommentsRemoteRepo remoteRepo;

    public CommentsRepoImpl(CommentsLocalRepo commentsLocalRepo, CommentsRemoteRepo commentsRemoteRepo) {
        this.localRepo = commentsLocalRepo;
        this.remoteRepo = commentsRemoteRepo;
    }

    public Flowable<List<Comment>> commentsFromEvent(Long id) {
        return Flowable.merge(remoteRepo.commentsFromEvent(id).subscribeOn(Schedulers.io())
                        .doOnNext(comments -> {
                            localRepo.addComments(comments);
                        }),
                localRepo.commentsFromEvent(id).subscribeOn(Schedulers.io()));
    }

    public void addComment(Comment comment) {
        try {
            remoteRepo.addComment(comment).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CommentsRemoteRepo getRemoteRepo() {
        return remoteRepo;
    }

    public CommentsLocalRepo getLocalRepo() {
        return localRepo;
    }


}
