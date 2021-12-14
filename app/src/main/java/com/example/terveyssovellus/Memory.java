package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Luokka tallentaa tietoa laitteen muistiin käyttäen shared preferences
 * @author Suvi Laitinen
 * @version 13.12.2021
 */

    public class Memory {
        private SharedPreferences prefs;

        public Memory(SharedPreferences pref){
            this.prefs=pref;
        }

    /**
     * tallentaa painon muistiin käyttäen gson kirjastoa
     * @param weight on arraylist joka sisältää kaikki käyttäjän syöttämät painot
     */
    public void saveWeight(ArrayList<String>weight){
            Gson gson = new Gson();
            String json = gson.toJson(weight);
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putString("paino",json);
            editor.apply();
        }
         /** tarkastetaan käyttäjän syöte
          * vaihdetaan pilkku pisteeseen, estäen parsen ongelmat
          * sekä poistetaan välit
          * @param s käyttäjän syöte joka tarkistetaan
          * @return palauttaa alkuperäisen syötteen pilkut vaihdettuna pisteeseen ja ilman välejä
          * */
        public String verify(String s){
            String s1 =s;
            if (s1.contains(",")) {
                s1 = s1.replace(",", ".");
            }
            if(s1.contains(" ")) {
                s1 = s1.replace(" ", "");
            }
            if(s1.contains("-")) {
                s1 = s1.replace("-", "");
            }
            return s1;
        }
    /** tarkastetaan käyttäjän syöte
     * muokkaa käyttäjän syötettä
     * @param s käyttäjän syöte joka halutaan validoida
     * @return palauttaa käyttäjän syötteen muokattuna mikäli sisälsi laittomia merkkejä
     * */
        public String verifyCal(String s){

            String s1 =s;
            if (s1.contains(",")) {
                s1 = s1.replace(",", "");
            }
            if (s1.contains(".")) {
                s1 = s1.replace(".", "");
            }
            if(s1.contains(" ")) {
                s1 = s1.replace(" ", "");
            }
            return s1;
        }

    /**
     * Tallentaa painon muistiin tunnisteella "user_height"
     * @param temp käyttäjän syöte haettuna tekstikentästä
     */
    public void saveHeight(String temp){

            float n = Float.parseFloat(verify(temp));
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putFloat("user_height",n);
            editor.commit();
        }

    /**
     * Tallentaa käyttäjän syöttämän kaloritavoitteen muistiin tunnisteella "user_food_goal"
     * @param s käyttäjän antama tavoite haettuna tekstikentästä
     */
    public void saveFood(String s){
            int i = Integer.parseInt(verify(s));
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putInt("user_food_goal",i);
            editor.commit();
        }

    /**
     * Tallentaa käyttäjän syömät kalorit muistiin kyseisellä päivämäärällä tunnisteena
     * @param s käyttäjän syöte haettuna tekstikentästä
     */
    public void saveCal(String s){
            int i = Integer.parseInt(verifyCal(s));
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putInt(date(),i);
            editor.commit();
        }
    /**
     * Tallentaa liikuntasuoritukset mustiin json muodossa käyttäen gson kirjastoa
     * @param exer arraylist johon on kerätty kaikki käyttäjän syöttämät liikunta suoritukset
     * */
        public void saveExercise(ArrayList<String>exer){
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(exer);
            editor.putString("e"+date(), json);
            editor.apply();

        }

    /**
     * Hakee nykyisen päivän
     * @return palauttaa päivämäärän muodossa pp.kk.vvvv 
     */
    public String date(){
            Calendar calendar = Calendar.getInstance();                                                 //haetaan päivä + aika
            String currentDateKey = DateFormat.getDateInstance().format(calendar.getTime());
            return currentDateKey;
        }
    }

