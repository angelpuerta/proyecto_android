package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DescriptionActivity2 extends AppCompatActivity {

    Intent intent;

    TextView tTittle;
    TextView tDescription;
    ImageView imageViewer;

    MockEvent event;

    public static final String EVENT_ID = "EVENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_layaout2);
       //Event event = getIntent().getExtras().getParcelable();
        event = EventSevice.getInstance().getEvent(Long.valueOf(1));

        intent = new Intent(this, RankingActivity.class);

        tTittle = findViewById(R.id.tituloDescripcion);
        tDescription= findViewById(R.id.cuerpoDescripcion);
        imageViewer = findViewById(R.id.imageView);
        loadEvent(event);
    }

    void loadEvent (MockEvent event){
        Log.d("ERROR", event.toString());
        tTittle.setText(event.getTittle());
        tDescription.setText(event.getDescription());
        Glide.with(this).load(event.getImgURL()).into(imageViewer);
    }

    public void nextClick(View view) {

        startActivity(intent);
        intent.putExtra(EVENT_ID, event.getId());
    }


}
