package org.duckdns.einyel.trabajo_grupal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.duckdns.einyel.trabajo_grupal.model.User;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.util.Base64;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class Login extends AppCompatActivity {

    protected EditText pw;
    protected EditText user;

    FirebaseDatabase database;
    DatabaseReference users;

    CallbackManager callbackManager;
    ProgressDialog mDialog;
    ImageView imgAvatar;

    public static final String NOMBRE_USUARIO = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        FacebookSdk.sdkInitialize(getApplicationContext());

        pw = findViewById(R.id.editPassword);
        user = findViewById(R.id.editUser);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("usuarios");

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton)findViewById(R.id.fbLogin);
        loginButton.setReadPermissions(Arrays.asList("public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                mIntent.putExtra(NOMBRE_USUARIO, "Kibian");
                startActivity(mIntent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //printKeyHash();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void printKeyHash() {
        try {
            System.out.println("Llego1");
            PackageInfo info = getPackageManager().getPackageInfo("org.duckdns.einyel.trabajo_grupal",
                    PackageManager.GET_SIGNATURES);
            System.out.println("Llego2");
            for(Signature signature: info.signatures){
                System.out.println("Llego3");
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("Llego4");
            }
            System.out.println("Llego5");
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();;
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
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

    protected void goSignUp(View view){
        Intent mIntent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(mIntent);
    }

}
