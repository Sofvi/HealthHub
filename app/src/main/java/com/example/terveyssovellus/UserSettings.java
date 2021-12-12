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

/*
APUAA
 */
public class UserSettings extends AppCompatActivity {
    //muuttuja painolle
    private double weight = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //haetaan ui elementit myöhempää käyttöä varten
        EditText editWeight = findViewById(R.id.et_weight);
        EditText editFood = findViewById(R.id.et_food_goal);
        //määritetään paikka muistissa jossa haetaan tiedot
        SharedPreferences prefs3 = getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        //Haetaan päivä+aika
        Calendar calendar = Calendar.getInstance();
        //Asetetaan currentDateKey muotoon pp.kk.vvvv, käytetään Keynä sharedPreferensseissä
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        //muistista haettu tieto tallennetaan muuttujaan
        String jsonText = prefs3.getString("e"+currentDate, null);
        //ehto jossa tarkistetaan että muistissa oli tallenttu tietoa, estää mahdolliset virhetilanteet
        if(jsonText!=null){
           //Pilkotaan tieto joka on haettu muistista
            String[] parsed = jsonText.split(",");
            //muuttujia stringin pilkkomista varten
            String str;
            String str2;
            int sum = 0;
            //looppi jossa pilkotaan ja lopulta asetetaan ui:hin näkyviin tieto muistista
            for (int i = 0;i< parsed.length;i++) {
                //sijoitetaan muuttujaan parsettu osuus
               str=parsed[i];
               //tarkistetaan että kyseessä on käyttäjän syöte
               if(str.contains("Muu")) {
                   //parsetaan tieto siten että jäljelle jää enään pelkät numerot
                   str2 = str.replace("\"]", "");
                   str2 = str2.replace("[\"", "");
                   str2 = str2.replaceAll("[^\\d-]", "");
                   //ohitetaan päivämäärä jolloin saadaan kalorit
                   str2 = str2.substring(8);
                   //mikäli useita syötteitä lasketaan yhteen
                   sum = sum + Integer.parseInt(str2);

                   //asetetaan poltetut kalorit näkyville UI:hin
                   EditText editBurnedCalories = findViewById(R.id.et_exercise_goal);
                   editBurnedCalories.setText(String.valueOf(sum));
               }
            }


        }
        //ehto jolla tarkastetaan että kyseinen data on tallennettu muistiin
        if (prefs3.getString("paino", null) != null) {
            //asetetaan muistista haettu data muuttujaan käsittelyä varten
            String paino = prefs3.getString("paino", "0");
            //pilkotaan muistista haetusta Stringistä pelkkä paino numerona
            String s1 = paino.substring(paino.indexOf(":")+1); //thx to ItamarG3 from stackoverflow
            paino = s1.replace(" kg","");
            s1 = paino.replace("\"]","");
            paino = s1.split("\"")[0]; // thx to assylias from stackoverflow
            weight = Double.parseDouble(paino);
            //asetetaan paino näkyville ui:hin
            editWeight.setText(String.valueOf(weight));
        }

        //tarkkaillaan millon käyttäjä on syöttänyt tietoa päivittäinen kaloritavote osioon ja reakoidaan entterin painallukseen
        editFood.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            //functio kutsu jossa tallennetaan datan ja asetetaan se näkyville
                            changeUserFood();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        // määritetään ui elementti muuttujaan
        EditText editHeight = findViewById(R.id.et_height);
        //tarkkaillaan millon käyttäjä on syöttänyt uuden pituuden ja reakoidaan entterin painallukseen
        editHeight.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            //functio kutsu jossa tallennetaan datan ja asetetaan se näkyville
                            changeUserHeight();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        //tarkkaillaan millon käyttäjä on syöttänyt uuden painolukeman ja reakoidaan entterin painallukseen
        editWeight.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            //tallennetaan paino muistiin ja asetetaan näkyville
                            changeUserWeight();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        //määritetään paikka muistissa josta haetaan kaloritavoite
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        int calories_goal = prefs.getInt("user_food_goal", 0);
        //haetaan elemennti ui:sta id:llä ja lisätään muuttujaan
        TextView textView = findViewById(R.id.et_food_goal);
        //asetetaan elemmenttiin uusi arvo
        textView.setText(String.valueOf(calories_goal));
       //määritetään paikka muistista josta haetaan pituus
        SharedPreferences prefs2 = getDefaultSharedPreferences(getApplicationContext());
        int user_height = prefs2.getInt("user_height", 0);
        //haetaan elemennti ui:sta id:llä ja lisätään muuttujaan
        TextView textView2 = findViewById(R.id.et_height);
        //asetetaan pituus näkyville ui:hin
        textView2.setText(String.valueOf(user_height));
    }
