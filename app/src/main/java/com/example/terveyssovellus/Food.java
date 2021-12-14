package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Luokka on Kalorilaskurin näyttö
 *
 * @author Tuomo Muttonen
 * @version 12.12.2021
 */

public class Food extends AppCompatActivity {

    private int foodCalories = 0;                                                                       //muuttuja syödyille kaloreille

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * Hakee kalorimäärän muistista ja asettaa sen textViewiin
         * Hakee kaloritavoitteen muistista ja asettaa sen textViewiin
         * Asettaa päivittäisen kaloritavoitteen edistymispalkin maksimiarvoksi
         * Asettaa edistymispalkin arvon nykyiseen kalorimäärään
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fetch fetch =new Fetch(getDefaultSharedPreferences(getApplicationContext()));
        foodCalories=fetch.fetchDailyCalories();
        TextView textView = findViewById(R.id.total_food_calories);
        textView.setText(String.valueOf(foodCalories));                                                 //Asetetaan päivän aikana jo syödyt kalorit textViewiin

        TextView textView2 = findViewById(R.id.food_calories_goal);
        textView2.setText(String.valueOf(fetch.fetchCalories_goal()));                                               //Asetetaan päivittäinen kaloritavoite textViewiin

        ProgressBar progressBar = findViewById(R.id.food_progressBar);
        progressBar.setMax(fetch.fetchCalories_goal());                                                              //Asetetaan editysmispalkin maksimiarvoksi päivittäinen kaloritavoite
        progressBar.setProgress(foodCalories);                                                          //Asetetaan editysmispalkin edityminen päivän aikana syötyihin kaloreihin
    }


    public void updateFoodCalories(View view) {                                                          //Kutsutaan "Lisää" nappulaa painettaessa
        /**
         * Kutsutaan Food.java "Lisää" nappia painettaessa
         * Lisää käyttäjän syöttämät kalorit edellisiin kaloreihin
         * Päivittää kaloreiden määrän textViewiin
         * Tallentaa nykyisen kalorimäärän muistiin
         * Päivittää edistysmispalkin
         */
        Memory save =new Memory(getDefaultSharedPreferences(getApplicationContext()));
        EditText editText = findViewById(R.id.editTxFoodCalories);
        String temp = editText.getText().toString();                                                    //haetaan ui elementti idellä ja tallennetaan muuttujaan
        if (temp.contains(",") || (temp.contains("."))) {                                               //tarkastetaan käyttäjän syöte
            editText.getText().clear();
            Toast.makeText(getApplicationContext(), "Syötä kokonaisluku", Toast.LENGTH_SHORT).show();
        } else {
            int n = Integer.parseInt(editText.getText().toString());                                        //tallennetaan käyttäjän syöte muistiin

            Toast.makeText(getApplicationContext(), "Päivittäinen kaloritavoite asetettu.", Toast.LENGTH_SHORT).show();  //Ilmoitetaan käyttäjälle onnistunut tapahtuma

            foodCalories = foodCalories + n;                                                                  //Päivitetään syötyihin kaloreihin editTextissä syötetyt uudet
            TextView textView = findViewById(R.id.total_food_calories);
            textView.setText(String.valueOf(foodCalories));                                                 //Päivitetään syödyt kalorit textViewiin
            Toast.makeText(getApplicationContext(), n + " Kaloria lisätty.", Toast.LENGTH_SHORT).show(); //Annetaan toast, käyttäjä saa ilmoituksen lisäyksestä
            editText.getText().clear();                                                                     //tyhjennetään editText kenttä
            save.saveCal(String.valueOf(foodCalories));

            ProgressBar progressBar = findViewById(R.id.food_progressBar);
            progressBar.setProgress(foodCalories);                                                          //Päivitetään progressbar
        }
    }

}