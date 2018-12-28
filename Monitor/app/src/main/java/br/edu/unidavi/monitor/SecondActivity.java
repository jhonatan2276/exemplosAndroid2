package br.edu.unidavi.monitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button danger = findViewById(R.id.button_danger);
        danger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 1_000_000; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5_000);
                            } catch (InterruptedException e) {
                                Log.e("SecondActivity", "Ops");
                            }
                        }
                    }).start();
                }
            }
        });
    }
}
