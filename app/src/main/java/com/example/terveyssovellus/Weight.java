package com.example.terveyssovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Weight extends AppCompatActivity {

    private List<Integer> weight = new ArrayList<Integer>();
    private ArrayAdapter<Weight> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
    }

    public void updateWeight() {
        EditText editText = findViewById(R.id.editTxWeight);
        int n = Integer.parseInt(editText.getText().toString());
        weight.add(n);
        updateListview();

    }

    private void updateListview() {
    }



}