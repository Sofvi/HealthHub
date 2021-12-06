package com.example.terveyssovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;
import java.util.ArrayList;
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

        et = findViewById(R.id.editTxWeight);
        bt = findViewById(R.id.button);
        lv = findViewById(R.id.weightListview);

        weight = new ArrayList<String>();
        arrayadapter = new ArrayAdapter<String>(
                Weight.this,
                android.R.layout.simple_list_item_1,
                weight);
        lv.setAdapter(arrayadapter);

        onClick();

    }



    public void onClick() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                String s = currentDate + ", Paino: " + et.getText().toString() + " kg";
                weight.add(s);
                Collections.reverse(weight);
                arrayadapter.notifyDataSetChanged();
            }
        });
    }


}

