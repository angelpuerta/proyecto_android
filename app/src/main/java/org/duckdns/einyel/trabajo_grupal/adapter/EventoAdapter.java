package org.duckdns.einyel.trabajo_grupal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.duckdns.einyel.trabajo_grupal.MockEvent;
import org.duckdns.einyel.trabajo_grupal.R;

import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder>{

    public static class EventoViewHolder extends RecyclerView.ViewHolder{

        public ImageView imagen;
        public TextView nombre;
        public TextView puntuacion;
        public TextView descripcion;

        public EventoViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            puntuacion = (TextView) v.findViewById(R.id.puntuacion);
            descripcion = (TextView) v.findViewById(R.id.descripcion);
        }
    }

    private List<MockEvent> eventos;

    public EventoAdapter(List<MockEvent> eventos){
        this.eventos = eventos;
    }

    @Override
    public EventoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.evento_card, parent, false);
        return new EventoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventoViewHolder holder, int position) {
        holder.imagen.setImageResource(eventos.get(position).getImgURL());
        holder.nombre.setText(eventos.get(position).getTittle());
        changeMarkBackground(holder,position);
        holder.puntuacion.setText(eventos.get(position).getMark()+"");
        holder.descripcion.setText(eventos.get(position).getDescription());
    }


    private void changeMarkBackground(EventoViewHolder holder, int position){

        double calificacion = eventos.get(position).getMark();
        if(calificacion >= 0 && calificacion < 4){
            holder.puntuacion.setBackgroundResource(R.drawable.puntuacion_background_orange);
        }
        else if(calificacion >=4 && calificacion <=6.5){
            holder.puntuacion.setBackgroundResource(R.drawable.puntuacion_background_yellow);
        }
        else{
            holder.puntuacion.setBackgroundResource(R.drawable.puntuacion_background_green);
        }

    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }
}