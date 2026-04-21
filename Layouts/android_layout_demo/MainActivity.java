package com.example.android_layout_demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnLinear, btnRelative, btnAbsolute, btnGrid, btnConstraint, btnFrame, btnTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLinear = findViewById(R.id.btnLinear);
        btnRelative = findViewById(R.id.btnRelative);
        btnAbsolute = findViewById(R.id.btnAbsolute);
        btnGrid = findViewById(R.id.btnGrid);
        btnConstraint = findViewById(R.id.btnConstraint);
        btnFrame = findViewById(R.id.btnFrame);
        btnTable = findViewById(R.id.btnTable);

        btnLinear.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LinearActivity.class)));

        btnRelative.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RelativeActivity.class)));

        btnAbsolute.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AbsoluteActivity.class)));

        btnGrid.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, GridActivity.class)));

        btnConstraint.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ConstraintActivity.class)));

        btnFrame.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, FrameActivity.class)));

        btnTable.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, TableActivity.class)));
    }
}
