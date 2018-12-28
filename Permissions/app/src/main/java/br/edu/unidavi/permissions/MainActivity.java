package br.edu.unidavi.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.request_permission);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasCameraPermission()) {
                    return;
                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Por favor usuário, não nega dessa vez!")
                            .setPositiveButton("OK", null)
                            .show();
                }

                requestPermission();
            }
        });
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 && permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this,
                        "Permissão Negada",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Permissão Concedida!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = findViewById(R.id.status);
        textView.setText(String.valueOf(hasCameraPermission()));
    }

    boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }
}
