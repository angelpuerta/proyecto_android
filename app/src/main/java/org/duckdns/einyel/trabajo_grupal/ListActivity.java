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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.duckdns.einyel.trabajo_grupal.adapter.EventoAdapter;
import org.duckdns.einyel.trabajo_grupal.listener.RecyclerTouchListener;
import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;
import org.duckdns.einyel.trabajo_grupal.service.App;
import org.duckdns.einyel.trabajo_grupal.service.DownloadImageTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton profileButton;

    public static final String EVENTO = "EVENTO";

    public static String NOMBRE_USUARIO = "";
    public static String LOGIN = "";
    public static String URL_PIC = "";
    public static String SEXO = "";
    public static String BUSCAR = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        Bundle extras = getIntent().getExtras();
        BUSCAR = extras.getString("buscar");
        LOGIN = extras.getString("socialLogin");
        NOMBRE_USUARIO = extras.getString("username");
        SEXO = extras.getString("sexo");
        URL_PIC = extras.getString("imageUrl");

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        profileButton = (ImageButton) findViewById(R.id.profileButton);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                mIntent.putExtra("imageUrl", URL_PIC);
                mIntent.putExtra("username", NOMBRE_USUARIO);
                mIntent.putExtra("socialLogin", LOGIN);
                mIntent.putExtra("sexo", SEXO);
                mIntent.putExtra("buscar", query);
                startActivityForResult(mIntent, 100);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if(BUSCAR == null) {
            adapter = new EventoAdapter(App.get().eventsOptions());
        }else {
            adapter = new EventoAdapter(App.get().filtrarEventos(BUSCAR));
        }
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        MockEvent evento = (MockEvent) adapter.getItem(position);
                        Intent nextActivity = new Intent(getApplicationContext(), DescripcionActivity.class);
                        nextActivity.putExtra(EVENTO, evento);
                        NOMBRE_USUARIO = extras.getString("username");
                        nextActivity.putExtra("username", NOMBRE_USUARIO);
                        startActivity(nextActivity);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                })
        );

        if(URL_PIC != null && !URL_PIC.equals("")) {
            if (LOGIN.equals("facebook") || LOGIN.equals("twitter") || LOGIN.equals("google")) {
                Picasso.get().load(URL_PIC).into(profileButton);
            }
        }

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOGIN = extras.getString("socialLogin");
                NOMBRE_USUARIO = extras.getString("username");
                URL_PIC = extras.getString("imageUrl");
                SEXO = extras.getString("sexo");
                Intent mIntent = new Intent(getApplicationContext(), Perfil.class);
                mIntent.putExtra("imageUrl", URL_PIC);
                mIntent.putExtra("username", NOMBRE_USUARIO);
                mIntent.putExtra("socialLogin", LOGIN);
                mIntent.putExtra("sexo", SEXO);
                startActivityForResult(mIntent, 100);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
