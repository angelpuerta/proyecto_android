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
        setContentView(R.layout.login_activity);

        pw = (EditText)findViewById(R.id.editPassword);
        user = (EditText)findViewById(R.id.editUser);
    }


    public void checkUser(View view){
        String password = pw.getText().toString();
        String user = user.getText().toString();
        if(password.equals("12345") && user.equals("Usuario")){
            Intent mIntent = new Intent(this, ListActivity.class);
			NOMBRE_USUARIO = user;
            mIntent.putExtra(NOMBRE_USUARIO, user.getText().toString());
            startActivity(mIntent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }
}
