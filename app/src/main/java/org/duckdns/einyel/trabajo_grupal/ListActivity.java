package org.duckdns.einyel.trabajo_grupal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import org.duckdns.einyel.trabajo_grupal.adapter.EventoAdapter;
import org.duckdns.einyel.trabajo_grupal.database.AppDatabase;
import org.duckdns.einyel.trabajo_grupal.listener.RecyclerTouchListener;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;
import org.duckdns.einyel.trabajo_grupal.service.App;

import java.util.ArrayList;
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
    private List<MockEvent> eventosBD;

    public static final String EVENTO= "EVENTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        //Obtengo los eventos de la base de datos
        Flowable<List<MockEvent>> eventosFlowables = app.getEventsRepoImp().getAll();
        eventosBD = eventosFlowables.blockingFirst();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventoAdapter(eventosBD);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        MockEvent evento = eventosBD.get(position);
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

}
