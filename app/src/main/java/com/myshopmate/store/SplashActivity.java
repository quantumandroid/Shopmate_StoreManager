package com.myshopmate.store;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.myshopmate.store.Dashboard.LoginActivity;
import com.myshopmate.store.util.Session_management;


public class SplashActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;
    private AlertDialog dialog;

    private Session_management sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        sessionManagement = new Session_management(SplashActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(500);

                    // After 5 seconds redirect to another intent
                    checkAppPermissions();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // start thread
        background.start();
    }

    public void checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_NETWORK_STATE)) {
                go_next();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.ACCESS_NETWORK_STATE
                        },
                        MY_PERMISSIONS_REQUEST_WRITE_FIELS);
            }
        } else {
            go_next();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_FIELS) {
            if (grantResults == null) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                showDialog();
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                go_next();
            } else {
                showDialog();
            }
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setCancelable(false);
        builder.setMessage("App requires some permission, please enable it in settings.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openPermissionScreen();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                        finish();
                    }
                });
        dialog = builder.show();
    }

    public void go_next() {
        if (sessionManagement.isLoggedIn()) {
            Intent startmain = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(startmain);
        } else {
            Intent startmain = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(startmain);
        }
        finish();
    }

    public void openPermissionScreen() {
        // startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", SplashActivity.this.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        finish();
    }
}