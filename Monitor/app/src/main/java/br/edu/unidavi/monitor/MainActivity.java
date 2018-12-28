package br.edu.unidavi.monitor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static MainActivity EU_NAO_DEVERIA_FAZER_ISSO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EU_NAO_DEVERIA_FAZER_ISSO = this;

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "teste";
                String content = "Conteúdo do arquivo";
                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(content.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    Log.e("MainActivity", "Falha ao salvar arquivo", e);
                }
            }
        });

        Button navigate = findViewById(R.id.button_navigate);
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        MainActivity.this,
                        SecondActivity.class));
                finish();
            }
        });
    }
}
