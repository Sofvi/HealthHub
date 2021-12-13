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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Luokka on liikunnan lisäyksen näyttö
 * @author Henri Vuento
 * @version 12.12.2021
 */
public class Exercise extends AppCompatActivity {
    Button nappi;
    String sel;
    EditText et;
    ListView elv;
    ArrayList<String> exer;
    ArrayAdapter<String> arrayadapter2;

    /**
     * asetetaan näkyviin activity
     * haetaan ohjelman kannalta olennaiset osat muistista
     * asetetaan näkyviin ui:hin
     * käsitellään dropdown menun valintoja
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nappi = findViewById(R.id.esub);
        elv = findViewById(R.id.exerciseList);
        Fetch fetch =new Fetch(getDefaultSharedPreferences(getApplicationContext()));
        exer = new ArrayList<String>();                                                                 //** luodaan arraylist sekä adapter jolla saadaan arraylist sisältö lista näkymään
        et = findViewById(R.id.editTxexercise);
        TextView tv_kalorit = findViewById(R.id.textView13);
        arrayadapter2 = new ArrayAdapter<String>(
                Exercise.this,
                android.R.layout.simple_list_item_1,
                exer);
        elv.setAdapter(arrayadapter2);

        Spinner liikunnatDropdown = (Spinner) findViewById(R.id.spinnerExercises);                      // määritellään dropdown menu

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(                                   //** tehdään adapteri dropdown menulle ja haetaan siihen strings.xml tiedostosta sisältö
                Exercise.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.liikuntamuodot));
        /**
         * haetaan muistista sinne tallennetut liikunta suoritukset
         * asetetaan näkyviin listanäkymään
         */

        if (fetch.fetchExercise()!=null) {                                 //tarkastetaan että muistiin on tallennettu tietoa
            String[] parsed = fetch.fetchExercise().split(",");

            for (int i=0;i<parsed.length;i++) {                                                         //lisätään parsetut stringit arraylistin

                exer.add(parsed[i]);

            }
        }
        /**
         * tarkkaillaan käyttäjän valintaa dropdown menussa
         * näytetään tai piilotetaan elementtejä
         * tallennetaan valinta muuttujana
         */
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        liikunnatDropdown.setAdapter(arrayAdapter);
        liikunnatDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {       //kun listasta valitaan kohta "Muu" niin näytetään syöttö elementti jotta käyttäjä saa lisättyä kalori määrän
                 sel = parent.getSelectedItem().toString();
                 if(sel.equals("Muu")){
                    et.setVisibility(View.VISIBLE);
                    tv_kalorit.setVisibility(View.VISIBLE);
                 }
                 else{                                                                                  //kaikilla muilla valinnoilla piilotetaan syöttö elementti
                     et.setVisibility(View.INVISIBLE);
                     tv_kalorit.setVisibility(View.INVISIBLE);
                 }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        onClick();
    }

    /**
     * Reagoidaan napin painallukseen ja tallennetaan tietoa puhelimen muistiin
     */

    public void onClick() {
        nappi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Memory save =new Memory(getDefaultSharedPreferences(getApplicationContext()));
                String s;

                if(sel.equals("Muu")){                                                                  // muotoillaan tallennettava stringi sen mukaan että mitä käyttäjä on valinnut
                     s = save.date() + " Muu jossa poltetut kalorit " + et.getText().toString();        //jos valinta "Muu" niin tallennetaan käyttäjän antamat kalorit
                    et.getText().clear();
                }
                else{                                                                                   //muutoin tallennetaan listasta valittu kohta ja kalorit sen mukaan
                 s = save.date() + " " + sel;
                }

                exer.add(s);                                                                            //tallennetaan stringi arraylistiin
                arrayadapter2.notifyDataSetChanged();                                                   //päivitetään lista näkymä uudella tiedolla
                save.saveExercise(exer);

                Toast.makeText(getApplicationContext(),sel + " lisätty.", Toast.LENGTH_SHORT).show();   //ilmoitetaan käyttäjälle että suoritus on lisätty
            }


        });
    }

    
}