package org.duckdns.einyel.trabajo_grupal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.duckdns.einyel.trabajo_grupal.model.User;

public class ChangePw extends AppCompatActivity {

    EditText textActualPw, textPwChange, textPwChangeRepeat;
    Button cancel, confirm;

    FirebaseDatabase database;
    DatabaseReference users;
    public User user;

    public static String NOMBRE_USUARIO = "";
    public static String LOGIN = "";
    public static String URL_PIC = "";
    public static String SEXO = "";
    public static String TWITTERID = "";
    public static String FACEBOOKID = "";
    public static Long ID = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pw_activity);

        Bundle extras = getIntent().getExtras();

        cancel = (Button) findViewById(R.id.buttonCancel2);
        confirm = (Button) findViewById(R.id.buttonConfirm2);
        textPwChange = (EditText) findViewById(R.id.textNewPwChange);
        textPwChangeRepeat = (EditText) findViewById(R.id.textNewPwChangeRepeat);
        textActualPw = (EditText) findViewById(R.id.textPwActualChange2);
        ID=extras.getLong("id");

        database = FirebaseDatabase.getInstance();
        users = database.getReference("usuarios");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textChange = textPwChange.getText().toString();
                String secondTextChange = textPwChangeRepeat.getText().toString();
                String actualPw = textActualPw.getText().toString();

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

                        if(textChange.equals(secondTextChange)){
                            if(actualPw.equals(user.getPassword())) {
                                if (!actualPw.equals(textChange)) {
                                    for(DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                        if(userSnapshot.child("id").getValue().equals(user.getId())){
                                            setResult(112, null);
                                            finish();
                                            userSnapshot.getRef().child("password").setValue(textChange);
                                            Toast.makeText(ChangePw.this, "Contraseña cambiada",
                                                    Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(ChangePw.this, "La nueva contraseña es igual a la anterior",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(ChangePw.this, "La contraseña actual es incorrecta",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(ChangePw.this, "La nueva contraseña no es igual en ambos campos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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
