package org.duckdns.einyel.trabajo_grupal.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.duckdns.einyel.trabajo_grupal.DescripcionActivity;
import org.duckdns.einyel.trabajo_grupal.R;
import org.duckdns.einyel.trabajo_grupal.adapter.ComentarioAdapter;
import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.model.Event;
import org.duckdns.einyel.trabajo_grupal.service.App;
import org.duckdns.einyel.trabajo_grupal.service.Check;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValoracionesFragment extends Fragment implements ComentarioAdapter.AddCommentToWhole {

    private DescripcionActivity descripcionActivity;
    ComentarioAdapter adapter;
    private View v;

    private RecyclerView recyclerView;

    private Event event;
    private String username;

    private Button puntuar;
    private Date actualDate;

    private TextView puntuacionMedia;
    private TextView votosTotales;

    private double puntuacionTotal;
    private int numeroComentarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        descripcionActivity = (DescripcionActivity) container.getContext();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(descripcionActivity);


        v = LayoutInflater.from(container.getContext())
                .inflate(R.layout.fragmentvaloraciones_lista, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.listaComentarios);
        recyclerView.setLayoutManager(layoutManager);


        puntuar = v.findViewById(R.id.btnPuntuar);
        puntuar.setEnabled(false);
        puntuar.setBackground(getResources().getDrawable(R.drawable.star_icon_grey));
        TextView textPuntuar = (TextView) v.findViewById(R.id.textPuntuar);
        textPuntuar.setTextColor(getResources().getColor(R.color.grey));
        //puntuar.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        this.event = descripcionActivity.getEvento();
        adapter = new ComentarioAdapter(App.get().commentsOption(event.getId()), this);

        recyclerView.setAdapter(adapter);

        actualDate = new Date();

        puntuacionMedia = (TextView) v.findViewById(R.id.puntuacion_media);
        puntuacionMedia.setText(this.formatMark(this.event.getMark()));
        votosTotales = (TextView) v.findViewById(R.id.totalValoraciones);
        votosTotales.setText(this.event.getNumberOfComments() + " valoraciones");

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        String search = "QR/" + descripcionActivity.getUsername() + "/" + event.getId();
        String code = descripcionActivity.getSharedPreferences("QRs", Context.MODE_PRIVATE).getString(search, "");
        if (!code.equals("")) {
            puntuar.findViewById(R.id.btnPuntuar).setEnabled(true);
            puntuar.getBackground().setColorFilter(null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

            if (value > 0) {
                return mFormat.format(value);
            } else {
                return "";
            }
        }
    }


    private void changeRecyclerData(double rate) {
        adapter = new ComentarioAdapter(App.get().commentsOption(event.getId(), rate), this);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void changeRecyclerData() {
        adapter = new ComentarioAdapter(App.get().commentsOption(event.getId()), this);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public void addCommentToWhole(Comment comment) {
        if (comment.getTimestamp().compareTo(actualDate) >= 0) {
            puntuacionTotal = this.puntuacionTotal + comment.getRate();
            numeroComentarios++;
            puntuacionMedia.clearComposingText();
            puntuacionMedia.setText(this.formatMark(this.puntuacionTotal));

            votosTotales.clearComposingText();
            votosTotales.setText(this.numeroComentarios + " valoraciones");
        }
    }

    private String formatMark(Double mark) {
        return String.format("%.1f", mark);
    }

}
