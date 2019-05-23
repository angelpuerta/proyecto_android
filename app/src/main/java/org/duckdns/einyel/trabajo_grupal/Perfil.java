package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.duckdns.einyel.trabajo_grupal.model.User;
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
    public static Long ID = 0L;

    private ImageView imgProfile;
    private TextView name;
    private Button logOut;
    private Button edit;
    private Button change;

    FirebaseDatabase database;
    DatabaseReference users;
    DatabaseReference usersSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        imgProfile = (ImageView) findViewById(R.id.profileImage);
        name = (TextView) findViewById(R.id.name);
        logOut = (Button) findViewById(R.id.LogOut);
        edit = (Button) findViewById(R.id.buttoneditprofile);
        change = (Button) findViewById(R.id.buttonChangePw);

        database = FirebaseDatabase.getInstance();

        users = database.getReference("usuarios");
        usersSocial = database.getReference("usuariossociales");

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            System.out.println("Extras: " + extras.getString("socialLogin") + " " + extras.getString("username"));
            LOGIN = extras.getString("socialLogin");
            NOMBRE_USUARIO = extras.getString("username");
            URL_PIC = extras.getString("imageUrl");
            SEXO = extras.getString("sexo");
            ID = extras.getLong("id");
            TWITTERID = extras.getString("twitterId");
            FACEBOOKID = extras.getString("facebookId");
        }
        if(LOGIN.equals("android")){
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User usuario = null;
                    String urlPic;
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        if (userSnapshot.child("id").getValue().equals(ID) && userSnapshot.hasChild("id")) {
                            usuario = new User(ID,
                                    userSnapshot.child("nick").getValue().toString(),
                                    userSnapshot.child("password").getValue().toString(),
                                    userSnapshot.child("sexo").getValue().toString(), "", "",
                                    userSnapshot.child("pfpUrl").getValue().toString(),
                                    userSnapshot.child("nacimiento").getValue().toString());
                            break;
                        }
                    }
                    new DownloadImageTask((ImageView) findViewById(R.id.socialImage))
                            .execute("https://cdn4.iconfinder.com/data/icons/iconsimple-logotypes/512/android-512.png");

                    name.setText("Nombre: " + usuario.getNick() + "\nSexo: " + usuario.getSexo() + "\nFecha de nacimiento: " + usuario.getNacimiento());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //not implemented
                }
            });
        }
        else {
            usersSocial.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User usuario = null;
                    String urlPic;
                    Log.d("Exception x:", ID.toString());
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        if (userSnapshot.child("id").getValue().equals(ID) && userSnapshot.hasChild("id")) {
                            usuario = new User(ID,
                                    userSnapshot.child("nick").getValue().toString(),
                                    userSnapshot.child("password").getValue().toString(),
                                    userSnapshot.child("sexo").getValue().toString(),
                                    userSnapshot.child("fbId").getValue().toString(),
                                    userSnapshot.child("twUsername").getValue().toString(),
                                    userSnapshot.child("pfpUrl").getValue().toString(),
                                    userSnapshot.child("nacimiento").getValue().toString());
                            break;
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
                    new DownloadImageTask((ImageView) findViewById(R.id.profileImage))
                            .execute(usuario.getPfpUrl());
                    name.setText("Nombre: " + usuario.getNick() + "\nSexo: " + usuario.getSexo() + "\nFecha de nacimiento: " + usuario.getNacimiento());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //not implemented
                }
            });
        }
        /**if(URL_PIC!=null) {
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
         }**/


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mintent = new Intent(getApplicationContext(), EditPerfil.class);
                mintent.putExtra("socialLogin", LOGIN);
                mintent.putExtra("username", NOMBRE_USUARIO);
                mintent.putExtra("id", ID);
                if(LOGIN.equals("twitter")){
                    mintent.putExtra("twitterId", TWITTERID);
                }
                else if (LOGIN.equals("facebook")){
                    mintent.putExtra("facebookId", FACEBOOKID);
                }
                startActivityForResult(mintent, 111);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LOGIN.equals("android")) {
                    Intent mintent = new Intent(getApplicationContext(), ChangePw.class);
                    mintent.putExtra("socialLogin", LOGIN);
                    mintent.putExtra("username", NOMBRE_USUARIO);
                    mintent.putExtra("id", ID);
                    if(LOGIN.equals("twitter")){
                        mintent.putExtra("twitterId", TWITTERID);
                    }
                    else if (LOGIN.equals("facebook")){
                        mintent.putExtra("facebookId", FACEBOOKID);
                    }
                    startActivityForResult(mintent, 111);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111) {
            if (resultCode == 112) {
                setResult(111, null);
                this.finish();
            }
        }
    }
}
