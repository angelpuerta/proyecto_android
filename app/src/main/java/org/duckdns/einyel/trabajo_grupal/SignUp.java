package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.duckdns.einyel.trabajo_grupal.model.User;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity {
    protected EditText pw;
    protected EditText userF;
    protected EditText pwRepeat;
    protected Spinner spinner;

    FirebaseDatabase database;
    DatabaseReference users;

    public static final String NOMBRE_USUARIO = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        pw = findViewById(R.id.editPassword);
        userF = findViewById(R.id.editUser);
        pwRepeat = findViewById(R.id.editPasswordRepeat);
        spinner = findViewById(R.id.spinnerSexo);

        List<String> list = new ArrayList<String>();
        list.add("Hombre");
        list.add("Mujer");
        list.add("Otro");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("usuarios");

    }

    public void checkUser(View view) {
        String Spassword = pw.getText().toString();
        String Suser = userF.getText().toString();
        String SpasswordRepeat = pwRepeat.getText().toString();
        String SSexo = spinner.getSelectedItem().toString();
        signUp(Suser, Spassword, SpasswordRepeat, SSexo);
    }

    public void signUp(String username, String password, String passwordRepeat, String sexo){
        users.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long id = 0L;
                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    if(userSnapshot.child("id").getValue()!=null)
                         id = Long.valueOf(userSnapshot.child("id").getValue().toString());
                }
                if(id!=0L) {
                    if(!username.isEmpty()&&!password.isEmpty()&&!passwordRepeat.isEmpty()){
                        if (password.equals(passwordRepeat)) {
                            Long newId = id+1;
                            final User user = new User(newId, username, password, sexo);
                            List<String> usernames = new ArrayList<>();
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                if(userSnapshot.child("nick").getValue()!=null)
                                    usernames.add(userSnapshot.child("nick").getValue().toString());
                            }
                            if (!usernames.contains(username)) {
                                users.child(newId.toString()).setValue(user);
                                Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                                mIntent.putExtra("socialLogin", "android");
                                mIntent.putExtra("username", username);
                                mIntent.putExtra("sexo", sexo);

                                startActivity(mIntent);

                                pw.setText("");
                                pwRepeat.setText("");
                                userF.setText("");
                            } else {
                                Toast.makeText(SignUp.this, "Ya existe una cuenta con ese " +
                                                "nombre de usuario",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUp.this, "Las contraseñas son diferentes",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(SignUp.this, "Debe rellenar todos los campos",
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

    public void goLogIn(View view){
        finish();
    }
}
