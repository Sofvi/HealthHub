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

/**
 * Luokka on painonlisäystä varten
 * @author Henri Vuento
 * @version 12.12.2021
 */


public class Weight extends AppCompatActivity {
   //ui elementtien määrittely
    Button bt;
    EditText et;
    ListView lv;
    ArrayList<String> weight;
    ArrayAdapter<String> arrayadapter;

    /**luodaan arraylist ja adapteri jolla saadaan arraylistin sisältö listanäkymään*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et = findViewById(R.id.editTxWeight);                                                           //haetaan elementit id:ellä koodista
        bt = findViewById(R.id.button);
        lv = findViewById(R.id.weightListview);
        Fetch fetch =new Fetch(getDefaultSharedPreferences(getApplicationContext()));
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());

        weight = new ArrayList<String>();
        arrayadapter = new ArrayAdapter<String>(
                Weight.this,
                android.R.layout.simple_list_item_1,
                weight);
        lv.setAdapter(arrayadapter);

        /**
         * Tallennetaan parsetut osat
         * Loop jossa käydään läpi array johon tieto on tallennettu ja tallennetaan se arraylistin
         * */
        if (prefs.getString("paino", null) != null) {                                       //ehtolause jolla tarkastetaan että muistissa on tallennettu tietoa
            String[] parsed = fetch.fetchWeightList().split(",");
            for (int i = 0; i < parsed.length; i++) {
                weight.add(parsed[i]);
            }
        }
        onClick();
    }

    public void onClick() {                                                                             //reagoidaan napin painallukseen kun käyttäjä haluaa tallentaa tietoa
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Memory save =new Memory(getDefaultSharedPreferences(getApplicationContext()));
                Collections.reverse(weight);                                                            //käännettään arraylist jotta uusin tieto on ensimmäisenä
                String s = save.date() + " Paino: " + et.getText().toString() + " kg";                  //luodaan stringi joka tallennetaan muistiin joka sisältää päivän ja painon arvon

                if (s.contains(",")) {                                                                  //tarkastetaan käyttäjän syöte
                    s = s.replace(",", ".");                                            //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
                }
                weight.add(s);                                                                          //tallennetaan tieto arraylistiin
                Collections.reverse(weight);                                                            //käännettään arraylist jotta uusin tieto näkyy ensimmäisenä
                arrayadapter.notifyDataSetChanged();                                                    //päivitetään lista näkymä uudella tiedolla
                save.saveWeight(weight);
                et = findViewById(R.id.editTxWeight);
                et.getText().clear();

            }

        });

    }

}

