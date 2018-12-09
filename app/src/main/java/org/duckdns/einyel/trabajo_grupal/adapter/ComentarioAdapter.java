package org.duckdns.einyel.trabajo_grupal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.duckdns.einyel.trabajo_grupal.R;
import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.service.App;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.CommentViewHolder> {

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView nombreUsuario;
        public TextView puntuacion;
        public TextView comentario;
        public TextView fecha;

        public CommentViewHolder(View v) {
            super(v);
            nombreUsuario = (TextView) v.findViewById(R.id.usuario);
            puntuacion = (TextView) v.findViewById(R.id.puntuacion_comment);
            comentario = (TextView) v.findViewById(R.id.comentario);
            fecha = (TextView) v.findViewById(R.id.fecha_comentario);
        }
    }

    private List<Comment> comentarios;
    private ViewGroup parent;
    private App app = App.get();

    public ComentarioAdapter(List<Comment> comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_card, parent, false);

        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.nombreUsuario.setText(comentarios.get(position).getU_id()+"");
        changeMarkBackground(holder, position);
        holder.puntuacion.setText(comentarios.get(position).getRate() + "");
        holder.comentario.setText(comentarios.get(position).getComment());
        Date fecha = comentarios.get(position).getTimestamp();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        holder.fecha.setText(day + "-" + month + "-" + year);
        holder.comentario.setTextColor(parent.getContext().getResources().getColor(R.color.black));
    }


    private void changeMarkBackground(CommentViewHolder holder, int position) {

        double calificacion = comentarios.get(position).getRate();

        if(calificacion>=1 && calificacion < 2)
            holder.puntuacion.setBackgroundResource(R.drawable.puntuacion_background_voto1);
        else if(calificacion>=2 && calificacion < 3)
            holder.puntuacion.setBackgroundResource(R.drawable.puntuacion_background_voto2);
        else if(calificacion>=3 && calificacion < 4)
            holder.puntuacion.setBackgroundResource(R.drawable.puntuacion_background_voto3);
        else if(calificacion >=4 && calificacion <5)
            holder.puntuacion.setBackgroundResource(R.drawable.puntuacion_background_voto4);
        else if(calificacion >=5)
            holder.puntuacion.setBackgroundResource(R.drawable.puntuacion_background_voto5);


    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }
}