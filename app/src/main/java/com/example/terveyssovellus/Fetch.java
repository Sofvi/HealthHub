package com.example.terveyssovellus;

import android.content.SharedPreferences;

import java.text.DateFormat;
import java.util.Calendar;

public class Fetch {
    private SharedPreferences prefs;

    public Fetch(SharedPreferences pref){
        this.prefs=pref;
    }
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
    public double fetchWeight(){
        double weight = 0;
        String paino = this.prefs.getString("paino", "0");                                  //asetetaan muistista haettu data muuttujaan käsittelyä varten
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
        String jsonText = prefs.getString("e"+date(), null);
        String yett= jsonText.replace("\\", "");                                    //poistetaan stringistä erikoismerkkit ja parsetaan stringi
        jsonText= yett.replace("[","");
        yett = jsonText.replace("\"","");
        jsonText= yett.replace("]","");
        return jsonText;
    }
    public int fetchDailyCalories(){
        int foodCalories = prefs.getInt(date(),0);
        return foodCalories;
    }
    private String date(){
        Calendar calendar = Calendar.getInstance();                                                         //Haetaan päivä+aika
        String currentDateKey = DateFormat.getDateInstance().format(calendar.getTime());
        return currentDateKey;
    }

}