package org.duckdns.einyel.trabajo_grupal.service;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.model.Event;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public FirebaseRecyclerOptions<Event> filtrarEventos(String filtro) {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .orderByChild("tittle")
                .startAt(filtro);

        return new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(query, new SnapshotParser<Event>() {
                    @NonNull
                    @Override
                    public Event parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.d("AUXILIAR", snapshot.toString());
                        return snapshot.getValue(Event.class);
                    }
                })
                .build();
    }

    public FirebaseRecyclerOptions<Event> filtrarEventosPorCategoria(String filtro) {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .orderByChild("tags")
                .equalTo(filtro);

        return new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(query, new SnapshotParser<Event>() {
                    @NonNull
                    @Override
                    public Event parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.d("AUXILIAR", snapshot.toString());
                        return snapshot.getValue(Event.class);
                    }
                })
                .build();

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

    public FirebaseRecyclerOptions<Event> eventsOptions() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .limitToLast(50);

        FirebaseRecyclerOptions<Event> auxiliar = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(query, new SnapshotParser<Event>() {
                    @NonNull
                    @Override
                    public Event parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.d("AUXILIAR", snapshot.toString());
                        return snapshot.getValue(Event.class);
                    }
                })
                .build();

        ObservableSnapshotArray<Event> snapshots = auxiliar.getSnapshots();

        return auxiliar;
    }


    public FirebaseRecyclerOptions<Event> eventsOptions(String filter) {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .startAt(filter)
                .orderByChild("tittle")
                .limitToLast(50);
        return new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(query, new SnapshotParser<Event>() {
                    @NonNull
                    @Override
                    public Event parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Log.d("AUXILIAR", snapshot.toString());
                        return snapshot.getValue(Event.class);
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