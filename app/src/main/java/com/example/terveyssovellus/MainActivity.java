package com.example.terveyssovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickFood(View view) {
        Intent intent = new Intent(this, Food.class);
        startActivity(intent);
    }

    public void onClickWeight(View view) {
        Intent intent = new Intent(this, Weight.class);
        startActivity(intent);
    }

    public void onClickExercise(View view) {
        Intent intent = new Intent(this, Exercise.class);
        startActivity(intent);
    }

    public void onClickSettings(View view) {
        Intent intent = new Intent(this, UserSettings.class);
        startActivity(intent);
    }

    public void onClickStatistics(View view) {
        Intent intent = new Intent(this, Statistics.class);
        startActivity(intent);
    }
}