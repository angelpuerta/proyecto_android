package org.duckdns.einyel.trabajo_grupal.database;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;

import org.duckdns.einyel.trabajo_grupal.database.local.CommentDao;
import org.duckdns.einyel.trabajo_grupal.database.local.EventDao;
import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.model.DateConverter;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;
import org.duckdns.einyel.trabajo_grupal.model.User;


@Database(version = 1, entities = {MockEvent.class, Comment.class, User.class})
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    abstract public EventDao EventDao();

    abstract public CommentDao CommentDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

}
