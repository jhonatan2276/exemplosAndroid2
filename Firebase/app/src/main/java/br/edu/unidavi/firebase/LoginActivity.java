package br.edu.unidavi.firebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText inputEmail = findViewById(R.id.input_email);
        final EditText inputPassword = findViewById(R.id.input_password);

        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputEmail.getText().length() > 0
                        && inputPassword.getText().length() > 0) {
                    auth.signInWithEmailAndPassword(inputEmail.getText().toString(),
                            inputPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Falha na autenticação",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    FirebaseAnalytics analytics = FirebaseAnalytics
                            .getInstance(getApplicationContext());

                    Bundle bundle = new Bundle();
                    bundle.putBoolean("errou", true);
                    analytics.logEvent("empty_form", bundle);
                }
            }
        });
    }
}
