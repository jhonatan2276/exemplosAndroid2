package br.edu.unidavi.firebase;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
    private Button buttonLogin;
    private Button buttonAdd;
    private EditText inputName;
    private TextView labelApproved;

    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Crashlytics.getInstance().crash();

        FirebaseRemoteConfigSettings settings =
                new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();
        config.setConfigSettings(settings);
        config.setDefaults(R.xml.defaults);

        welcome = findViewById(R.id.welcome);
        inputName = findViewById(R.id.input_name);
        labelApproved = findViewById(R.id.label_approved);

        buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        LoginActivity.class));
            }
        });

        applyButtonLabel();

        buttonAdd = findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = db.getReference("data/"
                        + auth.getCurrentUser().getUid()
                        + "/nome");
                reference.setValue(inputName.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Não foi possível inserir o valor",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void applyButtonLabel() {
        buttonLogin.setText(config.getString("button_label"));

        long cacheExpiration = 3600; // 1 hora
        if (config.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        config.fetch(cacheExpiration).addOnCompleteListener(
                new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    config.activateFetched();
                }
                buttonLogin.setText(config.getString("button_label"));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            welcome.setText("Olá " + user.getEmail());
            buttonLogin.setVisibility(View.GONE);
            buttonAdd.setVisibility(View.VISIBLE);
            inputName.setVisibility(View.VISIBLE);
            labelApproved.setVisibility(View.VISIBLE);
            setupValueListener();
        }
    }

    private void setupValueListener() {
        DatabaseReference reference = db.getReference("data/"
                + auth.getCurrentUser().getUid()
                + "/aprovado");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.getValue(Boolean.class);
                if (Boolean.TRUE.equals(value)) {
                    labelApproved.setText("Aprovado!");
                    labelApproved.setTextColor(Color.GREEN);
                } else {
                    labelApproved.setText("Reprovado...");
                    labelApproved.setTextColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),
                        "Não foi possível obter o valor de 'aprovação'",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
