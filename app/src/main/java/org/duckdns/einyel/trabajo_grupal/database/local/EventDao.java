package org.duckdns.einyel.trabajo_grupal.database.local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.duckdns.einyel.trabajo_grupal.model.MockEvent;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;


@Dao
public interface EventDao {

    @Query("SELECT * FROM events")
    Flowable<List<MockEvent>> getAll();

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    Maybe<MockEvent> getEvent(Long id);

    @Update
    void update(MockEvent event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MockEvent event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MockEvent> events);

    @Delete
    void delete(MockEvent event);

}
