package org.duckdns.einyel.trabajo_grupal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
import org.duckdns.einyel.trabajo_grupal.adapter.EventoAdapterAsistidos;
import org.duckdns.einyel.trabajo_grupal.listener.RecyclerTouchListener;
import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;
import org.duckdns.einyel.trabajo_grupal.model.User;
import org.duckdns.einyel.trabajo_grupal.service.App;
import org.duckdns.einyel.trabajo_grupal.service.DownloadImageTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton profileButton;

    public static final String EVENTO = "EVENTO";

    public static String NOMBRE_USUARIO = "";
    public static String LOGIN = "";
    public static String URL_PIC = "";
    public static Long EVENTOS_ASISTIDOS = null;
    private ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Bundle extras = getIntent().getExtras();
        URL_PIC = extras.getString("imageUrl");
        NOMBRE_USUARIO = extras.getString("username");
        LOGIN = extras.getString("socialLogin");
        EVENTOS_ASISTIDOS = extras.getLong("LISTA_EVENTOS");

        setIds();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        profileButton = (ImageButton) findViewById(R.id.profileButton);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (EVENTOS_ASISTIDOS.equals(new Long(0))) {
            adapter = new EventoAdapter(App.get().eventsOptions());
        } else {
            adapter = new EventoAdapterAsistidos(App.get().eventsOptions(), assisted);
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

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOGIN = extras.getString("socialLogin");
                NOMBRE_USUARIO = extras.getString("username");
                URL_PIC = extras.getString("imageUrl");
                Intent mIntent = new Intent(getApplicationContext(), Perfil.class);
                mIntent.putExtra("imageUrl", URL_PIC);
                mIntent.putExtra("username", NOMBRE_USUARIO);
                mIntent.putExtra("socialLogin", LOGIN);
                startActivityForResult(mIntent, 100);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //no funciona, no carga el R.id.nav_header_imageView
        /*ImageView imagen = (ImageView) findViewById(R.id.nav_header_imageView);
        if(URL_PIC != null && !URL_PIC.equals("")) {
            if (LOGIN.equals("facebook") || LOGIN.equals("twitter") || LOGIN.equals("google")) {
                Picasso.get().load(URL_PIC).into(imagen);
                //new DownloadImageTask(imagenPerfil).execute(URL_PIC);
            }
        }*/


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getTitle().toString()) {
            case "Mis eventos":
                Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                mIntent.putExtra("socialLogin", LOGIN);
                mIntent.putExtra("username", NOMBRE_USUARIO);
                mIntent.putExtra("imageUrl", URL_PIC);
                mIntent.putExtra("LISTA_EVENTOS", new Long(1));
                startActivity(mIntent);
                Toast.makeText(this, "Clicked item one", Toast.LENGTH_SHORT).show();
                break;
        }

        return false;
    }

    List<Long> assisted;

    protected void setIds() {
        FirebaseDatabase.getInstance().getReference("usuarios").orderByChild("nick").equalTo(NOMBRE_USUARIO.trim()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren())
                            if (userSnapshot.hasChild("assisted")) {
                                assisted = userSnapshot.child("assisted").getValue(List.class);
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}

