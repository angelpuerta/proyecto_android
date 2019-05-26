package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.duckdns.einyel.trabajo_grupal.fragments.InfoFragment;
import org.duckdns.einyel.trabajo_grupal.fragments.ValoracionesFragment;
import org.duckdns.einyel.trabajo_grupal.model.Event;
import org.duckdns.einyel.trabajo_grupal.service.Check;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DescripcionActivity extends AppCompatActivity {

    Intent intent;

    public static final String EVENTO = "EVENTO";
    public static String NOMBRE_USUARIO = "";
    public static String LOGIN = "";
    public static String URL_PIC = "";
    public static String BUSCAR = "";

    private Bitmap bm = null;
    Event evento = null;
    TabAdapter tab;


    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descripcion_activity);


        intent = new Intent(this, RankingActivity.class);

        Bundle b = getIntent().getExtras();
        evento = b.getParcelable(ListActivity.EVENTO);
        username = b.getString("username");

        iniciarTabLayout();

    }

    public String getUsername() {
        return username;
    }

    public void abrirMapaGrande(View view) {

        //Coordenadas
        String[] latLng = evento.getLocation().split(",");

        String uri = "http://maps.google.com/maps?saddr=&daddr=" + latLng[0] + "," + latLng[1];
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);


    }


    public Event getEvento() {
        return this.evento;
    }


    public void valorar(View view) {
        Intent nextActivity = new Intent(getApplicationContext(), RankingActivity.class);
        nextActivity.putExtra(EVENTO, evento.getId());
        nextActivity.putExtra("username", getUsername());
        startActivity(nextActivity);

    }

    public void checkIn(View view) {
        Intent nextActivity = new Intent(getApplicationContext(), QRCodeActivity.class);
        nextActivity.putExtra(EVENTO, evento);
        nextActivity.putExtra("username", getUsername());
        startActivity(nextActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.description_menu, menu);


        /*MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                mIntent.putExtra("imageUrl", URL_PIC);
                mIntent.putExtra("username", USERNAME);
                mIntent.putExtra("socialLogin", LOGIN);
                mIntent.putExtra("buscar", query);
                startActivityForResult(mIntent, 100);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchableActivity.class)));
        searchView.setIconifiedByDefault(false);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_profile:
                Bundle extras = getIntent().getExtras();
                LOGIN = extras.getString("socialLogin");
                NOMBRE_USUARIO = extras.getString("username");
                URL_PIC = extras.getString("imageUrl");
                Intent mIntent = new Intent(getApplicationContext(), Perfil.class);
                mIntent.putExtra("imageUrl", URL_PIC);
                mIntent.putExtra("username", NOMBRE_USUARIO);
                mIntent.putExtra("socialLogin", LOGIN);
                startActivityForResult(mIntent, 100);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void iniciarTabLayout() {

        Picasso.get().load(evento.getImgURL()).into((ImageView) findViewById(R.id.imageViewDescripcion));
        setTitle(evento.getTittle());
        //TextView titulo = findViewById(R.id.tituloDescripcion);
        //titulo.setText(evento.getTittle());

        cargarBitmap();

        TabLayout tl = (TabLayout) findViewById(R.id.tabs);
        tl.setBackgroundColor(getDominantColor(bm));

        tab = new TabAdapter(getSupportFragmentManager(), evento.getId());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(tab);
        tl.setupWithViewPager(viewPager);

        changeColorTextDarkLight(tl, getDominantColor(bm));

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        // TODO
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getDominantColor(bm)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void cargarBitmap() {
        Picasso.get().load(evento.getImgURL()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                bm = bitmap;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }


    /**
     * Metodo que cambia de color al texto del titulo de los TabItem dependiendo de si el
     * color dominante de la imagen es claro u oscuro
     *
     * @param tl
     * @param dominantColor
     */
    private void changeColorTextDarkLight(TabLayout tl, int dominantColor) {
        int red = Color.red(dominantColor);
        int green = Color.green(dominantColor);
        int blue = Color.blue(dominantColor);

        int selectedColor = (red + green + blue > 255 * 3 / 2) ?
                R.color.black :
                R.color.white;

        int shadowColor = (selectedColor == R.color.black) ?
                R.color.white :
                R.color.black;

        //TextView titulo = (TextView) findViewById(R.id.tituloDescripcion);
        //titulo.setTextColor(getResources().getColor(selectedColor));
        //titulo.setShadowLayer(2, 3, 3, getResources().getColor(shadowColor));
        //titulo.setBackgroundColor(dominantColor);
        //titulo.getBackground().setAlpha(190);


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

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];

        bitmap.getPixels(
                pixels,
                0,
                bitmap.getWidth(),
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight()
        );

        for (int y = 0, h = bitmap.getHeight(); y < h; y++) {
            for (int x = 0, w = bitmap.getWidth(); x < w; x++) {
                int color = pixels[x + y * w];            // x + y * width
                redBucket += (color >> 16) & 0xFF;         // Color.red
                greenBucket += (color >> 8) & 0xFF;          // Color.greed
                blueBucket += (color & 0xFF);               // Color.blue
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


}

class TabAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> titulos = new ArrayList<>();

    private ValoracionesFragment valoraciones;


    TabAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new InfoFragment());
        valoraciones = new ValoracionesFragment();
        fragments.add(valoraciones);
        titulos.add("Info");
        titulos.add("Valoraciones");
    }

    public TabAdapter(FragmentManager supportFragmentManager, Long id) {
        super(supportFragmentManager);
        fragments.add(new InfoFragment());
        valoraciones = new ValoracionesFragment();
        fragments.add(valoraciones);
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
