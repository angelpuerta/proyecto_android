package org.duckdns.einyel.trabajo_grupal.database.local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import org.duckdns.einyel.trabajo_grupal.model.Comment;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;


@Dao
public interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment comment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Comment> comments);

    @Query("Select * From comments Where e_id = :id")
    Flowable<List<Comment>> commentsFromEvent(Long id);


}
