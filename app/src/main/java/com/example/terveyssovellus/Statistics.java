package com.example.terveyssovellus;

import static android.graphics.Color.rgb;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Statistics extends AppCompatActivity {

    private double weight = 0;
    private float height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        updateBMI();
        updateFoodChart();
        updateWeightChart();


    }

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


