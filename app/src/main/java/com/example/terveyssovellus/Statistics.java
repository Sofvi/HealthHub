package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Statistics extends AppCompatActivity {

    private double weight = 0;
    private int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        String paino = prefs.getString("paino",
                "0");
        String s1 = paino.substring(paino.indexOf(":")+1); //thx to ItamarG3 from stackoverflow
        paino = s1.replace(" kg","");
        s1 = paino.replace("\"]","");
        paino = s1.split("\"")[0]; // thx to assylias from stackoverflow

        weight = Double.parseDouble(paino);

        SharedPreferences prefs2 = getDefaultSharedPreferences(getApplicationContext());
        height = prefs2.getInt("user_height",
                0);

        double bmi = weight / ((double)height/100) / ((double) height/100);
        String bmi2 = String.format("%.2f", bmi);

        TextView textView = findViewById(R.id.tv_bmi);
        textView.setText("Painoindeksi: " + bmi2);







    }


}