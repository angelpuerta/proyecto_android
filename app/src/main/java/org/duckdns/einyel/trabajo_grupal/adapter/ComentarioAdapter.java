package org.duckdns.einyel.trabajo_grupal.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.duckdns.einyel.trabajo_grupal.DescripcionActivity;
import org.duckdns.einyel.trabajo_grupal.R;
import org.duckdns.einyel.trabajo_grupal.fragments.ValoracionesFragment;
import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.service.App;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ComentarioAdapter extends FirebaseRecyclerAdapter<Comment, ComentarioAdapter.CommentViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    AddCommentToWhole addcomments;

    public ComentarioAdapter(@NonNull FirebaseRecyclerOptions<Comment> options, AddCommentToWhole addcomments) {
        super(options);
        this.addcomments = addcomments;
    }

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

    private ViewGroup parent;

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_card, parent, false);

        return new CommentViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {

        addcomments.addCommentToWhole(model);


        holder.nombreUsuario.setText(model.getUser() == null ? "anonymous" : model.getUser() + "");
        holder.puntuacion.setText(setRateTextView(model.getRate()));
        holder.comentario.setText(model.getComment());
        Date fecha = model.getTimestamp();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        holder.fecha.setText(day + "-" + month + "-" + year);
        holder.comentario.setTextColor(parent.getContext().getResources().getColor(R.color.black));
    }

    private String setRateTextView(double rate){
        int truncRate = (int) rate;
        switch (truncRate){
            case 1:
                return "★";
            case 2:
                return "★★";
            case 3:
                return "★★★";
            case 4:
                return "★★★★";
            case 5:
                return "★★★★★";
            default:
                return "";
        }
    }



    public interface AddCommentToWhole {

        void addCommentToWhole(Comment comment);

    }
}