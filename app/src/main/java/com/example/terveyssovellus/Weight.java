package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Weight extends AppCompatActivity {

    Button bt;
    EditText et;
    ListView lv;
    ArrayList<String> weight;
    ArrayAdapter<String> arrayadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();



        String jsonText = prefs.getString("paino", null);


        et = findViewById(R.id.editTxWeight);
        bt = findViewById(R.id.button);
        lv = findViewById(R.id.weightListview);

        weight = new ArrayList<String>();

        arrayadapter = new ArrayAdapter<String>(
                Weight.this,
                android.R.layout.simple_list_item_1,
                weight);
        lv.setAdapter(arrayadapter);

        if (prefs.getString("paino", null)!=null) {
            //    String[] text = gson.fromJson(jsonText, String[].class);
            //  String str = text.toString();
            String yett= jsonText.replace("\\", "");
            jsonText= yett.replace("[","");
            yett = jsonText.replace("\"","");
            jsonText= yett.replace("]","");

            String[] parsed = jsonText.split(",");

            for (int i=0;i<parsed.length;i++) {

                weight.add(parsed[i]);

            }
            Log.d("yeet", jsonText);
        }

        onClick();

    }



    public void onClick() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                String s = currentDate + " Paino: " + et.getText().toString() + " kg";
                if(s.contains(",")){
                   s = s.replace(",",".");
                    Log.d("yeet2", s);
                }
                weight.add(s);
                Collections.reverse(weight);
                arrayadapter.notifyDataSetChanged();
                SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(weight);
                editor.putString("paino", json);
                editor.apply();
            }


        });
    }


}