// functio jolla tallennetaan muistiin kaloritavoite
    public void changeUserFood(){
        //ilmoitetetaan käyttäjälle että kalori tavoite on asetettu
        Toast.makeText(getApplicationContext(),"Päivittäinen kaloritavoite asetettu.", Toast.LENGTH_SHORT).show();
       //haetaan ui elementti id:llä ja asetetaan muuttujaan
        EditText editText = findViewById(R.id.et_food_goal);
        //luodaan muuttuja jonka arvoksi asetetaan käyttäjän syöttämä arvo
        int n = Integer.parseInt(editText.getText().toString());
       //asetetaan muistipaikka johon halutaan tallentaa tieto
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        //tallennetaan muistiin kaloritavoite
        editor.putInt("user_food_goal", n);
        editor.commit();
    }
    //functio pituuden tallentamista muistiin varten
    public void changeUserHeight(){
        //ilmoitetaan käyttäjälle että pituus on asetettu
        Toast.makeText(getApplicationContext(),"Pituus asetettu.", Toast.LENGTH_SHORT).show();
        //haetaan ui elementti idellä ja tallennetaan muuttujaan
        EditText editText = findViewById(R.id.et_height);
        //tallennetaan käyttäjän syöte muistiin
        int n = Integer.parseInt(editText.getText().toString());
        //Valitaan muistipaikka johon tallennetaan
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        //tallennetaan pituus muistiin
        editor.putInt("user_height", n);
        editor.commit();
    }
    //functio jolla tallennetaan käyttäjän paino muistiin
    public void changeUserWeight(){
        //luodaan arraylist weight johon tallennetaan paino
        ArrayList<String> weight;
        weight = new ArrayList<String>();
        //haetaan ui elementti id:llä
        EditText editWeight = findViewById(R.id.et_weight);
        //ilmoitetaan käyttäjälle että paino on tallenettu
        Toast.makeText(getApplicationContext(),"Paino asetettu.", Toast.LENGTH_SHORT).show();
        //käännetään arraylist jotta uusin syöte olisi ensimmäisenä
        Collections.reverse(weight);
        ////Haetaan päivä+aika
        Calendar calendar = Calendar.getInstance();
        ////Asetetaan currentDateKey muotoon pp.kk.vvvv, käytetään Keynä sharedPreferensseissä
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        //luodaan stringi joka tallennetaan muistiin
        String s = currentDate + " Paino: " + editWeight.getText().toString() + " kg";
        //tarkastetaan käyttäjän syöte
        if (s.contains(",")) {
            //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
            s = s.replace(",", ".");
        }
        //tallennetaan Stringi arraylistiin
        weight.add(s);
        //käännetään arraylist jotta uusin tieto tulisi ensimmäiseksi
        Collections.reverse(weight);
        //valitaan muistipaikka johon tallennetaan
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        //luodaan gson objekti jotta saadaan arraylist tallennettua muistiin
        Gson gson = new Gson();
        //muutetaan tieto json muotoon
        String json = gson.toJson(weight);
        //tallennetaan tieto muistiin
        editor.putString("paino", json);
        editor.apply();


    }
}