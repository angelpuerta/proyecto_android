package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import org.duckdns.einyel.trabajo_grupal.service.UserHolder;


public class Perfil extends AppCompatActivity {

    public static String NOMBRE_USUARIO = "";
    public static String LOGIN = "";
    public static String URL_PIC = "";
    public static String SEXO = "";
    public static String TWITTERID = "";
    public static String FACEBOOKID = "";
    public static Long ID = 0L;

    private ImageView imgProfile;
    private Button logOut;
    private Button edit;
    private Button change;
    private TextView tvUserName;
    private TextView tvBirthdateProfile;
    private TextView tvGenderProfile;

    FirebaseDatabase database;
    DatabaseReference users;
    DatabaseReference usersSocial;

    private final static String BASE_PROFILE_IMG = "https://firebasestorage.googleapis.com/v0/b/proyecto-moviles-86dc4.appspot.com/o/profile_icons%";
    private final static String PROFILE_IMG_1 = BASE_PROFILE_IMG + "2F1.png?alt=media&token=d375045b-a13e-47c2-b1dc-5004d9903f5b";
    private final static String PROFILE_IMG_2 = BASE_PROFILE_IMG + "2F2.png?alt=media&token=d2cbd9b2-6046-4776-885e-002f0cdd1e6b";
    private final static String PROFILE_IMG_3 = BASE_PROFILE_IMG + "2F3.png?alt=media&token=a848c3dd-e6c6-4c82-acbb-810ee3cddaec" ;
    private final static String PROFILE_IMG_4 = BASE_PROFILE_IMG + "2F4.png?alt=media&token=ed556508-9459-4618-95d8-703148108a74";
    private final static String PROFILE_IMG_5 = BASE_PROFILE_IMG + "2F5.png?alt=media&token=6291b277-70b6-4c7f-b943-648950bfa480";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        imgProfile = (ImageView) findViewById(R.id.profileImage);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvGenderProfile = (TextView) findViewById(R.id.tvGenderProfile);
        tvBirthdateProfile = (TextView) findViewById(R.id.tvBirthdateProfile);
        edit = (Button) findViewById(R.id.buttoneditprofile);
        logOut = (Button) findViewById(R.id.LogOut);
        change = (Button) findViewById(R.id.buttonChangePw);
        change.setPaintFlags(change.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        database = FirebaseDatabase.getInstance();

        users = database.getReference("usuarios");
        usersSocial = database.getReference("usuariossociales");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            System.out.println("Extras: " + extras.getString("socialLogin") + " " + extras.getString("username"));
            LOGIN = UserHolder.getSocialLogin();
            NOMBRE_USUARIO = UserHolder.getUser().getNick();
            URL_PIC = UserHolder.getUser().getPfpUrl();
            SEXO = UserHolder.getUser().getSexo();
            ID = UserHolder.getUser().getId();
            TWITTERID = UserHolder.getUser().getTwUsername();
            FACEBOOKID = UserHolder.getUser().getFbId();
        }
        if (LOGIN.equals("android")) {
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User usuario = null;
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
                    tvUserName.setText(usuario.getNick());
                    tvGenderProfile.setText(usuario.getSexo());
                    tvBirthdateProfile.setText(usuario.getNacimiento());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //not implemented
                }
            });
        } else {
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
                    Log.d("Exception aaaaa:", usuario.getPfpUrl());

                    new DownloadImageTask((ImageView) findViewById(R.id.profileImage))
                            .execute(usuario.getPfpUrl());
                    tvUserName.setText(usuario.getNick());
                    tvGenderProfile.setText(usuario.getSexo());
                    tvBirthdateProfile.setText(usuario.getNacimiento());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //not implemented
                }
            });
        }


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mintent = new Intent(getApplicationContext(), EditPerfil.class);
                mintent.putExtra("socialLogin", LOGIN);
                mintent.putExtra("username", NOMBRE_USUARIO);
                mintent.putExtra("id", ID);
                if (LOGIN.equals("twitter")) {
                    mintent.putExtra("twitterId", TWITTERID);
                } else if (LOGIN.equals("facebook")) {
                    mintent.putExtra("facebookId", FACEBOOKID);
                }
                startActivityForResult(mintent, 111);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LOGIN.equals("android")) {
                    Intent mintent = new Intent(getApplicationContext(), ChangePw.class);
                    mintent.putExtra("socialLogin", LOGIN);
                    mintent.putExtra("username", NOMBRE_USUARIO);
                    mintent.putExtra("id", ID);
                    if (LOGIN.equals("twitter")) {
                        mintent.putExtra("twitterId", TWITTERID);
                    } else if (UserHolder.getSocialLogin().equals("facebook")) {
                        mintent.putExtra("facebookId", FACEBOOKID);
                    }
                    startActivityForResult(mintent, 111);
                } else {
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

        setProfileImage();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setProfileImage(){
        if (URL_PIC != null && !URL_PIC.equals("")) {
            if (LOGIN.equals("facebook") || LOGIN.equals("twitter")) {
                new DownloadImageTask((ImageView) findViewById(R.id.profileImage))
                        .execute(URL_PIC);
            }
        }
        else{
            double random = Math.random();
            Picasso.get().setLoggingEnabled(true);
            if(random >= 0 && random <= 0.20){
                Picasso.get().load(PROFILE_IMG_1).into(imgProfile);
            }else if(random > 0.20 && random <= 0.40){
                Picasso.get().load(PROFILE_IMG_2).into(imgProfile);
            }else if(random > 0.40 && random <= 0.60){
                Picasso.get().load(PROFILE_IMG_3).into(imgProfile);
            }else if(random > 0.60 && random <= 0.80){
                Picasso.get().load(PROFILE_IMG_4).into(imgProfile);
            }else {
                Picasso.get().load(PROFILE_IMG_5).into(imgProfile);
            }
        }
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
