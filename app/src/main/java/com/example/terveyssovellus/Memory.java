package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Luokka on tiedonkäsittelyä varten
 * @author Suvi Laitinen
 * @version 13.12.2021
 */

    public class Memory {
        private SharedPreferences prefs;

        public Memory(SharedPreferences pref){
            this.prefs=pref;
        }
        public void saveWeight(ArrayList<String>weight){
            Gson gson = new Gson();
            String json = gson.toJson(weight);
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putString("paino",json);
            editor.apply();
        }
        public String verify(String s){
            /** tarkastetaan käyttäjän syöte
             * vaihdetaan pilkku pisteeseen, estäen parsen ongelmat
             * */
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
        public String verifyCal(String s){
            /** tarkastetaan käyttäjän syöte
             * vaihdetaan pilkku pisteeseen, estäen parsen ongelmat
             * */
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
        public void saveHeight(String temp){

            float n = Float.parseFloat(verify(temp));
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putFloat("user_height",n);
            editor.commit();
        }
        public void saveFood(String s){
            int i = Integer.parseInt(verify(s));
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putInt("user_food_goal",i);
            editor.commit();
        }
        public void saveCal(String s){
            int i = Integer.parseInt(verifyCal(s));
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putInt(date(),i);
            editor.commit();
        }
        public void saveExercise(ArrayList<String>exer){
            /** käytetään gson kirjastoa jotta saadaan muutettua java objekti json muotoon
             * tallennetaan tieto json muodossa muistiin
             * */
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(exer);
            editor.putString("e"+date(), json);
            editor.apply();

        }
        public String date(){
            Calendar calendar = Calendar.getInstance();                                                 //haetaan päivä + aika
            String currentDateKey = DateFormat.getDateInstance().format(calendar.getTime());
            return currentDateKey;
        }
    }

