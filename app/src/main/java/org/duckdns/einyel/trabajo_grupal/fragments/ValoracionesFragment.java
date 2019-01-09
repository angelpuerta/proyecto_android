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
import org.duckdns.einyel.trabajo_grupal.service.App;


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
    private int votos1 = 0;
    private int votos2 = 0;
    private int votos3 = 0;
    private int votos4 = 0;
    private int votos5 = 0;

    private RecyclerView recyclerView;

    private Set<Comment> comments;

    private Long evento_id;
    private String username;

    private AppCompatImageButton puntuar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        descripcionActivity = (DescripcionActivity) container.getContext();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(descripcionActivity);


        v = LayoutInflater.from(container.getContext())
                .inflate(R.layout.fragmentvaloraciones_lista, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.listaComentarios);
        recyclerView.setLayoutManager(layoutManager);


        puntuar = v.findViewById(R.id.puntuarButton);
        puntuar.setEnabled(false);
        puntuar.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        this.evento_id = descripcionActivity.getEvento().getId();
        adapter = new ComentarioAdapter(App.get().commentsOption(evento_id), this);

        recyclerView.setAdapter(adapter);


        comments = new HashSet<>();

        chart = (HorizontalBarChart) v.findViewById(R.id.horizontalBarChar);

        crearGrafico();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        String search = "QR/" + descripcionActivity.getUsername() + "/" + evento_id;
        String code = descripcionActivity.getSharedPreferences("QRs", Context.MODE_PRIVATE).getString(search, "");

        if (!code.equals("")) {
            puntuar.findViewById(R.id.puntuarButton).setEnabled(true);
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

    HorizontalBarChart chart;

    private void crearGrafico() {

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, votos1));
        entries.add(new BarEntry(2, votos2));
        entries.add(new BarEntry(3, votos3));
        entries.add(new BarEntry(4, votos4));
        entries.add(new BarEntry(5, votos5));
        chart.setViewPortOffsets(0f, 0f, 0f, 0f);
        chart.setExtraOffsets(0, 0, 0, 0);

        BarDataSet set = new BarDataSet(entries, "Puntuaciones");


        int c = Color.rgb(189, 189, 189);
        int[] colors = {c, c, c, c, c};

        set.setColors(colors);


        BarData data = new BarData(set);
        data.setValueFormatter(new MyValueFormatter());
        chart.setData(data);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.setScaleEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        //Con esto borro las labels del eje de abajo
        chart.getAxisRight().setDrawLabels(false);
        //Con esto borro la linea del eje de abajo
        chart.getAxisRight().setDrawAxisLine(false);

        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                chart.resetZoom();
                int numero = (int) e.getX();

                if (e.getY() == 0) {
                    changeRecyclerData();
                    return;
                }

                changeRecyclerData(numero);

            }

            @Override
            public void onNothingSelected() {
                changeRecyclerData();
            }
        });
    }

    private void changeRecyclerData(double rate) {
        adapter = new ComentarioAdapter(App.get().commentsOption(evento_id, rate), this);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void changeRecyclerData() {
        adapter = new ComentarioAdapter(App.get().commentsOption(evento_id), this);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public void addCommentToWhole(Comment comment) {
        if (comments.add(comment)) {
            double rate = comment.getRate();
            if (rate >= 1 && rate < 2)
                votos1++;
            else if (rate >= 2 && rate < 3)
                votos2++;
            else if (rate >= 3 && rate < 4)
                votos3++;
            else if (rate >= 4 && rate < 5)
                votos4++;
            else if (rate >= 5)
                votos5++;
            double puntuacionMediaEvento = ((votos1) + (votos2) * 2 + (votos3) * 3 + (votos4) * 4 + (votos5) * 5.0) / this.comments.size();
            TextView puntuacionMedia = (TextView) v.findViewById(R.id.puntuacion_media);
            puntuacionMedia.clearComposingText();
            puntuacionMedia.setText(String.format("%.1f", puntuacionMediaEvento));

            TextView votosTotales = (TextView) v.findViewById(R.id.totalValoraciones);
            votosTotales.clearComposingText();
            votosTotales.setText(this.comments.size() + " valoraciones");

            chart.notifyDataSetChanged();
            chart.invalidate();

            crearGrafico();
        }
    }


}
