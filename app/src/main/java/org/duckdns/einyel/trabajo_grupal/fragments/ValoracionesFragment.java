package org.duckdns.einyel.trabajo_grupal.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ValoracionesFragment extends Fragment {

    private List<Comment> comentarios;
    private DescripcionActivity descripcionActivity;
    private View v;
    private int votos1 = 0;
    private int votos2 = 0;
    private int votos3 = 0;
    private int votos4 = 0;
    private int votos5 = 0;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        descripcionActivity = (DescripcionActivity) container.getContext();
        comentarios = descripcionActivity.getComentarios();

        if(comentarios.size() == 0){
            v = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.fragmentvaloraciones_vacia, container, false);
        }

        else{
            v = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.fragmentvaloraciones_lista, container, false);

            for(int i=0; i<comentarios.size(); i++){
                double rate = comentarios.get(i).getRate();
                if(rate>=1 && rate < 2)
                    votos1++;
                else if(rate>=2 && rate < 3)
                    votos2++;
                else if(rate>=3 && rate < 4)
                    votos3++;
                else if(rate >=4 && rate <5)
                    votos4++;
                else if(rate >=5)
                    votos5++;
            }

            recyclerView = (RecyclerView) v.findViewById(R.id.listaComentarios);
            crearGrafico();


            double puntuacionMediaEvento = ((votos1)+(votos2)*2+(votos3)*3+(votos4)*4+(votos5)*5.0)/this.comentarios.size();
            TextView puntuacionMedia = (TextView) v.findViewById(R.id.puntuacion_media);
            puntuacionMedia.setText(puntuacionMediaEvento+"");

            TextView votosTotales = (TextView) v.findViewById(R.id.totalValoraciones);
            votosTotales.setText(this.comentarios.size() + " valoraciones");


            changeRecyclerData(comentarios);
        }
        return v;
    }

    private void crearGrafico(){
        HorizontalBarChart chart = (HorizontalBarChart) v.findViewById(R.id.horizontalBarChar);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, votos1));
        entries.add(new BarEntry(2, votos2));
        entries.add(new BarEntry(3, votos3));
        entries.add(new BarEntry(4, votos4));
        entries.add(new BarEntry(5, votos5));
        chart.setViewPortOffsets(0f, 0f, 0f, 0f);
        chart.setExtraOffsets(0,0,0,0);

        BarDataSet set = new BarDataSet(entries, "Puntuaciones");

        int  c = Color.rgb(189, 189, 189);
        int[] colors = {c,c,c,c,c};

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
                List<Comment> filtrados = new ArrayList<>();
                int numero = (int) e.getX();

                if(e.getY() == 0){
                    changeRecyclerData(comentarios);
                    return;
                }

                for (Comment c : comentarios){
                    if(c.getRate()>=numero && c.getRate() <numero+1)
                        filtrados.add(c);
                }

                changeRecyclerData(filtrados);

            }

            @Override
            public void onNothingSelected() {
                changeRecyclerData(comentarios);
            }
        });
    }

    private void changeRecyclerData(List<Comment> lista){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(descripcionActivity);
        recyclerView.setLayoutManager(layoutManager);
        ComentarioAdapter adapter = new ComentarioAdapter(lista);
        recyclerView.setAdapter(adapter);
    }


    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

            if(value > 0) {
                return mFormat.format(value);
            } else {
                return "";
            }
        }
    }


}
