package org.duckdns.einyel.trabajo_grupal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.duckdns.einyel.trabajo_grupal.adapter.EventoAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ListActivity extends AppCompatActivity {

    ListView listView;
    public static ArrayList<MockEvent> lista = new ArrayList<MockEvent>();
    public static final String EVENTO = "EVENTO";
    Intent mi;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        /*mi = new Intent(this, DescripcionActivity.class);

        MockEvent evento = new MockEvent();
        evento.setTitulo("Concierto Melendi");
        evento.setDescripcion("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Phasellus dignissim ligula ut dignissim rutrum. " +
                "Mauris ut feugiat justo. Pellentesque placerat felis vitae ligula consectetur, " +
                "et ultricies enim vestibulum. Pellentesque nec lorem purus. " +
                "Pellentesque at justo sem. Aliquam ut leo ipsum. Praesent vitae est iaculis, " +
                "condimentum sem quis, vehicula velit. Nunc nec libero bibendum dolor facilisis " +
                "congue et id nisl. Nulla ut fringilla diam.");
        evento.setUbicación("Oviedo");
        evento.setFechaInicio(new Date());


        mi.putExtra(EVENTO, evento);
        lista.add(evento);

        listView = (ListView) findViewById(R.id.listaEventos);
        AdaptadorEventos adapter = new AdaptadorEventos(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(mi, ActivityOptions.makeSceneTransitionAnimation(ListActivity.this).toBundle());
            }
        });*/

        MockEvent evento = new MockEvent(new Long(1));
        evento.tittle = "Tour Melendi 2018";
        evento.mark = 8.5;
        evento.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Phasellus dignissim ligula ut dignissim rutrum. " +
                "Mauris ut feugiat justo. Pellentesque placerat felis vitae ligula consectetur, " +
                "et ultricies enim vestibulum. Pellentesque nec lorem purus. " +
                "Pellentesque at justo sem. Aliquam ut leo ipsum. Praesent vitae est iaculis, " +
                "condimentum sem quis, vehicula velit. Nunc nec libero bibendum dolor facilisis";
        evento.imgURL = R.raw.melendi;

        MockEvent evento2 = new MockEvent(new Long(1));
        evento2.tittle = "Tour Melendi 2018";
        evento2.mark = 3;
        evento2.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Phasellus dignissim ligula ut dignissim rutrum. " +
                "Mauris ut feugiat justo. Pellentesque placerat felis vitae ligula consectetur, " +
                "et ultricies enim vestibulum. Pellentesque nec lorem purus. " +
                "Pellentesque at justo sem. Aliquam ut leo ipsum. Praesent vitae est iaculis, " +
                "condimentum sem quis, vehicula velit. Nunc nec libero bibendum dolor facilisis";
        evento2.imgURL = R.raw.melendi;

        MockEvent evento3 = new MockEvent(new Long(1));
        evento3.tittle = "Tour Melendi 2018";
        evento3.mark = 6;
        evento3.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Phasellus dignissim ligula ut dignissim rutrum. " +
                "Mauris ut feugiat justo. Pellentesque placerat felis vitae ligula consectetur, " +
                "et ultricies enim vestibulum. Pellentesque nec lorem purus. " +
                "Pellentesque at justo sem. Aliquam ut leo ipsum. Praesent vitae est iaculis, " +
                "condimentum sem quis, vehicula velit. Nunc nec libero bibendum dolor facilisis";
        evento3.imgURL = R.raw.melendi;

        List items = new ArrayList();
        items.add(evento);
        items.add(evento2);
        items.add(evento3);
        items.add(evento);
        items.add(evento);

        // Obtener el Recycler
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventoAdapter(items);
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
