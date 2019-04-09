package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.duckdns.einyel.trabajo_grupal.service.DownloadImageTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Perfil extends AppCompatActivity {

    public static String NOMBRE_USUARIO = "";
    public static String LOGIN = "";
    public static String URL_PIC = "";
    public static String SEXO = "";
    public static String TWITTERID = "";
    public static String FACEBOOKID = "";

    private ImageView imgProfile;
    private TextView name;
    private Button logOut;
    private Button edit;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        imgProfile = (ImageView) findViewById(R.id.profileImage);
        name = (TextView) findViewById(R.id.name);
        logOut = (Button) findViewById(R.id.LogOut);
        edit = (Button) findViewById(R.id.buttoneditprofile);
        change = (Button) findViewById(R.id.buttonChangePw);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            System.out.println("Extras: " + extras.getString("socialLogin") + " " + extras.getString("username"));
            LOGIN = extras.getString("socialLogin");
            NOMBRE_USUARIO = extras.getString("username");
            URL_PIC = extras.getString("imageUrl");
            SEXO = extras.getString("sexo");
            if(LOGIN.equals("twitter")){
                TWITTERID = extras.getString("twitterId");
            }
            else if (LOGIN.equals("facebook")){
                FACEBOOKID = extras.getString("facebookId");
            }
        }
        if(URL_PIC!=null) {
            if (LOGIN.equals("facebook") || LOGIN.equals("twitter") || LOGIN.equals("google")) {
                new DownloadImageTask((ImageView) findViewById(R.id.profileImage))
                        .execute(URL_PIC);
            }
        }

        if(LOGIN.equals("twitter")){
            new DownloadImageTask((ImageView) findViewById(R.id.socialImage))
                    .execute("https://vignette.wikia.nocookie.net/es.starwars/images/9/92/Twitter_Icon.png");
        }
        else if(LOGIN.equals("facebook")){
            new DownloadImageTask((ImageView) findViewById(R.id.socialImage))
                    .execute("https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Facebook_logo_%28square%29.png/600px-Facebook_logo_%28square%29.png");
        }
        else if(LOGIN.equals("google")){
            new DownloadImageTask((ImageView) findViewById(R.id.socialImage))
                    .execute("https://cdn4.iconfinder.com/data/icons/new-google-logo-2015/400/new-google-favicon-512.png");
        }
        else {
            new DownloadImageTask((ImageView) findViewById(R.id.socialImage))
                    .execute("https://cdn4.iconfinder.com/data/icons/iconsimple-logotypes/512/android-512.png");
        }
        name.setText("Nombre: " + NOMBRE_USUARIO + "\nSexo: " + SEXO);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mintent = new Intent(getApplicationContext(), EditPerfil.class);
                mintent.putExtra("socialLogin", LOGIN);
                mintent.putExtra("username", NOMBRE_USUARIO);
                if(LOGIN.equals("twitter")){
                    mintent.putExtra("twitterId", TWITTERID);
                }
                else if (LOGIN.equals("facebook")){
                    mintent.putExtra("facebookId", FACEBOOKID);
                }
                startActivity(mintent);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LOGIN.equals("android")) {
                    Intent mintent = new Intent(getApplicationContext(), ChangePw.class);
                    mintent.putExtra("socialLogin", LOGIN);
                    mintent.putExtra("username", NOMBRE_USUARIO);
                    if(LOGIN.equals("twitter")){
                        mintent.putExtra("twitterId", TWITTERID);
                    }
                    else if (LOGIN.equals("facebook")){
                        mintent.putExtra("facebookId", FACEBOOKID);
                    }
                    startActivity(mintent);
                }
                else {
                    Toast.makeText(Perfil.this, "Esta función no está disponible para " +
                                    "inicios de sesión desde otras aplicaciones",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });



        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                Intent mIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(mIntent);
            }
        });
    }
}
