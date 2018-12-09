package org.duckdns.einyel.trabajo_grupal.database.local;

import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class CommentsLocalRepo {

    private CommentDao commentDao;

    public CommentsLocalRepo(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public Observable<List<Comment>> commentsFromEvent(Long id) {
        return Observable.create(x -> commentDao.commentsFromEvent(id));
    }

    public void addComment(Comment comment) {
        commentDao.insert(comment);
    }

    public void addComments(List<Comment> comments) {
        commentDao.insertAll(comments);
    }

}
