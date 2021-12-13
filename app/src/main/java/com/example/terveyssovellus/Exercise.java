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
 * @author Henri Vuento, Tuomo Muttonen Suvi Laitinen, Eetu Haverinen
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
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());

        Calendar calendar = Calendar.getInstance();                                                     //Haetaan päivä+aika
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());                   //Asetetaan currentDateKey muotoon pp.kk.vvvv
        String jsonText = prefs.getString("e"+currentDate, null);                           //asetetaan muistista haettu arvo muuttujaan

        if (prefs.getString("e"+currentDate, null)!=null) {                                 //tarkastetaan että muistiin on tallennettu tietoa
            String yett= jsonText.replace("\\", "");                                    //poistetaan stringistä erikoismerkkit ja parsetaan stringi
            jsonText= yett.replace("[","");
            yett = jsonText.replace("\"","");
            jsonText= yett.replace("]","");
            String[] parsed = jsonText.split(",");

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
                Calendar calendar = Calendar.getInstance();                                             //Haetaan päivä+aika
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());           //Asetetaan currentDateKey muotoon pp.kk.vvvv
                String s;

                if(sel.equals("Muu")){                                                                  // muotoillaan tallennettava stringi sen mukaan että mitä käyttäjä on valinnut
                     s = currentDate + " Muu jossa poltetut kalorit " + et.getText().toString();        //jos valinta "Muu" niin tallennetaan käyttäjän antamat kalorit
                    et.getText().clear();
                }
                else{                                                                                   //muutoin tallennetaan listasta valittu kohta ja kalorit sen mukaan
                 s = currentDate + " " + sel;
                }

                if (s.contains(",")) {                                                                  //tarkistetaan käyttäjän syöte siltä varalta että siinä olisi pilkkuja ja korvataan ne pisteellä
                    s = s.replace(",", ".");                                            //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
                }

                exer.add(s);                                                                            //tallennetaan stringi arraylistiin
                arrayadapter2.notifyDataSetChanged();                                                   //päivitetään lista näkymä uudella tiedolla

                SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());         //valitaan paikka muistista johon tallennetaan
                SharedPreferences.Editor editor = prefs.edit();

                Gson gson = new Gson();                                                                 //** käytetään gson kirjastoa jotta saadaan muutettua java objekti json muotoon
                String json = gson.toJson(exer);

                editor.putString("e"+currentDate, json);                                                //** tallennetaan tieto jsno muodossa muistiin
                editor.apply();

                Toast.makeText(getApplicationContext(),sel + " lisätty.", Toast.LENGTH_SHORT).show();   //ilmoitetaan käyttäjälle että suoritus on lisätty
            }


        });
    }

    
}