package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class Food extends AppCompatActivity {

    private int foodCalories = 0;
    Calendar calendar = Calendar.getInstance();
    String currentDateKey = DateFormat.getDateInstance().format(calendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        foodCalories = prefs.getInt(currentDateKey,
                0);
        TextView textView = findViewById(R.id.total_food_calories);
        textView.setText(String.valueOf(foodCalories));

        SharedPreferences prefs2 = getDefaultSharedPreferences(getApplicationContext());
        int goal_calories = prefs2.getInt("user_food_goal",
                0);
        TextView textView2 = findViewById(R.id.food_calories_goal);
        textView2.setText(String.valueOf(goal_calories));

        ProgressBar progressBar = findViewById(R.id.food_progressBar);
        progressBar.setMax(goal_calories);
        progressBar.setProgress(foodCalories);
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
        editor.putInt(currentDateKey, foodCalories);
        editor.commit();

        ProgressBar progressBar = findViewById(R.id.food_progressBar);
        progressBar.setProgress(foodCalories);
    }


}