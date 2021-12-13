package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
            String s1 =s;
            if (s1.contains(",")) {                                                                     //tarkastetaan käyttäjän syöte
                s1 = s.replace(",", ".");                                               //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
            }
            if(s1.contains(" ")) {
                s1 = s.replace(" ", "");
            }
            return s1;
        }
        public String verifyCal(String s){
            String s1 =s;
            if (s1.contains(",")) {                                                                     //tarkastetaan käyttäjän syöte
                s1 = s.replace(",", "");                                                //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
            }
            if (s1.contains(".")) {                                                                     //tarkastetaan käyttäjän syöte
                s1 = s.replace(".", "");                                                //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
            }
            if(s1.contains(" ")) {
                s1 = s.replace(" ", "");
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
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();                                                                     //** käytetään gson kirjastoa jotta saadaan muutettua java objekti json muotoon
            String json = gson.toJson(exer);
            editor.putString("e"+date(), json);                                                         //** tallennetaan tieto jsno muodossa muistiin
            editor.apply();

        }
        public String date(){
            Calendar calendar = Calendar.getInstance();                                                 //Haetaan päivä+aika
            String currentDateKey = DateFormat.getDateInstance().format(calendar.getTime());
            return currentDateKey;
        }
    }

