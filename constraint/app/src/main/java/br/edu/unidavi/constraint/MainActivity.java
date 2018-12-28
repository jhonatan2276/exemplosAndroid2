package br.edu.unidavi.constraint;

import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    @LayoutRes
    private int currentLayout = R.layout.layout_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_1);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(this);
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (currentLayout == R.layout.layout_1) {
            currentLayout = R.layout.layout_2;
        } else {
            currentLayout = R.layout.layout_1;
        }

        ConstraintLayout constraintLayout = findViewById(R.id.constraint);
        TransitionManager.beginDelayedTransition(constraintLayout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.load(this, currentLayout);
        constraintSet.applyTo(constraintLayout);
    }
}
