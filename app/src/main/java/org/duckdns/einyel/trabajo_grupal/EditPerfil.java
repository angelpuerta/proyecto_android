package org.duckdns.einyel.trabajo_grupal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.List;

public class EditPerfil extends AppCompatActivity {

    EditText textNombre;
    Spinner spinnerSexo;
    EditText textActualPw;
    Button changeEdad;
    protected TextView tvEdad;
    public static String socialLogin = "";
    public static String username = "";
    public static String TWITTERID = "";
    public static String FACEBOOKID = "";
    public static Long ID = 0L;

    protected DatePickerDialog.OnDateSetListener dateSetListener;

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
        ID=extras.getLong("id");

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
        changeEdad = (Button) findViewById(R.id.buttonChangeEdad);
        tvEdad = (TextView)  findViewById(R.id.tvEditEdad);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("usuarios");
        usersSociales = database.getReference("usuariossociales");

        changeEdad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditPerfil.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                tvEdad.setText(i2+"/"+(i1+1)+"/"+i);
            }
        };

        Intent intent = new Intent(this, Perfil.class);

        if(socialLogin.equals("android")){
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        if(userSnapshot.child("id").getValue().equals(ID)){
                            user = new User((Long)userSnapshot.child("id").getValue(), userSnapshot.child("nick").getValue().toString(),
                                    userSnapshot.child("password").getValue().toString(), userSnapshot.child("sexo").getValue().toString());
                            user.setNacimiento(userSnapshot.child("nacimiento").getValue().toString());
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
                    tvEdad.setText(user.getNacimiento());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //not implemented
                }
            });
        }
        else {
            textActualPw.setEnabled(false);
            usersSociales.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(socialLogin.equals("twitter")) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            {
                                Log.d("Exception", "DATOS: " + TWITTERID + " " + userSnapshot.child("twUsername").getValue() + " " + TWITTERID + " " + userSnapshot.child("twUsername").getValue().equals(TWITTERID));
                                if (userSnapshot.child("twUsername").getValue().equals(TWITTERID)) {
                                    user = new User((Long) userSnapshot.child("id").getValue(), userSnapshot.child("nick").getValue().toString(),
                                            userSnapshot.child("password").getValue().toString(), userSnapshot.child("sexo").getValue().toString());
                                    user.setNacimiento(userSnapshot.child("nacimiento").getValue().toString());
                                    break;
                                }
                            }
                        }

                        textNombre.setText(user.getNick());
                        tvEdad.setText(user.getNacimiento());
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
                                            if(userSnapshot.child("id").getValue()!=null)
                                                if (userSnapshot.child("id").getValue().equals(user.getId())) {
                                                    setResult(112, null);
                                                    finish();
                                                    userSnapshot.getRef().child("nick").setValue(usernameElegido);
                                                    userSnapshot.getRef().child("sexo").setValue(spinnerSexo.getSelectedItem().toString());
                                                    userSnapshot.getRef().child("nacimiento").setValue(tvEdad.getText());
                                                    Toast.makeText(EditPerfil.this, "Cambios realizados",
                                                            Toast.LENGTH_SHORT).show();
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
                    else {
                        Toast.makeText(EditPerfil.this, "La contraseña actual es incorrecta",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    String usernameElegido = textNombre.getText().toString();


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
                            usersSociales.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        if(userSnapshot.child("id").getValue()!=null) {
                                            if (userSnapshot.child("id").getValue().equals(user.getId())) {
                                                setResult(112, null);
                                                finish();
                                                userSnapshot.getRef().child("nick").setValue(usernameElegido);
                                                userSnapshot.getRef().child("sexo").setValue(spinnerSexo.getSelectedItem().toString());
                                                userSnapshot.getRef().child("nacimiento").setValue(tvEdad.getText());
                                                Toast.makeText(EditPerfil.this, "Cambios realizados",
                                                        Toast.LENGTH_SHORT).show();
                                            }
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
