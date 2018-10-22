package org.duckdns.einyel.trabajo_grupal;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    public static ArrayList<Evento> lista = new ArrayList<Evento>();
    public static final String EVENTO = "EVENTO";
    Intent mi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mi = new Intent(this, DescripcionActivity.class);

        Evento evento = new Evento();
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
        });

    }

    class AdaptadorEventos extends ArrayAdapter<Evento>{

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
    }

}
