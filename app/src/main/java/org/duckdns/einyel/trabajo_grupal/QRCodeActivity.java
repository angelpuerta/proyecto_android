package org.duckdns.einyel.trabajo_grupal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.duckdns.einyel.trabajo_grupal.fragments.CodeNoOkFragment;
import org.duckdns.einyel.trabajo_grupal.fragments.CodeOkFragment;
import org.duckdns.einyel.trabajo_grupal.model.Event;
import org.duckdns.einyel.trabajo_grupal.service.App;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRCodeActivity extends AppCompatActivity {

    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private String token = "";
    private String tokenanterior = "";
    private Event evento;
    private ProgressBar progressBar;
    public static final String CODE = "CODE";

    private String username;
    private QRCodeActivity thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Bundle b = getIntent().getExtras();
        evento = b.getParcelable(DescripcionActivity.EVENTO);
        username = b.getString("username");

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.GONE);
        initQR();
    }

    public void initQR() {

        // creo el detector qr
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

        // creo la camara
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        // listener de ciclo de vida de la camara
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                // verifico si el usuario dio los permisos para la camara
                if (ActivityCompat.checkSelfPermission(QRCodeActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // verificamos la version de ANdroid que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                        if (shouldShowRequestPermissionRationale(
                                Manifest.permission.CAMERA)) ;
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                    return;
                } else {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException ie) {
                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        // preparo el detector de QR
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }


            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();


                if (barcodes.size() > 0) {

                    // obtenemos el token
                    token = barcodes.valueAt(0).displayValue.toString();


                    // verificamos que el token anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo token
                    if (!token.equals(tokenanterior)) {


                        // guardamos el ultimo token proceado
                        tokenanterior = token;
                        Log.i("token", token);

                        App.get().getFirebaseApi().checkCode(evento.getId(), token, username).enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()) {
                                    evento.setCode(token);
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    DialogFragment dialogo = new CodeOkFragment();
                                    dialogo.show(fragmentManager, "fragmentoOk");
                                    addToSharedPrefference(token);
                                    finish();
                                    Toast.makeText(thisActivity, getString(R.string.EventCheckIn),Toast.LENGTH_LONG).show();
                                } else {
                                    failureWindow();
                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                failureWindow();
                            }
                        });





                        /*if (URLUtil.isValidUrl(token)) {
                            // si es una URL valida abre el navegador
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(token));
                            startActivity(browserIntent);
                        } else {
                            // comparte en otras apps
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, token);
                            shareIntent.setType("text/plain");
                            startActivity(shareIntent);
                        }*/

                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (this) {
                                        wait(5000);
                                        // limpiamos el token
                                        tokenanterior = "";
                                    }
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!");
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                }
            }
        });

    }

    public void failureWindow() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment dialogo = new CodeNoOkFragment();
        dialogo.show(fragmentManager, "fragmentoNoOk");
    }


    public void addToSharedPrefference(String code) {
        String search = "QR/" + username + "/" + evento.getId();
        this.getSharedPreferences("QRs", Context.MODE_PRIVATE).edit()
                .putString(search, code)
                .commit();
        String code22 = this.getSharedPreferences("QRs", Context.MODE_PRIVATE).getString(search, "");

    }
}
