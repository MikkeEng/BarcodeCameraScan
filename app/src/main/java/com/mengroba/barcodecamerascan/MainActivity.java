package com.mengroba.barcodecamerascan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

import me.sudar.zxingorient.ZxingOrient;
import me.sudar.zxingorient.ZxingOrientResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 0x0000c0de; // Only use bottom 16 bits
    private static final int REQUEST_CAMERA = 0x00000011;
    private static final String BS_PACKAGE = "me.sudar.zxing";
    //Instanciamos los elementos graficos
    private Button scan_btn;
    private TextView format_txv, content_txv;
    private final Map<String,Object> moreExtras = new HashMap<String,Object>(3);


    private String scanContent, scanFormat;
    private boolean vibrate;
    private boolean playBeep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Enlazamos los elementos graficos
        scan_btn = (Button) findViewById(R.id.btn_scan);
        format_txv = (TextView) findViewById(R.id.txv_format);
        content_txv = (TextView) findViewById(R.id.txv_content);

        //listener para el boton scan
        scan_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_scan) {
            new ZxingOrient(MainActivity.this).initiateScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ZxingOrientResult scanResult = ZxingOrient.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            content_txv.setText(scanResult.getContents());
            format_txv.setText(scanResult.getFormatName());
        }
    }

    private void requestCameraPermission() {
        Log.i(TAG, "El perimso de la camara no esta garantizado. Enviado permiso.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            Log.i(TAG,
                    "Mostrando el mensaje para permitir la camara.");
            Snackbar.make(findViewById(R.id.activity_main), R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // Si la solicitud se cancela, el array se vacia
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new ZxingOrient(MainActivity.this).initiateScan();
                } else {
                    finish();
                }
            }
            break;
        }
    }

}
