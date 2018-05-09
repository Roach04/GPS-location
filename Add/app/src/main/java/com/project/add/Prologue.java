package com.project.add;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.project.add.R;

public class Prologue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prologue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void address(View view) {

        startActivity(new Intent(this, Splash.class));
    }

    public void map(View view) {

        startActivity(new Intent(this, MapsActivity.class));
    }

    public void history(View view) {

        Toast.makeText(Prologue.this, " This is History Activity.", Toast.LENGTH_SHORT).show();
    }

    public void near(View view) {

        Toast.makeText(Prologue.this, " This is Near Me Activity.", Toast.LENGTH_SHORT).show();
    }
}
