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
 * @author Suvi Laitinen, Henri Vuento, Tuomo Muttonen, Eetu Haverinen
 * @version 12.12.2021
 */


public class Weight extends AppCompatActivity {
   //ui elementtien määrittely
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

        /** haetaan tieto muistista ja tallennetaan muuttujaan*/
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        String jsonText = prefs.getString("paino", null);

        et = findViewById(R.id.editTxWeight);                                                           //haetaan elementit id:ellä koodista
        bt = findViewById(R.id.button);
        lv = findViewById(R.id.weightListview);

        /**luodaan arraylist ja adapteri jolla saadaan arraylistin sisältö listanäkymään*/
        weight = new ArrayList<String>();
        arrayadapter = new ArrayAdapter<String>(
                Weight.this,
                android.R.layout.simple_list_item_1,
                weight);
        lv.setAdapter(arrayadapter);

        if (prefs.getString("paino", null) != null) {                                       //ehtolause jolla tarkastetaan että muistissa on tallennettu tietoa
            String yett = jsonText.replace("\\", "");                                   //parsetaan stringistä pois kaikki erikoismerkit
            jsonText = yett.replace("[", "");
            yett = jsonText.replace("\"", "");
            jsonText = yett.replace("]", "");

            String[] parsed = jsonText.split(",");                                                  //tallennetaan parsetut osat
            /** loop jossa käydään läpi array johon tieto on tallennettu ja tallennetaan se arraylistin*/
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
                Collections.reverse(weight);                                                            //käännettään arraylist jotta uusin tieto on ensimmäisenä
                Calendar calendar = Calendar.getInstance();                                             //Haetaan päivä+aika
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());           //Asetetaan currentDateKey muotoon pp.kk.vvvv, käytetään Keynä sharedPreferensseissä
                String s = currentDate + " Paino: " + et.getText().toString() + " kg";                  //luodaan stringi joka tallennetaan muistiin joka sisältää päivän ja painon arvon

                if (s.contains(",")) {                                                                  //tarkastetaan käyttäjän syöte
                    s = s.replace(",", ".");                                            //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
                }
                weight.add(s);                                                                          //tallennetaan tieto arraylistiin
                Collections.reverse(weight);                                                            //käännettään arraylist jotta uusin tieto näkyy ensimmäisenä
                arrayadapter.notifyDataSetChanged();                                                    //päivitetään lista näkymä uudella tiedolla
                /** valitaan paikka muistista johon tieto tallennetaan*/
                SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                /** käytetään gson kirjastoa jotta saadaan muutettua java objekti json muotoon*/
                Gson gson = new Gson();
                String json = gson.toJson(weight);
                /** tallennetaan tieto jsno muodossa muistiin*/
                editor.putString("paino", json);
                editor.apply();
                /** tyhjennettään syöttö kenttä*/
                et = findViewById(R.id.editTxWeight);
                et.getText().clear();

            }


        });

    }

}

