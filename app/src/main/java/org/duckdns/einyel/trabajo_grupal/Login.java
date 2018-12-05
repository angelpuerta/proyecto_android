package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.duckdns.einyel.trabajo_grupal.model.Comment;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;
import org.duckdns.einyel.trabajo_grupal.service.App;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Login extends AppCompatActivity {

    protected EditText pw;
    protected EditText user;

    public static final String NOMBRE_USUARIO = "";

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        pw =  findViewById(R.id.editPassword);
        user = findViewById(R.id.editUser);


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
