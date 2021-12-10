package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Exercise extends AppCompatActivity {
    Button nappi;
    String sel;
    EditText et;
    ListView elv;
    ArrayList<String> exer;
    ArrayAdapter<String> arrayadapter;
    ArrayAdapter<String> arrayadapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nappi = findViewById(R.id.esub);
        elv = findViewById(R.id.exerciseList);
        exer = new ArrayList<String>();
        et = findViewById(R.id.editTxexercise);
        arrayadapter2 = new ArrayAdapter<String>(
                Exercise.this,
                android.R.layout.simple_list_item_1,
                exer);
        elv.setAdapter(arrayadapter2);

        Spinner liikunnatDropdown = (Spinner) findViewById(R.id.spinnerExercises);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                Exercise.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.liikuntamuodot));
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        String jsonText = prefs.getString("e"+currentDate, null);

        if (prefs.getString("e"+currentDate, null)!=null) {
            Log.d("yeet2", jsonText);
            String yett= jsonText.replace("\\", "");
            jsonText= yett.replace("[","");
            yett = jsonText.replace("\"","");
            jsonText= yett.replace("]","");

            String[] parsed = jsonText.split(",");

            for (int i=0;i<parsed.length;i++) {

                exer.add(parsed[i]);

            }
            Log.d("yeet2", jsonText);
        }

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        liikunnatDropdown.setAdapter(arrayAdapter);
        liikunnatDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 sel = parent.getSelectedItem().toString();
                Log.d("yeet", sel);
                 if(sel.equals("Muu")){
                    et.setVisibility(View.VISIBLE);
                 }
                 else{
                     et.setVisibility(View.INVISIBLE);
                 }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        onClick();
    }
    public void onClick() {
        nappi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

                String s;
                if(sel.equals("Muu")){
                     s = currentDate + " liikuntamuoto Muu jossa poltetut kalorit " + et.getText().toString();
                }
                else{
                 s = currentDate + " liikuntamuoto " + sel;}

                Log.d("yeet", s);
                exer.add(s);
                String debug = exer.toString();
                arrayadapter2.notifyDataSetChanged();
                Log.d("yeet", debug );
                SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(exer);
                editor.putString("e"+currentDate, json);
                editor.apply();

                Toast.makeText(getApplicationContext(),"Nappulaa(eiselapsi) painettu.", Toast.LENGTH_SHORT).show();
            }


        });
    }

    
}