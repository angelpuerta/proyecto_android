package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import org.duckdns.einyel.trabajo_grupal.adapter.EventoAdapter;
import org.duckdns.einyel.trabajo_grupal.database.AppDatabase;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtengo los eventos de la base de datos
        Flowable<List<MockEvent>> eventosFlowables = app.getEventsRepoImp().getAll();
        eventosBD = eventosFlowables.blockingFirst();

        setContentView(R.layout.list_activity);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventoAdapter(eventosBD);
        recyclerView.setAdapter(adapter);

    }

    /*class AdaptadorEventos extends ArrayAdapter<MockEvent>{

        AppCompatActivity appCompatActivity;

        public AdaptadorEventos(AppCompatActivity context) {
            super(context, R.layout.fila_lista, lista);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.fila_lista, null);

            TextView textView1 = (TextView)item.findViewById(R.id.nombre_fila_lista);
            textView1.setText(lista.get(position).getTitulo());

            ImageView imageView1 = (ImageView)item.findViewById(R.id.icon);
            //imageView1.setImageResource(R.mipmap.melendi);

            return(item);
        }
    }*/

}
