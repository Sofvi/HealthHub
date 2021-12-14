package com.example.terveyssovellus;

import android.content.SharedPreferences;

import java.text.DateFormat;
import java.util.Calendar;
/**
 * Luokka on tiedonkäsittelyä varten
 * @author Eetu Haverinen
 * @version 13.12.2021
 */

public class Fetch {
    private SharedPreferences prefs;

    public Fetch(SharedPreferences pref){
        this.prefs=pref;
    }
    /** Hakee muistista painon arraylist muotoon
     * parsetaan Stringistä erikoismerkit
     * */
    public String fetchWeightList(){

        String jsonText = this.prefs.getString("paino", null);
        if (prefs.getString("paino", null) != null) {                                       //ehtolause jolla tarkastetaan että muistissa on tallennettu tietoa
            String yett = jsonText.replace("\\", "");                                   //parsetaan stringistä pois kaikki erikoismerkit
            jsonText = yett.replace("[", "");
            yett = jsonText.replace("\"", "");
            jsonText = yett.replace("]", "");
        }
        return jsonText;
    }
    /** Hakee muistista uusimman painon
     * muistista haettu Stringi pilkotaan pelkiksi painon numeroiksi
     * */
    public double fetchWeight(){

        double weight = 0;
        String paino = this.prefs.getString("paino", "0");
        String s1 = paino.substring(paino.indexOf(":") + 1);                                          //pilkotaan muistista haetusta Stringistä pelkkä paino numerona
        paino = s1.replace(" kg", "");
        s1 = paino.replace("\"]", "");
        paino = s1.split("\"")[0];
        weight = Double.parseDouble(paino);
        return weight;
    }
    public float fetchHeight(){
        float user_height = this.prefs.getFloat("user_height", 0);
        return user_height;
    }
    public int fetchCalories_goal(){
        int calories_goal = prefs.getInt("user_food_goal", 0);
        return calories_goal;
    }
    public String fetchExercise(){
        /** poistetaan Stringistä erikoismerkit
         * parsetaan Stringi
         * */
        if(prefs.getString("e" + date(), null)!=null) {
            String jsonText = prefs.getString("e" + date(), null);
            String yett = jsonText.replace("\\", "");                                    //poistetaan stringistä erikoismerkkit ja parsetaan stringi
            jsonText = yett.replace("[", "");
            yett = jsonText.replace("\"", "");
            jsonText = yett.replace("]", "");
            return jsonText;
        }
        return null;
    }
    public int fetchDailyCalories(){
        int foodCalories = prefs.getInt(date(),0);
        return foodCalories;
    }
    private String date(){
        Calendar calendar = Calendar.getInstance();                                                         //Haetaan päivä ja aika
        String currentDateKey = DateFormat.getDateInstance().format(calendar.getTime());
        return currentDateKey;
    }

}