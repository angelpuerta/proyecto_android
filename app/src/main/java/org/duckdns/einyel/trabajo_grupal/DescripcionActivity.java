package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

public class DescripcionActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    Intent intent;

    public static final String EVENT_ID = "EVENT_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

        intent = new Intent(this, RankingActivity.class);


        Bundle b = getIntent().getExtras();

        TextView textoTitulo = (TextView) findViewById(R.id.textTitulo);
        TextView textoDescripcion = (TextView) findViewById(R.id.textDescripcion);
        TextView textoPuntuacion = (TextView) findViewById(R.id.textPuntuacion);

        textoTitulo.setText("Concierto Melendi");
        textoDescripcion.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Phasellus dignissim ligula ut dignissim rutrum. " +
                "Mauris ut feugiat justo. Pellentesque placerat felis vitae ligula consectetur, " +
                "et ultricies enim vestibulum. Pellentesque nec lorem purus. " +
                "Pellentesque at justo sem. Aliquam ut leo ipsum. Praesent vitae est iaculis, " +
                "condimentum sem quis, vehicula velit. Nunc nec libero bibendum dolor facilisis " +
                "congue et id nisl. Nulla ut fringilla diam.");
        textoPuntuacion.setText("8/10");

        MapView mapa = (MapView) findViewById(R.id.mapa);



        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapa = findViewById(R.id.mapa);
        mapa.onCreate(mapViewBundle);
        mapa.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        LatLng coor = new LatLng(43.36029, -5.84476);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(coor));
    }

    public void nextClick(View view) {

        startActivity(intent);
        intent.putExtra(EVENT_ID, Long.valueOf(1));
    }
}
