package org.duckdns.einyel.trabajo_grupal.service;

import android.app.Application;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import org.duckdns.einyel.trabajo_grupal.ListActivity;
import org.duckdns.einyel.trabajo_grupal.Login;
import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;
import org.duckdns.einyel.trabajo_grupal.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class App extends Application {

    public static App INSTANCE;
    private static final String DATABASE_NAME = "MyDatabase";
    private static final String PREFERENCES = "RoomDemo.preferences";
    private static final String KEY_FORCE_UPDATE = "force_update";
    private static final String BASE_URL = "https://us-central1-proyecto-moviles-86dc4.cloudfunctions.net/";
    private Retrofit retrofit;

    private String user;


    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        INSTANCE = this;
    }


    public FirebaseRecyclerOptions<Comment> commentsOption(Long evento) {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("comments")
                .child(evento.toString())
                .limitToLast(50);
        return new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(query, new SnapshotParser<Comment>() {
                    @NonNull
                    @Override
                    public Comment parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.d("AUXILIAR", snapshot.toString());
                        return snapshot.getValue(Comment.class);
                    }
                })
                .build();
    }

    public FirebaseRecyclerOptions<MockEvent> filtrarEventosUsuario(Long evento) {

        List<Long> ids = new ArrayList<>();
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .orderByChild("id")
                .equalTo(ids);
        return new FirebaseRecyclerOptions.Builder<MockEvent>()
                .setQuery(query, new SnapshotParser<MockEvent>() {
                    @NonNull
                    @Override
                    public MockEvent parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.d("AUXILIAR", snapshot.toString());
                        return snapshot.getValue(MockEvent.class);
                    }
                })
                .build();
    }

    public FirebaseRecyclerOptions<MockEvent> eventsOptions() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .limitToLast(50);
        return new FirebaseRecyclerOptions.Builder<MockEvent>()
                .setQuery(query, new SnapshotParser<MockEvent>() {
                    @NonNull
                    @Override
                    public MockEvent parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.d("AUXILIAR", snapshot.toString());
                        return snapshot.getValue(MockEvent.class);
                    }
                })
                .build();
    }


    public FirebaseRecyclerOptions<MockEvent> eventsOptions(String filter) {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .startAt(filter)
                .orderByChild("tittle")
                .limitToLast(50);
        return new FirebaseRecyclerOptions.Builder<MockEvent>()
                .setQuery(query, new SnapshotParser<MockEvent>() {
                    @NonNull
                    @Override
                    public MockEvent parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.d("AUXILIAR", snapshot.toString());
                        return snapshot.getValue(MockEvent.class);
                    }
                })
                .build();
    }

    public FirebaseRecyclerOptions<Comment> commentsOption(Long evento, double mark) {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("comments")
                .child(evento.toString())
                .orderByChild("rate")
                .startAt(mark)
                .endAt(mark)
                .limitToLast(50);
        return new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(query, new SnapshotParser<Comment>() {
                    @NonNull
                    @Override
                    public Comment parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.d("AUXILIAR", snapshot.toString());
                        return snapshot.getValue(Comment.class);
                    }
                })
                .build();
    }

    public Retrofit getRetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) //This is the only mandatory call on Builder object.
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .build();
        }
        return retrofit;
    }

    public FirebaseApi getFirebaseApi() {
        return App.get().getRetrofitClient().create(FirebaseApi.class);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}