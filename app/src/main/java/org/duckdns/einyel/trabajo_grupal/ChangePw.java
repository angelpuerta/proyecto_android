package org.duckdns.einyel.trabajo_grupal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangePw extends AppCompatActivity {

    EditText textActualPw, textPwChange, textPwChangeRepeat;
    Button cancel, confirm;

    public static String NOMBRE_USUARIO = "";
    public static String LOGIN = "";
    public static String URL_PIC = "";
    public static String SEXO = "";
    public static String TWITTERID = "";
    public static String FACEBOOKID = "";

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



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
