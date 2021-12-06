package com.example.terveyssovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Food extends AppCompatActivity {

    private int foodCalories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
    }

    public void updateFoodCalories(View view){
        EditText editText = findViewById(R.id.editTxFoodCalories);
        int n = Integer.parseInt(editText.getText().toString());
        foodCalories = foodCalories+n;
        TextView textView = findViewById(R.id.total_food_calories);
        textView.setText(String.valueOf(foodCalories));
    }
}