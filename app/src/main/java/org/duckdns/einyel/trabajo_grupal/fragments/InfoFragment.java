package org.duckdns.einyel.trabajo_grupal.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.duckdns.einyel.trabajo_grupal.DescripcionActivity;
import org.duckdns.einyel.trabajo_grupal.ListActivity;
import org.duckdns.einyel.trabajo_grupal.R;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class InfoFragment extends Fragment implements OnMapReadyCallback {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = LayoutInflater.from(container.getContext())
                .inflate(R.layout.fragmentinfo, container, false);
        this.container = container;
        fillData();
        return v;
    }

    private GoogleMap mapa;
    private MockEvent evento;
    private DescripcionActivity descripcionActivity;
    private View v;
    private ViewGroup container;

    private void fillData(){
        descripcionActivity  = (DescripcionActivity) container.getContext();
        evento = descripcionActivity.getEvento();

        TextView descripcion = v.findViewById(R.id.descripcionInfo);
        descripcion.setText(evento.getDescription());

        SupportMapFragment mapFragment = (SupportMapFragment) descripcionActivity.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.getUiSettings().setZoomControlsEnabled(false);
        mapa.getUiSettings().setScrollGesturesEnabled(false);

        //Coordenadas
        String[] latLng = evento.getLocation().split(",");
        Double lat = Double.parseDouble(latLng[0]);
        Double lng = Double.parseDouble(latLng[1]);

        //Mover camara
        LatLng ubicacion = new LatLng(lat, lng);
        CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(ubicacion,15);
        mapa.moveCamera(camUpd);
        mapa.addMarker (new MarkerOptions()
                .position(ubicacion));

        cargarCalle(lat,lng);
    }

    private void cargarCalle(Double lat, Double lng){
        Geocoder geocoder = new Geocoder(descripcionActivity, Locale.getDefault());

        try {
            List<Address> direcciones = geocoder.getFromLocation(lat,lng, 1);
            String direccionCompleta = "";

            Address direccion = direcciones.get(0);
            direccionCompleta += direccion.getThoroughfare() + " " + direccion.getSubThoroughfare() + "\n";
            direccionCompleta += direccion.getLocality() + " (" + direccion.getSubAdminArea() + ")\n";
            direccionCompleta += direccion.getPostalCode();

            TextView calle = v.findViewById(R.id.calleInfo);
            calle.setText(direccionCompleta);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
