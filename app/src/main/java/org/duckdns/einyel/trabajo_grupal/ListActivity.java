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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Bundle extras = getIntent().getExtras();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        profileButton = (ImageButton) findViewById(R.id.profileButton);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventoAdapter(App.get().eventsOptions());
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
