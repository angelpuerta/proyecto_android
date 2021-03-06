package org.duckdns.einyel.trabajo_grupal;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import org.duckdns.einyel.trabajo_grupal.adapter.EventoAdapter;
import org.duckdns.einyel.trabajo_grupal.listener.RecyclerTouchListener;
import org.duckdns.einyel.trabajo_grupal.model.Event;
import org.duckdns.einyel.trabajo_grupal.model.User;
import org.duckdns.einyel.trabajo_grupal.service.App;


public class ListActivity extends AppCompatActivity implements EventoAdapter.Assisted{


    public static final String EVENTO = "EVENTO";
    public static String USERNAME = "";
    public static String LOGIN = "";
    public static String URL_PIC = "";
    public static String SEXO = "";
    public static String BUSCAR = "";
    public static String TWITTERID = "";
    public static String FACEBOOKID = "";
    public static String FILTRO = "";
    public static Long ID = 0L;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private int isOnCreate = 0;
    private User usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            BUSCAR = extras.getString("buscar");
            LOGIN = extras.getString("socialLogin");
            if(LOGIN.equals("twitter")){
                TWITTERID = extras.getString("twitterId");
            }
            else if (LOGIN.equals("facebook")){
                FACEBOOKID = extras.getString("facebookId");
            }
            USERNAME = extras.getString("username");
            SEXO = extras.getString("sexo");
            URL_PIC = extras.getString("imageUrl");
            ID = extras.getLong("id");
            FILTRO = extras.getString("filtro");
            usuario = extras.getParcelable(Login.USER);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        //profileButton = (ImageButton) findViewById(R.id.profileButton);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setRecyclerViewAdapter();


        setSpinnerCategoryFilterAdapter();

    }

    private void startSearchView(){
        SearchView searchView = findViewById(R.id.app_bar_search);
    }

    private void setSpinnerCategoryFilterAdapter() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setSelection(chooseItemByFilter());
        spinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = (String) parent.getItemAtPosition(position);
                    if(item != null && ++isOnCreate > 1) {
                        Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                        mIntent.putExtra("socialLogin", LOGIN);
                        mIntent.putExtra("username", USERNAME);
                        mIntent.putExtra("filtro", item.toLowerCase());
                        mIntent.putExtra("imageUrl", URL_PIC);
                        mIntent.putExtra(Login.USER, usuario);
                        startActivity(mIntent);
                        finish();
                    }
                }
                public void onNothingSelected(AdapterView<?> spn) {
                }
            });
    }

    private int chooseItemByFilter(){
        if(FILTRO!= null && FILTRO != "todo"){
            switch (FILTRO){
                case "ambiental":
                    return 0;
                case "cultura":
                    return 1;
                case "educación e investigación":
                    return 2;
                case "social":
                    return 3;
            }
        }
        return 4;
    }

    private void setRecyclerViewAdapter() {
        if(FILTRO != null){
            if(FILTRO.equals("todo")) {
                adapter = new EventoAdapter(App.get().eventsOptions(), this);
            }else{
                adapter = new EventoAdapter(App.get().filtrarEventosPorCategoria(FILTRO),this);
            }
        }

        if(BUSCAR != null){
            adapter = new EventoAdapter(App.get().filtrarEventos(BUSCAR),this);
        }

        filtrarEventos();

    }

    private void filtrarEventos(){

        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Event evento = (Event) adapter.getItem(position);
                        Intent nextActivity = new Intent(getApplicationContext(), DescripcionActivity.class);
                        nextActivity.putExtra(EVENTO, evento);
                        //USERNAME = extras.getString("username");
                        nextActivity.putExtra("imageUrl", URL_PIC);
                        nextActivity.putExtra("socialLogin", LOGIN);
                        nextActivity.putExtra("username", USERNAME);
                        startActivity(nextActivity);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                })
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                    mIntent.putExtra("socialLogin", LOGIN);
                    mIntent.putExtra("username", USERNAME);
                    mIntent.putExtra("imageUrl", URL_PIC);
                    mIntent.putExtra("buscar", query);
                    mIntent.putExtra(Login.USER, usuario);
                    startActivity(mIntent);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_profile:
                Bundle extras = getIntent().getExtras();
                LOGIN = extras.getString("socialLogin");
                USERNAME = extras.getString("username");
                URL_PIC = extras.getString("imageUrl");
                SEXO = extras.getString("sexo");
                Intent mIntent = new Intent(getApplicationContext(), Perfil.class);
                mIntent.putExtra("imageUrl", URL_PIC);
                mIntent.putExtra("username", USERNAME);
                mIntent.putExtra("socialLogin", LOGIN);
                mIntent.putExtra("sexo", SEXO);
                mIntent.putExtra("id", ID);
                if(LOGIN.equals("twitter")){
                    mIntent.putExtra("twitterId", TWITTERID);
                }
                else if (LOGIN.equals("facebook")){
                    mIntent.putExtra("facebookId", FACEBOOKID);
                }
                startActivityForResult(mIntent, 100);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean hasAssisted(Long id) {
        return usuario.getAssisted().stream().anyMatch(x-> x.equals(id));
    }
}
