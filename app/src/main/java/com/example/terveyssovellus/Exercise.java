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

public class Exercise extends AppCompatActivity {
    Button nappi;
    String sel;
    EditText et;
    ListView elv;
    ArrayList<String> exer;
    ArrayAdapter<String> arrayadapter2;

    /**
     * asetetaan näkyviin activity ja haetaan ohjelman kannalta olennaiset osat muistista, sekä asetetaan näkyviin ui:hin, käsitellään dropdown menun valintoja
     * @author Henri Vuento, Tuomo Muttonen Suvi Laitinen, Eetu Haverinen
     * @version 12.12.2021
     * @param savedInstanceState
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nappi = findViewById(R.id.esub);
        elv = findViewById(R.id.exerciseList);
        //** luodaan arraylist sekä adapter jolla saadaan arraylist sisältö lista näkymään
        exer = new ArrayList<String>();
        et = findViewById(R.id.editTxexercise);
        TextView tv_kalorit = findViewById(R.id.textView13);
        arrayadapter2 = new ArrayAdapter<String>(
                Exercise.this,
                android.R.layout.simple_list_item_1,
                exer);
        elv.setAdapter(arrayadapter2);
        // määritellään dropdown menu
        Spinner liikunnatDropdown = (Spinner) findViewById(R.id.spinnerExercises);
        //** tehdään adapteri dropdown menulle ja haetaan siihen strings.xml tiedostosta sisältö
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                Exercise.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.liikuntamuodot));
        /**
         * haetaan muistista sinne tallennetut liikunta suoritukset ja asetetaan näkyviin listanäkymään
         */
        //määritetään paikka muistista josta haetaan tieto
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());

        //Haetaan päivä+aika
        Calendar calendar = Calendar.getInstance();
        //Asetetaan currentDateKey muotoon pp.kk.vvvv
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        //asetetaan muistista haettu arvo muuttujaan
        String jsonText = prefs.getString("e"+currentDate, null);
        //tarkastetaan että muistiin on tallennettu tietoa
        if (prefs.getString("e"+currentDate, null)!=null) {
            //poistetaan stringistä erikoismerkkit ja parsetaan stringi
            String yett= jsonText.replace("\\", "");
            jsonText= yett.replace("[","");
            yett = jsonText.replace("\"","");
            jsonText= yett.replace("]","");
            String[] parsed = jsonText.split(",");
            //lisätään parsetut stringit arraylistin
            for (int i=0;i<parsed.length;i++) {

                exer.add(parsed[i]);

            }
        }
        /**
         * tarkkaillaan käyttäjän valintaa dropdown menussa ja näytetään tai piilotetaan elementtejä sen mukaan, lisäksi tallennetaan valinta muuttujan
         */
        //
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        liikunnatDropdown.setAdapter(arrayAdapter);
        liikunnatDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //kun listasta valitaan kohta "Muu" niin näytetään syöttö elementti jotta käyttäjä saa lisättyä kalori määrän
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 sel = parent.getSelectedItem().toString();
                 if(sel.equals("Muu")){
                    et.setVisibility(View.VISIBLE);
                    tv_kalorit.setVisibility(View.VISIBLE);
                 }
                 //kaikilla muilla valinnoilla piilotetaan syöttö elementti
                 else{
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
     * Reakoidaan napin painalluukseen ja tallennetaan tietoa puhelimen muistiin
     */

    public void onClick() {
        nappi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Haetaan päivä+aika
                Calendar calendar = Calendar.getInstance();
                //Asetetaan currentDateKey muotoon pp.kk.vvvv
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                String s;
                //** muotoillaan tallennettava stringi sen mukaan että mitä käyttäjä on valinnut
                if(sel.equals("Muu")){
                    //jos valinta "Muu" niin tallennetaan käyttäjän antamat kalorit
                     s = currentDate + " Muu jossa poltetut kalorit " + et.getText().toString();
                    et.getText().clear();
                }
                //muutoin tallennetaan listasta valittu kohta ja kalorit sen mukaan
                else{
                 s = currentDate + " " + sel;}
                //** tarkistetaan käyttäjän syöte siltä varalta että siinä olisi pilkkuja ja korvataan ne pisteellä
                if (s.contains(",")) {
                    //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
                    s = s.replace(",", ".");
                }
                //tallennetaan stringi arraylistiin
                exer.add(s);
                //päivitetään lista näkymä uudella tiedolla
                arrayadapter2.notifyDataSetChanged();
                //valitaan paikka muistista johon tallennetaan
                SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                //** käytetään gson kirjastoa jotta saadaan muutettua java objekti json muotoon
                Gson gson = new Gson();
                String json = gson.toJson(exer);
                //** tallennetaan tieto jsno muodossa muistiin
                editor.putString("e"+currentDate, json);
                editor.apply();
                //ilmoitetaan käyttäjälle että suoritus on lisätty
                Toast.makeText(getApplicationContext(),sel + " lisätty.", Toast.LENGTH_SHORT).show();
            }


        });
    }

    
}