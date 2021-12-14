package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;

/**
 * Luokka on käyttäjän asetuksien näyttö
 *
 * @author Henri Vuento, Tuomo Muttonen
 * @version 12.12.2021
 */
public class UserSettings extends AppCompatActivity {

    private double weight = 0;                                                                          //muuttuja painolle

    /**
     * Hakee kaloritavoitteen muistista ja asettaa sen textViewiin
     * Hakee painon muistista ja asettaa sen textViewiin
     * Hakee pituuden muistista ja asettaa sen textViewiin
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText editWeight = findViewById(R.id.et_weight);                                             //haetaan ui elementit myöhempää käyttöä varten
        EditText editFood = findViewById(R.id.et_food_goal);

        SharedPreferences prefs3 = getDefaultSharedPreferences(getApplicationContext());                //määritetään paikka muistissa jossa haetaan tiedot
        Fetch fetch =new Fetch(getDefaultSharedPreferences(getApplicationContext()));

        if (prefs3.getString("paino", null) != null) {                                      //ehto jolla tarkastetaan että kyseinen data on tallennettu muistiin

            editWeight.setText(String.valueOf(fetch.fetchWeight()));                                     //asetetaan paino näkyville ui:hin
        }

        /**
         * tarkkailee millon käyttäjä on syöttänyt kenttään päivittäisen kaloritavoitteen
         * reagoi entterin painallukseen suorittamalla changeUserFood funktion
         */
        editFood.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            changeUserFood();                                                           //funktio kutsu jossa tallennetaan datan ja asetetaan se näkyville
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        /**
         * tarkkailee millon käyttäjä on syöttänyt kenttään pituuden
         * reagoi entterin painallukseen suorittamalla changeUserHeight funktion
         */
        EditText editHeight = findViewById(R.id.et_height);                                             //määritetään ui elementti muuttujaan
        editHeight.setOnKeyListener(new View.OnKeyListener()                                            //tarkkaillaan millon käyttäjä on syöttänyt uuden pituuden ja reakoidaan entterin painallukseen
        {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            changeUserHeight();                                                         //funktio kutsu jossa tallennetaan datan ja asetetaan se näkyville
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        /**
         * tarkkailee millon käyttäjä on syöttänyt kenttään painon ja reagoi entterin painallukseen suorittamalla changeUserWeight funktion
         */
        editWeight.setOnKeyListener(new View.OnKeyListener()                                            //tarkkaillaan millon käyttäjä on syöttänyt uuden painolukeman ja reagoidaan entterin painallukseen
        {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            changeUserWeight();                                                         //tallennetaan paino muistiin ja asetetaan näkyville
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        TextView textView = findViewById(R.id.et_food_goal);                                            //haetaan elemennti ui:sta id:llä ja lisätään muuttujaan
        textView.setText(String.valueOf(fetch.fetchCalories_goal()));                                   //asetetaan elemmenttiin uusi arvo

        TextView textView2 = findViewById(R.id.et_height);                                              //haetaan elemennti ui:sta id:llä ja lisätään muuttujaan
        textView2.setText(String.valueOf(fetch.fetchHeight()));                                         //asetetaan pituus näkyville ui:hin
    }

    /**
     * Tallennetaan käyttäjän syöttämä päivittäinen kaloritavoite muistiin
     */
    public void changeUserFood() {                                                                       //funktio jolla tallennetaan muistiin kaloritavoite
        Memory save =new Memory(getDefaultSharedPreferences(getApplicationContext()));
        EditText editText = findViewById(R.id.et_food_goal);                                            //haetaan ui elementti id:llä ja asetetaan muuttujaan
        String temp = editText.getText().toString();                                                    //haetaan ui elementti idellä ja tallennetaan muuttujaan
        if (temp.contains(",") || (temp.contains("."))) {                                               //tarkastetaan käyttäjän syöte
            editText.getText().clear();
            Toast.makeText(getApplicationContext(), "Syötä kokonaisluku", Toast.LENGTH_SHORT).show();
        } else {
            save.saveFood(temp);
            Toast.makeText(getApplicationContext(), "Päivittäinen kaloritavoite asetettu.", Toast.LENGTH_SHORT).show();  //Ilmoitetaan käyttäjälle onnistunut tapahtuma
        }
    }

    /**
     * Tallennetaan käyttäjän syöttämä pituus muistiin
     */
    public void changeUserHeight() {                                                                      //funktio pituuden tallentamista muistiin varten
        Memory save =new Memory(getDefaultSharedPreferences(getApplicationContext()));
        Toast.makeText(getApplicationContext(), "Pituus asetettu.", Toast.LENGTH_SHORT).show();      //ilmoitetaan käyttäjälle että pituus on asetettu
        EditText editText = findViewById(R.id.et_height);
        String temp = editText.getText().toString();                                                                                           //haetaan ui elementti idellä ja tallennetaan muuttujaan
        save.saveHeight(temp);                                                                          //tallennetaan käyttäjän syöte muistiin

    }

    /**
     * Tallennetaan käyttäjän syöttämä paino muistiin
     */
    public void changeUserWeight() {                                                                     //funktio jolla tallennetaan käyttäjän paino muistiin
        Memory save =new Memory(getDefaultSharedPreferences(getApplicationContext()));
        ArrayList<String> weight;                                                                       //luodaan arraylist weight johon tallennetaan paino
        weight = new ArrayList<String>();
        EditText editWeight = findViewById(R.id.et_weight);                                             //haetaan ui elementti id:llä

        Toast.makeText(getApplicationContext(), "Paino asetettu.", Toast.LENGTH_SHORT).show();      //ilmoitetaan käyttäjälle että paino on tallenettu
        Collections.reverse(weight);                                                                    //käännetään arraylist jotta uusin syöte olisi ensimmäisenä
        String s = save.date() + " Paino: " + editWeight.getText().toString() + " kg";                  //luodaan stringi joka tallennetaan muistiin joka sisältää päivän ja painon arvon

        if (s.contains(",")) {                                                                          //tarkastetaan käyttäjän syöte
            s = s.replace(",", ".");                                                    //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
        }

        weight.add(s);                                                                                  //tallennetaan String arraylistiin
        Collections.reverse(weight);                                                                    //käännetään arraylist jotta uusin tieto tulisi ensimmäiseksi
        save.saveWeight(weight);


    }
}