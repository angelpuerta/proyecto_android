package org.duckdns.einyel.trabajo_grupal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    protected EditText pw;
    protected EditText user;

    public static final String NOMBRE_USUARIO = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descripcion_angela_activity);

        pw = (EditText)findViewById(R.id.editPassword);
        user = (EditText)findViewById(R.id.editUser);
    }


    public void checkUser(View view){
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
