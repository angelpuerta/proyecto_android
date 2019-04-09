package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.duckdns.einyel.trabajo_grupal.model.User;
import org.duckdns.einyel.trabajo_grupal.service.DownloadImageTask;

import java.util.ArrayList;
import java.util.List;

public class EditPerfil extends AppCompatActivity {

    EditText textNombre;
    Spinner spinnerSexo;
    EditText textActualPw;
    public static String socialLogin = "";
    public static String username = "";
    public static String TWITTERID = "";
    public static String FACEBOOKID = "";

    public User user;

    Button cancel, confirm;

    FirebaseDatabase database;
    DatabaseReference users;
    DatabaseReference usersSociales;

    public boolean taken = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        Bundle extras = getIntent().getExtras();

        socialLogin = extras.getString("socialLogin");
        username = extras.getString("username");
        TWITTERID = extras.getString("twitterId");
        FACEBOOKID = extras.getString("facebookId");

        List<String> list = new ArrayList<String>();
        list.add("Hombre");
        list.add("Mujer");
        list.add("Otro");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cancel = (Button) findViewById(R.id.buttonCancel);
        confirm = (Button) findViewById(R.id.buttonConfirm);
        textNombre = (EditText) findViewById(R.id.editTextNombre);
        spinnerSexo = (Spinner) findViewById(R.id.spinnerSexoEdit);
        textActualPw = (EditText) findViewById(R.id.textPwActualChange);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("usuarios");
        usersSociales = database.getReference("usuariossociales");

        if(socialLogin.equals("android")){
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        if(userSnapshot.child("nick").getValue().equals(username)){
                            user = new User((Long)userSnapshot.child("id").getValue(), userSnapshot.child("nick").getValue().toString(),
                                    userSnapshot.child("password").getValue().toString(), userSnapshot.child("sexo").getValue().toString());
                            break;
                        }
                    }

                    textNombre.setText(user.getNick());
                    Log.d("Exception", user.getSexo());
                    spinnerSexo.setAdapter(dataAdapter);
                    if(user.getSexo().equals("Hombre"))
                        spinnerSexo.setSelection(dataAdapter.getPosition("Hombre"));
                    else if(user.getSexo().equals("Mujer"))
                        spinnerSexo.setSelection(dataAdapter.getPosition("Mujer"));
                    else
                        spinnerSexo.setSelection(dataAdapter.getPosition("Otro"));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //not implemented
                }
            });
        }
        else {
            usersSociales.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(socialLogin.equals("twitter")) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            {
                                Log.d("Exception", "DATOS: " + userSnapshot.child("twUsername").getValue() + " " + TWITTERID + " " + userSnapshot.child("twUsername").getValue().equals(TWITTERID));
                                if (userSnapshot.child("twUsername").getValue().equals(TWITTERID)) {
                                    user = new User((Long) userSnapshot.child("id").getValue(), userSnapshot.child("nick").getValue().toString(),
                                            userSnapshot.child("password").getValue().toString(), userSnapshot.child("sexo").getValue().toString());
                                    break;
                                }
                            }
                        }

                        textNombre.setText(user.getNick());

                        spinnerSexo.setAdapter(dataAdapter);
                        if(user.getSexo().equals("Otro"))
                            spinnerSexo.setSelection(dataAdapter.getPosition("Otro"));
                        else if(user.getSexo().equals("Mujer"))
                            spinnerSexo.setSelection(dataAdapter.getPosition("Mujer"));
                        else
                            spinnerSexo.setSelection(dataAdapter.getPosition("Hombre"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //not implemented
                }
            });
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(socialLogin.equals("android")) {
                    if (textActualPw.getText().toString().equals(user.getPassword())) {
                        String usernameElegido = textNombre.getText().toString();
                        if(!usernameElegido.equals(user.getNick())) {

                            users.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        if (userSnapshot.child("nick").getValue().equals(usernameElegido)) {
                                            taken = true;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    //not implemented
                                }
                            });

                            usersSociales.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        if (userSnapshot.child("nick").getValue().equals(usernameElegido)) {
                                            taken = true;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    //not implemented
                                }
                            });

                            if(taken){
                                Toast.makeText(EditPerfil.this, "Ya existe un usuario con ese nick de usuario",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                users.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                            if (userSnapshot.child("id").getValue().equals(user.getId())) {
                                                userSnapshot.child("nick");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        //not implemented
                                    }
                                });
                            }

                            taken = false;
                        }
                    } else {
                        Toast.makeText(EditPerfil.this, "La contraseña actual es incorrecta",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
