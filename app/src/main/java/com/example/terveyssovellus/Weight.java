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
        //** haetaaan tieto muistista ja tallennetaan muuttujaan
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        String jsonText = prefs.getString("paino", null);
        //haetaan elementit id:ellä koodista

        et = findViewById(R.id.editTxWeight);
        bt = findViewById(R.id.button);
        lv = findViewById(R.id.weightListview);

        //luodaan arraylist ja adabteri jolla saadaan arraylistin sisältö listanäkymään

        weight = new ArrayList<String>();
        arrayadapter = new ArrayAdapter<String>(
                Weight.this,
                android.R.layout.simple_list_item_1,
                weight);
        lv.setAdapter(arrayadapter);
        //ehtolause jolla tarkastetaan että muistissa on tallennettu tietoa
        if (prefs.getString("paino", null) != null) {
            //parsetaan stringistä pois kaikki erikoismerkit
            String yett = jsonText.replace("\\", "");
            jsonText = yett.replace("[", "");
            yett = jsonText.replace("\"", "");
            jsonText = yett.replace("]", "");
            //tallennetaan parsetut osat
            String[] parsed = jsonText.split(",");
            //** looppi jossa käydään läpi array johon tieto on tallennettu ja tallennetaan se arraylistin
            for (int i = 0; i < parsed.length; i++) {

                weight.add(parsed[i]);

            }
        }
        onClick();

    }
    //reakoidaan napin painallukseen kun käyttäjä haluaa tallentaa tietoa
    public void onClick() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //käännettään arraylist jotta uusin tieto on ensimmäisenä
                Collections.reverse(weight);
                //Haetaan päivä+aika
                Calendar calendar = Calendar.getInstance();
                //Asetetaan currentDateKey muotoon pp.kk.vvvv, käytetään Keynä sharedPreferensseissä
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                //luodaan stringi joka tallennetaan muistiin joka sisältää päivän ja painon arvon
                String s = currentDate + " Paino: " + et.getText().toString() + " kg";
                //tarkastetaan käyttäjän syöte
                if (s.contains(",")) {
                    //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
                    s = s.replace(",", ".");
                }
                //tallennetaan tieto arraylistiin
                weight.add(s);
                //käännettään arraylist jotta uusin tieto näkyy ensimmäisenä
                Collections.reverse(weight);
                //päivitetään lista näkymä uudella tiedolla
                arrayadapter.notifyDataSetChanged();
                //** valitaan paikka muistista johon tieto tallennetaan
                SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                //** käytetään gson kirjastoa jotta saadaan muutettua java objekti json muotoon
                Gson gson = new Gson();
                String json = gson.toJson(weight);
                //** tallennetaan tieto jsno muodossa muistiin
                editor.putString("paino", json);
                editor.apply();
                //** tyhjennettään syöttö kenttä
                et = findViewById(R.id.editTxWeight);
                et.getText().clear();

            }


        });

    }

}

