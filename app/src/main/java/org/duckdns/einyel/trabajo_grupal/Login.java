package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.reactivex.disposables.CompositeDisposable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.duckdns.einyel.trabajo_grupal.model.User;

import java.util.List;
import java.util.ArrayList;


public class Login extends AppCompatActivity {

    protected EditText pw;
    protected EditText user;

    FirebaseDatabase database;
    DatabaseReference users;

    public static final String NOMBRE_USUARIO = "";

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        pw = findViewById(R.id.editPassword);
        user = findViewById(R.id.editUser);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("usuarios");


    }


    public void checkUser(View view) {
        String Spassword = pw.getText().toString();
        String Suser = user.getText().toString();
        signIn(Suser, Spassword);
    }

    protected void signIn(final String username, final String password){
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User usuario = null;
                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    if(userSnapshot.child("nick").getValue().toString().equals(username)) {
                        String id = userSnapshot.child("id").getValue().toString();
                        usuario = new User(Long.parseLong(id),
                                userSnapshot.child("nick").getValue().toString(),
                                userSnapshot.child("password").getValue().toString());
                    }
                }

                if(!username.isEmpty()){
                    if(usuario != null){
                        if(usuario.getPassword().equals(password)){
                            Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                            mIntent.putExtra(NOMBRE_USUARIO, user.getText().toString());

                            startActivity(mIntent);
                        }
                        else {
                            Toast.makeText(Login.this, "Credenciales incorrectas",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Login.this, "Credenciales incorrectas",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Login.this, "Debe introducir un nombre de usuario",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //not implemented
            }
        });
    }
}
