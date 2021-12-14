package com.example.terveyssovellus;

/*
Kuvaajien luomiseen käytetään MPAndoirdChart kirjastoa.
https://github.com/PhilJay/MPAndroidChart

License for MPAndroidChart:

Copyright 2020 Philipp Jahoda
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.


 */

import static android.graphics.Color.rgb;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Luokka on Seuranta sivun näyttö
 * @author Tuomo Muttonen
 * @version 12.12.2021
 */

public class Statistics extends AppCompatActivity {

    private double weight = 0;
    private float height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        updateBMI();                                                                                    //Kutsutaan kaikki metodit
        updateFoodChart();
        updateWeightChart();
        updateBurnedCalories();


    }
    /**
     * Hakee muisista päivän aikana poltetut kalorit ja lisää ne yhteen
     * Päivittää päivän aikana yhteensä poltetut kalorit Activityyn
     */
    private void updateBurnedCalories(){

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
             //   if(str.contains("Muu")) {
                    //parsetaan tieto siten että jäljelle jää enään pelkät numerot
                    str2 = str.replace("\"]", "");
                    str2 = str2.replace("[\"", "");
                    str2 = str2.replaceAll("[^\\d-]", "");
                    //ohitetaan päivämäärä jolloin saadaan kalorit
                        if(currentDate.length()<10){
                            int yeet=10-currentDate.length();
                            str2=str2.substring(8-yeet);
                        }else {
                            str2 = str2.substring(8);
                        }
                    //mikäli useita syötteitä lasketaan yhteen
                    sum = sum + Integer.parseInt(str2);

                    //asetetaan poltetut kalorit näkyville UI:hin
                    TextView editBurnedCalories = findViewById(R.id.tv_poltetutkalorit);
                    editBurnedCalories.setText("Kaloreita poltettu: " + String.valueOf(sum));
               // }
            }
        }
    }

    /**
     * Hakee muistista tallennetut painot
     * Muotoilee kuvaajan
     * Luo tallennetuista painoista kuvaajan ja päivittää sen Activityyn
     */
    private void updateWeightChart() {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        ArrayList<Float> painot = new ArrayList<Float>();

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        String painoString = prefs.getString("paino",                                               //Haetaan aikaisemmin tallennetut painot, jotka ovat Stringinä
                "0");


        for (int i = 0; i < 7; i++) {
/*
Otetaan muistiin tallennetusta Stringistä 7 ensimmäistä painoa lukuina
Painot tallennetaan painot listaan float:ina
*/
            String s1 = painoString.substring(painoString.indexOf(":") + 1);
            painoString = s1;
            float paino = Float.parseFloat(s1.split(" kg")[0]);
            painot.add(paino);
        }

        Collections.reverse(painot);                                                                    //Käännetään painot lista toisin päin, jotta ne voidaan syöttää taulukkoon oikein


        for (int i = 0; i < 7; i++) {
/*
Tallennetaan painot entries listaan
 */
            entries.add(new BarEntry(i, painot.get(i)));
            Log.d("ZZZZ", "updateWeightChart: " + painot.get(i));
        }


/*
Kasataan kuvaaja ja asetetaan se näkymään.
 */
        LineChart chart = findViewById(R.id.paino_linechart);
        LineDataSet lineDataSet1 = new LineDataSet(entries, "Paino");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        chart.getDescription().setEnabled(false);
        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(9);
        xAxis.setValueFormatter(xAxisFormatter);

        LineData data = new LineData(dataSets);
        lineDataSet1.setColor(rgb(39, 78, 41));

        chart.setData(data);

    }

    /**
     * Ottaa viimeisimmäksi tallennetun painon ja pituuden
     * Laskee painoindeksin ja asettaa sen näkyviin
     */
    private void updateBMI() {

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        String paino = prefs.getString("paino",                                                     //Haetaan tallennettujen painojen string
                "0");
        String s1 = paino.substring(paino.indexOf(":") + 1); //thx to ItamarG3 from stackoverflow
        paino = s1.split(" kg")[0];

        weight = Double.parseDouble(paino);                                                             //Stringistä otetaan uusin paino arvo doubleksi

        SharedPreferences prefs2 = getDefaultSharedPreferences(getApplicationContext());
        height = prefs2.getFloat("user_height",                                                       //Haetaan käyttäjän pituus, joka asetetaan UserSettings activityssä
                0);

        double bmi = weight / ((double) height / 100) / ((double) height / 100);                        //laskukaava BMIlle
        String bmi2 = String.format("%.2f", bmi);                                                       //Leikataan BMIstä ylimääräiset desimaalit

        TextView textView = findViewById(R.id.tv_bmi);
        textView.setText("Painoindeksi: " + bmi2);                                                      //Asetetaan BMI textViewiin
    }

    /**
     * Hakee muisista syödyt kalorit
     * Muotoilee kuvaajan
     * Luo tallennetuista kaloreista kuvaajan
     */
    private void updateFoodChart() {

        BarChart chart = (BarChart) findViewById(R.id.severityBarChart);

        List<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 1; i < 8; i++) {

            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.DATE, -7 + i);
            String currentDateKey = DateFormat.getDateInstance().format(calendar.getTime());        //Haetaan currenDateKeylle päivämäärä -7+i, joka antaa viikon ajalta painot

            SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
            int foodCalories = prefs.getInt((currentDateKey),
                    0);
            entries.add(new BarEntry(i, foodCalories));                                             //Lisätään päivittäiset kalorit entries listaan

        }

/*
Kasataan kuvaaja ja asetetaan se näkymään
 */
        BarDataSet dataSet = new BarDataSet(entries, "Kaloreita syöty");
        BarData data = new BarData(dataSet);
        dataSet.setColor(rgb(39, 78, 41));

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        chart.getDescription().setEnabled(false);
        chart.setData(data);
        chart.invalidate();

    }

/*
Asetetaan X akselille päivämäärä viimeiselle arvolle

https://stackoverflow.com/questions/45320457/how-to-set-string-value-of-xaxis-in-mpandroidchart
Arjun G reply
 */

    /**
     * Asettaa kuvaajien X-akselille arvot
     */
    public class DayAxisValueFormatter extends ValueFormatter {

        Calendar calendar = Calendar.getInstance();
        String currentDateKey = DateFormat.getDateInstance().format(calendar.getTime());

        String[] mDays = {" ", " ", " ", " ", " ", " ", currentDateKey};


        private final BarLineChartBase<?> chart;
        private int count;

        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {

            String day = "";
            try {
                day = mDays[count];
            } catch (IndexOutOfBoundsException ex) {
                count = 0;
                day = mDays[count];
            }

            count++;
            return day;
        }
    }
}


