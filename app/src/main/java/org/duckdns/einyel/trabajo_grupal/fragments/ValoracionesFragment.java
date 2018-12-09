package org.duckdns.einyel.trabajo_grupal.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.duckdns.einyel.trabajo_grupal.DescripcionActivity;
import org.duckdns.einyel.trabajo_grupal.R;
import org.duckdns.einyel.trabajo_grupal.adapter.ComentarioAdapter;
import org.duckdns.einyel.trabajo_grupal.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class ValoracionesFragment extends Fragment implements ReceiveWhenCompleted<Comment> {

    private List<Comment> comentarios;
    private DescripcionActivity descripcionActivity;
    private View v;
    private PieChart mChart;
    private float[] yData = {5, 10, 15, 30, 40};
    private String[] xData = {"Sony", "Huawei", "LG", "Apple", "Samsung"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        descripcionActivity = (DescripcionActivity) container.getContext();
        descripcionActivity.getComentarios(this);

        if (comentarios.size() == 0) {
            v = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.fragmentvaloraciones_vacia, container, false);
        } else {
            v = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.fragmentvaloraciones_lista, container, false);

            PieChart pieChart = (PieChart) v.findViewById(R.id.piechart);
            pieChart.setUsePercentValues(true);

            ArrayList<PieEntry> yvalues = new ArrayList<>();
            yvalues.add(new PieEntry(8f, 0));
            yvalues.add(new PieEntry(15f, 1));
            yvalues.add(new PieEntry(12f, 2));
            yvalues.add(new PieEntry(25f, 3));
            yvalues.add(new PieEntry(23f, 4));
            yvalues.add(new PieEntry(17f, 5));

            PieDataSet dataSet = new PieDataSet(yvalues, "");

            ArrayList<String> xVals = new ArrayList<String>();

            xVals.add("January");
            xVals.add("February");
            xVals.add("March");
            xVals.add("April");
            xVals.add("May");
            xVals.add("June");

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());

            dataSet.setColors(ColorTemplate.PASTEL_COLORS);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);
            pieChart.setData(data);


        }
        return v;
    }

    @Override
    public void set(List list) {
        comentarios = list;

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.listaComentarios);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(descripcionActivity);
        recyclerView.setLayoutManager(layoutManager);
        ComentarioAdapter adapter = new ComentarioAdapter(comentarios);
        recyclerView.setAdapter(adapter);
    }

/*    private void addData() {
        List<PieEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new PieEntry(yData[i], i));

        List<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xVals.add(xData[i]);

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "Market Share");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }*/


}
