package org.duckdns.einyel.trabajo_grupal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    public static ArrayList<Evento> lista = new ArrayList<Evento>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

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


        lista.add(evento);

        listView = (ListView) findViewById(R.id.listaEventos);
        AdaptadorEventos adapter = new AdaptadorEventos(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
