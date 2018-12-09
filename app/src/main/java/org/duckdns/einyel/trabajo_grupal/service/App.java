package org.duckdns.einyel.trabajo_grupal.service;

import android.app.Application;
import android.arch.persistence.room.Room;

import org.duckdns.einyel.trabajo_grupal.database.AppDatabase;
import org.duckdns.einyel.trabajo_grupal.database.CommentsRepoImpl;
import org.duckdns.einyel.trabajo_grupal.database.EventsRepoImpl;
import org.duckdns.einyel.trabajo_grupal.database.local.CommentsLocalRepo;
import org.duckdns.einyel.trabajo_grupal.database.local.EventsLocalRepo;
import org.duckdns.einyel.trabajo_grupal.database.remote.CommentsRemoteRepo;
import org.duckdns.einyel.trabajo_grupal.database.remote.EventsRemoteRepo;

import io.reactivex.disposables.CompositeDisposable;

public class App extends Application {

    public static App INSTANCE;
    private static final String DATABASE_NAME = "MyDatabase";
    private static final String PREFERENCES = "RoomDemo.preferences";
    private static final String KEY_FORCE_UPDATE = "force_update";

    private CompositeDisposable disposable;
    private AppDatabase database;

    private EventsRepoImpl eventsRepoImp;
    private CommentsRepoImpl commentsRepo;

    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create database
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build();

        eventsRepoImp = new EventsRepoImpl(new EventsRemoteRepo(), new EventsLocalRepo(database.EventDao()));
        commentsRepo = new CommentsRepoImpl(new CommentsLocalRepo(database.CommentDao()), new CommentsRemoteRepo());
        disposable = new CompositeDisposable();

        INSTANCE = this;
    }


    public AppDatabase getDB() {
        return database;
    }

    public EventsRepoImpl getEventsRepoImp() {
        return eventsRepoImp;
    }


    public CommentsRepoImpl getCommentsRepo() {
        return commentsRepo;
    }

    public CompositeDisposable disposable() {
        return disposable;
    }
}