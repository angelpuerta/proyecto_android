package org.duckdns.einyel.trabajo_grupal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class DescripcionActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

        Bundle b = getIntent().getExtras();
        Evento evento = b.getParcelable(MainActivity.EVENTO);

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
        System.out.println("\nEjecutandose onMapReady \n");
        gmap = googleMap;
        LatLng coor = new LatLng(43.3579649,-5.8733862);
        CameraUpdate cu = CameraUpdateFactory.newLatLng(coor);
        gmap.animateCamera(cu);
        gmap.addMarker (new MarkerOptions()
                .position(coor)
                .title("El titulo que quieras"));
    }
}
