package org.duckdns.einyel.trabajo_grupal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.duckdns.einyel.trabajo_grupal.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.internal.TwitterApi;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    protected EditText pw;
    protected EditText user;

    Long id = 0L;

    FirebaseDatabase database;
    DatabaseReference users;
    DatabaseReference usersSocial;

    public static final String USER = "USER";

    //Google
    GoogleApiClient googleApi;
    private SignInButton signInButtonGoogle;
    private GoogleSignInClient mGoogleSignInClient;

    //Twitter
    TwitterLoginButton loginButtonTwitter;

    //Facebook
    CallbackManager callbackManager;
    ProgressDialog mDialog;
    ImageView imgAvatar;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private LoginButton fbButton;

    public static final String NOMBRE_USUARIO = "";
    public static final int GOOGLE_SIGN_IN_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //la inicializacion de twitter (tiene que ir aqui si o si)
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.twitter_consumer_key), getResources().getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();

        Twitter.initialize(config);

        setContentView(R.layout.login);
        Button button = (Button) findViewById(R.id.SignUp);
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        //Inicializaciones de login social
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleApi = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //Obtención de info basica (usuarios normales)
        pw = findViewById(R.id.password);
        user = findViewById(R.id.user);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("usuarios");
        usersSocial = database.getReference("usuariossociales");

        //establecimiento de los procesos de login de las redes
        facebookLoginProccess();
        twitterLoginProccess();

        //printKeyHash();
    }

    public void goFacebookLogin(View view) {
        LoginButton loginButton = (LoginButton)findViewById(R.id.fbLogin);
        loginButton.performClick();
    }

    public void goTwitterLogin(View view) {
        TwitterLoginButton loginButton = (TwitterLoginButton)findViewById(R.id.twLogin);
        loginButton.performClick();
    }

    public void goGoogleLogin(View view) {
        SignInButton loginButton = (SignInButton)findViewById(R.id.googleLogin);
        loginButton.performClick();
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

                String userName = result.data.getUserName();

                final retrofit2.Call<com.twitter.sdk.android.core.models.User> userTw =
                        TwitterCore.getInstance().getApiClient(session).getAccountService()
                                .verifyCredentials(true, false, false);

                userTw.enqueue(new Callback<com.twitter.sdk.android.core.models.User>() {
                    @Override
                    public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                        user.setText("");
                        pw.setText("");
                        List<String> usernames = new ArrayList<>();
                        usersSocial.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                    if(userSnapshot.child("id").getValue()!=null)
                                        id = Long.valueOf(userSnapshot.child("id").getValue().toString());
                                }
                                id = id+1;
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    if(userSnapshot.child("twUsername").getValue()!=null)
                                        usernames.add(userSnapshot.child("twUsername").getValue().toString());
                                }
                                com.twitter.sdk.android.core.models.User userInfo = result.data;
                                User twuser = null;
                                if(!usernames.contains(userName)){ //lo crea
                                    twuser = new User(id, userName, "", "Sin definir", "", userName, userInfo.profileImageUrl.replace("_normal", ""), "");
                                    usersSocial.child(id.toString()).setValue(twuser);
                                }

                                else { //ya existe
                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        if(userSnapshot.child("twUsername").getValue().toString().equals(userName)){
                                            twuser = new User((Long) userSnapshot.child("id").getValue(), userSnapshot.child("nick").getValue().toString(),
                                                    userSnapshot.child("password").getValue().toString(),
                                                    userSnapshot.child("sexo").getValue().toString(),
                                                    userSnapshot.child("fbId").getValue().toString(),
                                                    userSnapshot.child("twUsername").getValue().toString(),
                                                    userSnapshot.child("pfpUrl").getValue().toString(),
                                                    userSnapshot.child("nacimiento").getValue().toString());

                                        }
                                    }
                                }
                                String userUrlImage = userInfo.profileImageUrl.replace("_normal", "");
                                Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                                mIntent.putExtra("username", twuser.getNick());
                                mIntent.putExtra("imageUrl", twuser.getPfpUrl());
                                mIntent.putExtra("sexo", twuser.getSexo());
                                mIntent.putExtra("socialLogin", "twitter");
                                mIntent.putExtra("twitterId", twuser.getTwUsername());
                                mIntent.putExtra("id", twuser.getId());
                                mIntent.putExtra("filtro", "todo");
                                mIntent.putExtra(USER,twuser);
                                startActivity(mIntent);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                //not implemented
                            }
                        });


                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });

            }

            @Override
            public void failure(TwitterException exception) {
                System.err.println(exception);
            }
        });
    }

    protected void facebookLoginProccess() {
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.fbLogin);
        loginButton.clearPermissions();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "basic_info"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        user.setText("");
                        pw.setText("");

                        List<String> ids = new ArrayList<>();
                        usersSocial.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        if (userSnapshot.child("id").getValue() != null)
                                            id = Long.valueOf(userSnapshot.child("id").getValue().toString());
                                    }
                                    id = id + 1;
                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        if (userSnapshot.child("fbId").getValue() != null)
                                            ids.add(userSnapshot.child("fbId").getValue().toString());
                                    }
                                    User fbuser = new User(id, object.getString("name"), "", "Sin definir");
                                    fbuser.setFbId(object.getString("id"));
                                    fbuser.setPfpUrl("https://graph.facebook.com/" + object.getString("id") + "/picture?type=large");
                                    if (!ids.contains(object.getString("id"))) {

                                        usersSocial.child(id.toString()).setValue(fbuser);
                                    }
                                    Log.d("Exception", object.toString());
                                    Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                                    mIntent.putExtra("imageUrl", "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large");
                                    mIntent.putExtra("username", object.getString("name"));
                                    mIntent.putExtra("socialLogin", "facebook");
                                    mIntent.putExtra("facebookId", object.getString("id"));
                                    mIntent.putExtra("id", id);
                                    mIntent.putExtra("filtro", "todo");
                                    mIntent.putExtra(USER,fbuser);
                                    startActivity(mIntent);
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                //not implemented
                            }
                        });

                    }
                });

                Bundle bundle = new Bundle();
                bundle.putString("fields", "name, id");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //fb y twitter
        super.onActivityResult(requestCode, resultCode, data);
        //google
        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // Signed in successfully, show authenticated UI.
                user.setText("");
                pw.setText("");
                Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                if (account.getPhotoUrl() != null)
                    mIntent.putExtra("imageUrl", account.getPhotoUrl().toString());
                mIntent.putExtra("username", account.getDisplayName());
                mIntent.putExtra("socialLogin", "google");
                mIntent.putExtra("filtro", "todo");
                startActivity(mIntent);
            } catch (ApiException e) {
                Log.d("Excepcion", "Exception : " + e.getMessage());
            }
        }
        //twitter
        loginButtonTwitter.onActivityResult(requestCode, resultCode, data);
        //fb
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void checkUser(View view) {
        String Spassword = pw.getText().toString();
        String Suser = user.getText().toString();
        signIn(Suser, Spassword);
    }

    protected void signIn(final String username, final String password) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User usuario = null;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.child("nick").getValue().toString().equals(username) && userSnapshot.hasChild("id")) {
                        String id = userSnapshot.child("id").getValue().toString();
                        usuario = new User(Long.parseLong(id),
                                userSnapshot.child("nick").getValue().toString(),
                                userSnapshot.child("password").getValue().toString(),
                                userSnapshot.child("sexo").getValue().toString());
                        break;
                    }
                }

                if (!username.isEmpty()) {
                    if (usuario != null) {
                        if (usuario.getPassword().equals(password)) {
                            Intent mIntent = new Intent(getApplicationContext(), ListActivity.class);
                            mIntent.putExtra("socialLogin", "android");
                            mIntent.putExtra("username", user.getText().toString());
                            mIntent.putExtra("sexo", usuario.getSexo());
                            mIntent.putExtra("id", usuario.getId());
                            mIntent.putExtra("filtro", "todo");
                            mIntent.putExtra(USER,usuario);
                            startActivity(mIntent);
                            user.setText("");
                            pw.setText("");
                        } else {
                            Toast.makeText(Login.this, "Credenciales incorrectas",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "Credenciales incorrectas, no existe el nombre de usuario",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
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

    @Override
    protected void onStop() {
        super.onStop();
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }

    public void goSignUp(View view) {
        Intent mIntent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(mIntent);
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("org.duckdns.einyel.trabajo_grupal",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
