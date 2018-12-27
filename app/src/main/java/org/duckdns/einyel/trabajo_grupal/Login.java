package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;
import org.duckdns.einyel.trabajo_grupal.service.App;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class Login extends AppCompatActivity {

    protected EditText pw;
    protected EditText user;

    public static final String NOMBRE_USUARIO = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        pw = findViewById(R.id.editPassword);
        user = findViewById(R.id.editUser);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("comments")
                .child("1")
                .orderByChild("rate")
                .startAt(3)
                .limitToLast(50);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Comment firstChild = dataSnapshot.getChildren().iterator().next().getValue(Comment.class);
                System.out.println("The dinosaur just shorter than the stegosaurus is: " + firstChild);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void checkUser(View view) {
        String Spassword = pw.getText().toString();
        String Suser = user.getText().toString();
        //if(Spassword.equals("12345") && Suser.equals("Usuario")){
        Intent mIntent = new Intent(this, ListActivity.class);
        mIntent.putExtra(NOMBRE_USUARIO, user.getText().toString());

        startActivity(mIntent);

        //}
        //else {
        //    Toast.makeText(getApplicationContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        //}
    }
}
