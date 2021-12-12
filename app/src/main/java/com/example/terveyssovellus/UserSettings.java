package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;

public class UserSettings extends AppCompatActivity {
    private double weight = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText editWeight = findViewById(R.id.et_weight);
        EditText editFood = findViewById(R.id.et_food_goal);
        SharedPreferences prefs3 = getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        String jsonText = prefs3.getString("e"+currentDate, null);

        if(jsonText!=null){

            String[] parsed = jsonText.split(",");
            String str;
            for (int i = 0;i> parsed.length;i++) {
                str=parsed[i];

                str = str.replaceAll("[^\\d-]", "");
                str.substring(10);
                Log.d("yeet", str);

            }
            //EditText editBurnedCalories = findViewById(R.id.et_exercise_goal);
          //  editBurnedCalories.setText(String.valueOf(cal));

        }

        if (prefs3.getString("paino", null) != null) {
            String paino = prefs3.getString("paino", "0");
            String s1 = paino.substring(paino.indexOf(":")+1); //thx to ItamarG3 from stackoverflow
            paino = s1.replace(" kg","");
            s1 = paino.replace("\"]","");
            paino = s1.split("\"")[0]; // thx to assylias from stackoverflow
            weight = Double.parseDouble(paino);
            editWeight.setText(String.valueOf(weight));
        }


        editFood.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            changeUserFood();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        EditText editHeight = findViewById(R.id.et_height);
        editHeight.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            changeUserHeight();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        editWeight.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            changeUserWeight();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        int calories_goal = prefs.getInt("user_food_goal",
                0);
        TextView textView = findViewById(R.id.et_food_goal);
        textView.setText(String.valueOf(calories_goal));

        SharedPreferences prefs2 = getDefaultSharedPreferences(getApplicationContext());
        int user_height = prefs2.getInt("user_height",
                0);
        TextView textView2 = findViewById(R.id.et_height);
        textView2.setText(String.valueOf(user_height));
    }

    public void changeUserFood(){
        Toast.makeText(getApplicationContext(),"Päivittäinen kaloritavoite asetettu.", Toast.LENGTH_SHORT).show();

        EditText editText = findViewById(R.id.et_food_goal);
        int n = Integer.parseInt(editText.getText().toString());

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_food_goal", n);
        editor.commit();
    }

    public void changeUserHeight(){
        Toast.makeText(getApplicationContext(),"Pituus asetettu.", Toast.LENGTH_SHORT).show();

        EditText editText = findViewById(R.id.et_height);
        int n = Integer.parseInt(editText.getText().toString());

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_height", n);
        editor.commit();
    }
    public void changeUserWeight(){
        ArrayList<String> weight;
        weight = new ArrayList<String>();
        EditText editWeight = findViewById(R.id.et_weight);
        Toast.makeText(getApplicationContext(),"Paino asetettu.", Toast.LENGTH_SHORT).show();
        Collections.reverse(weight);
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        String s = currentDate + " Paino: " + editWeight.getText().toString() + " kg";
        if (s.contains(",")) {
            s = s.replace(",", ".");
            Log.d("yeet2", s);
        }
        weight.add(s);
        Collections.reverse(weight);
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(weight);
        editor.putString("paino", json);
        editor.apply();


    }
}