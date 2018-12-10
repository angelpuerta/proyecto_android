package org.duckdns.einyel.trabajo_grupal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.duckdns.einyel.trabajo_grupal.adapter.EventoAdapter;
import org.duckdns.einyel.trabajo_grupal.database.AppDatabase;
import org.duckdns.einyel.trabajo_grupal.listener.RecyclerTouchListener;
import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;
import org.duckdns.einyel.trabajo_grupal.service.App;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class ListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private App app = App.get();
    private List<MockEvent> eventosBD = new ArrayList<>();

    public static final String EVENTO= "EVENTO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        DescripcionActivity.getComentariosDB();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventos = database.getReference("events");

        eventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> eventos = snapshot.getChildren();
                for(DataSnapshot evento : eventos){
                    Iterable<DataSnapshot> campos = evento.getChildren();
                    String titulo = "";
                    String descripcion = "";
                    String location = "";
                    String imgReal = "";
                    Double mark = 0.0;
                    Long id = null;
                    for(DataSnapshot campo : campos ){
                        String nombreCampo = campo.getKey();
                        if(nombreCampo.equals("titulo"))
                            titulo = (String) campo.getValue();
                        else if(nombreCampo.equals("descripcion"))
                            descripcion = (String) campo.getValue();
                        else if(nombreCampo.equals("location"))
                            location = (String) campo.getValue();
                        else if(nombreCampo.equals("imgReal"))
                            imgReal = (String) campo.getValue();
                        else if(nombreCampo.equals("mark"))
                            mark = (Double) campo.getValue();
                        else if(nombreCampo.equals("id"))
                            id = (Long) campo.getValue();
                    }
                    eventosBD.add(new MockEvent(id, mark, descripcion, titulo, location, imgReal));

                }

                rellenarRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        eventos.orderByChild("1");

        iniciarRecycler();

    }

    private void iniciarRecycler(){
        rellenarRecycler();

        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        MockEvent evento = eventosBD.get(position);
                        DescripcionActivity.resetComentarios();
                        Intent nextActivity = new Intent(getApplicationContext(), DescripcionActivity.class);
                        nextActivity.putExtra(EVENTO, evento);
                        startActivity(nextActivity);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                })
        );
    }


    private void rellenarRecycler(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventoAdapter(eventosBD);
        recyclerView.setAdapter(adapter);

    }


}
