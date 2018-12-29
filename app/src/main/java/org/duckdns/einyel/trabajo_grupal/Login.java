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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    protected EditText pw;
    protected EditText user;

    FirebaseDatabase database;
    DatabaseReference users;

    //Google
    GoogleApiClient googleApi;
    private SignInButton signInButtonGoogle;

    //Twitter
    TwitterLoginButton loginButtonTwitter;

    //Facebook
    CallbackManager callbackManager;
    ProgressDialog mDialog;
    ImageView imgAvatar;

    public static final String NOMBRE_USUARIO = "";
    public static final int GOOGLE_SIGN_IN_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if(AccessToken.getCurrentAccessToken() != null){
        //    Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
        //    mIntent.putExtra(NOMBRE_USUARIO, user.getText().toString());
        //    startActivity(mIntent);
        //}
        // else {
            super.onCreate(savedInstanceState);

            //la inicializacion de twitter (tiene que ir aqui si o si)
            TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.twitter_consumer_key), getResources().getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();

            Twitter.initialize(config);

            setContentView(R.layout.login_activity);

            //Inicializaciones de login social
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleApi = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            FacebookSdk.sdkInitialize(getApplicationContext());

            //Obtención de info basica (usuarios normales)
            pw = findViewById(R.id.editPassword);
            user = findViewById(R.id.editUser);

            database = FirebaseDatabase.getInstance();
            users = database.getReference("usuarios");

            //establecimiento de los procesos de login de las redes
            googleLoginProccess();
            facebookLoginProccess();
            twitterLoginProccess();

            //printKeyHash();
            //}
    }

    private void twitterLoginProccess() {
        loginButtonTwitter = (TwitterLoginButton) findViewById(R.id.twLogin);
        loginButtonTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                mIntent.putExtra(NOMBRE_USUARIO, "Kibian");
                startActivity(mIntent);
            }

            @Override
            public void failure(TwitterException exception) {
                System.err.println("Hola " + exception);
            }
        });
    }

    protected void facebookLoginProccess(){
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.fbLogin);
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
    }

    protected void googleLoginProccess() {
        signInButtonGoogle = findViewById(R.id.googleLogin);
        signInButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApi);
                startActivityForResult(intent, GOOGLE_SIGN_IN_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //fb y twitter
        super.onActivityResult(requestCode, resultCode, data);
        //google
        if(requestCode==GOOGLE_SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            System.out.println("StatusCode: " + result.getStatus().getStatusCode());
            if (result.isSuccess()) {
                Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                mIntent.putExtra(NOMBRE_USUARIO, "Kibian");
                startActivity(mIntent);
            }
            else {
                Toast.makeText(Login.this, "No se ha podido iniciar sesión",
                        Toast.LENGTH_SHORT).show();
            }
        }
        //twitter
        loginButtonTwitter.onActivityResult(requestCode, resultCode, data);
        //fb
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //  @Override
  //  protected void onDestroy(){
  //      System.out.println("destroymyfriend");
  //      super.onDestroy();
  //       LoginManager.getInstance().logOut();
  // }

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

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("org.duckdns.einyel.trabajo_grupal",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature: info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();;
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
