package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.duckdns.einyel.trabajo_grupal.fragments.InfoFragment;
import org.duckdns.einyel.trabajo_grupal.fragments.ValoracionesFragment;
import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;
import org.duckdns.einyel.trabajo_grupal.service.App;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DescripcionActivity extends AppCompatActivity {

    private GoogleMap mapa;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    Intent intent;

    public static final String EVENTO = "EVENTO";

    private Bitmap bm = null;
    private MockEvent evento = null;
    private App app = App.get();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descripcion_angela_activity);

        intent = new Intent(this, RankingActivity.class);

        Bundle b = getIntent().getExtras();
        evento = b.getParcelable(ListActivity.EVENTO);


        iniciarTabLayout();

    }

    public void abrirMapaGrande(View view) {

        //Coordenadas
        String[] latLng = evento.getLocation().split(",");

        String uri = "http://maps.google.com/maps?saddr=&daddr=" + latLng[0] + "," + latLng[1];
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);


    }


    public MockEvent getEvento(){
        return this.evento;
    }

    public List<Comment> getComentarios(){


        /*Long id = this.evento.getId();
        Flowable<List<Comment>> comentariosFlowables = app.getRemoteCommentsRepo().commentsFromEvent(id);

        String com = "";
        Disposable cosa = comentariosFlowables.
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe();*/

        List<Comment> comentarios = new ArrayList<>();
        if(getEvento().getId() == 1) {

            Comment c = new Comment
                    (new Long(1), new Long(1), "Wuolaaaaaa1", 1, new Long(1), new Date());
            Comment c2 = new Comment
                    (new Long(1), new Long(1), "Wuolaaaaaa2", 3, new Long(1), new Date());
            Comment c3 = new Comment
                    (new Long(1), new Long(1), "Wuolaaaaaa3", 4, new Long(1), new Date());
            Comment c4 = new Comment
                    (new Long(1), new Long(1), "Wuolaaaaaa4", 5, new Long(1), new Date());
            Comment c5 = new Comment
                    (new Long(1), new Long(1), "Wuolaaaaaa5", 5, new Long(1), new Date());
            Comment c6 = new Comment
                    (new Long(1), new Long(1), "Wuolaaaaaa6", 3, new Long(1), new Date());


            comentarios.add(c);
            comentarios.add(c2);
            comentarios.add(c3);
            comentarios.add(c4);
            comentarios.add(c5);
            comentarios.add(c6);
        }

        return comentarios;
    }

    public void valorar(View view){
        Intent nextActivity = new Intent(getApplicationContext(), RankingActivity.class);
        nextActivity.putExtra(EVENTO, evento);
        startActivity(nextActivity);

    }

    public void checkIn(View view){
        Intent nextActivity = new Intent(getApplicationContext(), QRCodeActivity.class);
        nextActivity.putExtra(EVENTO, evento);
        startActivity(nextActivity);
    }

    private void iniciarTabLayout(){

        Picasso.get().load(evento.getImgURLReal()).into((ImageView) findViewById(R.id.imageViewDescripcion));
        TextView titulo = findViewById(R.id.tituloDescripcion);
        titulo.setText(evento.getTittle());

        cargarBitmap();

        TabLayout tl = (TabLayout) findViewById(R.id.tabs);
        tl.setBackgroundColor(getDominantColor(bm));

        TabAdapter ta = new TabAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(ta);
        tl.setupWithViewPager(viewPager);

        changeColorTextDarkLight(tl,getDominantColor(bm));
    }

    private void cargarBitmap(){
        Picasso.get().load(evento.getImgURLReal()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                bm = bitmap;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });
    }


    /**
     * Metodo que cambia de color al texto del titulo de los TabItem dependiendo de si el
     * color dominante de la imagen es claro u oscuro
     * @param tl
     * @param dominantColor
     */
    private void changeColorTextDarkLight(TabLayout tl, int dominantColor) {
        int red = Color.red(dominantColor);
        int green = Color.green(dominantColor);
        int blue = Color.blue(dominantColor);

        int selectedColor = (red+green+blue>255*3/2) ?
                R.color.black :
                R.color.white;

        int shadowColor = (selectedColor == R.color.black)?
                R.color.white :
                R.color.black;

        TextView titulo = (TextView) findViewById(R.id.tituloDescripcion);
        titulo.setTextColor(getResources().getColor(selectedColor));
        titulo.setShadowLayer(2,3,3, getResources().getColor(shadowColor));
        titulo.setBackgroundColor(dominantColor);
        titulo.getBackground().setAlpha(190);


        for (int i = 0; i < tl.getTabCount(); i++) {

            TabLayout.Tab tab = tl.getTabAt(i);
            if (tab != null) {

                TextView tabTextView = new TextView(this);
                tab.setCustomView(tabTextView);

                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                tabTextView.setText(tab.getText());

                tabTextView.setTextColor(getResources().getColor(selectedColor));

            }

        }


    }

    private int getDominantColor(Bitmap bitmap) {
        if (null == bitmap) return Color.TRANSPARENT;

        int redBucket    = 0;
        int greenBucket  = 0;
        int blueBucket   = 0;
        int alphaBucket  = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount   = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels     = new int[pixelCount];

        bitmap.getPixels(
                pixels,
                0,
                bitmap.getWidth(),
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight()
        );

        for (int y = 0, h = bitmap.getHeight(); y < h; y++){
            for (int x = 0, w = bitmap.getWidth(); x < w; x++){
                int color   =  pixels[x + y * w];            // x + y * width
                redBucket   += (color >> 16) & 0xFF;         // Color.red
                greenBucket += (color >> 8) & 0xFF;          // Color.greed
                blueBucket  += (color & 0xFF);               // Color.blue
                if (hasAlpha) alphaBucket += (color >>> 24); // Color.alpha
            }
        }

        return Color.argb(
                (hasAlpha) ? (alphaBucket / pixelCount) : 255,
                redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount
        );
    }


/*    public void nextClick(View view) {

        startActivity(intent);
        intent.putExtra(EVENT_ID, Long.valueOf(1));
    }*/


}

class TabAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> titulos = new ArrayList<>();

    TabAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new InfoFragment());
        fragments.add(new ValoracionesFragment());
        titulos.add("Info");
        titulos.add("Valoraciones");
    }


    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titulos.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return titulos.get(position);
    }

    @Override
    public int getCount() {

        return fragments.size();
    }
}
