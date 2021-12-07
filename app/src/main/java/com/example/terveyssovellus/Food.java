package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Food extends AppCompatActivity {

    private int foodCalories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        foodCalories = prefs.getInt("ruokakalorit",
                0);
        TextView textView = findViewById(R.id.total_food_calories);
        textView.setText(String.valueOf(foodCalories));
    }

    public void updateFoodCalories(View view){
        EditText editText = findViewById(R.id.editTxFoodCalories);
        int n = Integer.parseInt(editText.getText().toString());
        foodCalories = foodCalories+n;
        TextView textView = findViewById(R.id.total_food_calories);
        textView.setText(String.valueOf(foodCalories));
        Toast.makeText(getApplicationContext(),n + " Kaloria lisätty.", Toast.LENGTH_SHORT).show();
        editText.getText().clear();

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("ruokakalorit", foodCalories);
        editor.commit();
    }


}