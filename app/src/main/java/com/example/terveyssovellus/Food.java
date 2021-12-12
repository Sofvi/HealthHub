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
 * @author Suvi Laitinen, Henri Vuento, Tuomo Muttonen, Eetu Haverinen
 * @version 12.12.2021
 */

public class Food extends AppCompatActivity {

    private int foodCalories = 0;                                                                       //muuttuja syödyille kaloreille
    Calendar calendar = Calendar.getInstance();                                                         //Haetaan päivä+aika
    String currentDateKey = DateFormat.getDateInstance().format(calendar.getTime());                    //Asetetaan currentDateKey muotoon pp.kk.vvvv, käytetään Keynä sharedPreferensseissä



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

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        foodCalories = prefs.getInt(currentDateKey,                                                     //Asetetaan kalorit muistista, jossa käytetään päivämäärää Keynä
                0);
        TextView textView = findViewById(R.id.total_food_calories);
        textView.setText(String.valueOf(foodCalories));                                                 //Asetetaan päivän aikana jo syödyt kalorit textViewiin

        SharedPreferences prefs2 = getDefaultSharedPreferences(getApplicationContext());
        int goal_calories = prefs2.getInt("user_food_goal",                                         //Haetaan muisista päiväkohtainen kaloritavoite, joka asetetaan UserSettingseissä, user_food_goal Keylle.
                0);
        TextView textView2 = findViewById(R.id.food_calories_goal);
        textView2.setText(String.valueOf(goal_calories));                                               //Asetetaan päivittäinen kaloritavoite textViewiin

        ProgressBar progressBar = findViewById(R.id.food_progressBar);
        progressBar.setMax(goal_calories);                                                              //Asetetaan editysmispalkin maksimiarvoksi päivittäinen kaloritavoite
        progressBar.setProgress(foodCalories);                                                          //Asetetaan editysmispalkin edityminen päivän aikana syötyihin kaloreihin
    }



    public void updateFoodCalories(View view){                                                          //Kutsutaan "Lisää" nappulaa painettaessa
        /**
         * Kutsutaan Food.java "Lisää" nappia painettaessa
         * Lisää käyttäjän syöttämät kalorit edellisiin kaloreihin
         * Päivittää kaloreiden määrän textViewiin
         * Tallentaa nykyisen kalorimäärän muistiin
         * Päivittää edistysmispalkin
         */

        EditText editText = findViewById(R.id.editTxFoodCalories);
        int n = Integer.parseInt(editText.getText().toString());                                        //Otetaan käyttäjän syöttämä luku editTextistä
        foodCalories = foodCalories+n;                                                                  //Päivitetään syötyihin kaloreihin editTextissä syötetyt uudet
        TextView textView = findViewById(R.id.total_food_calories);
        textView.setText(String.valueOf(foodCalories));                                                 //Päivitetään syödyt kalorit textViewiin
        Toast.makeText(getApplicationContext(),n + " Kaloria lisätty.", Toast.LENGTH_SHORT).show(); //Annetaan toast, käyttäjä saa ilmoituksen lisäyksestä
        editText.getText().clear();                                                                     //tyhjennetään editText kenttä

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(currentDateKey, foodCalories);                                                    //Tallennetaan kalorit muistiin, Keynä käytetään päivämäärää
        editor.commit();

        ProgressBar progressBar = findViewById(R.id.food_progressBar);
        progressBar.setProgress(foodCalories);                                                          //Päivitetään progressbar
    }


}