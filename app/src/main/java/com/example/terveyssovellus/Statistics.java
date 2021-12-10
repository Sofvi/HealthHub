package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Statistics extends AppCompatActivity {

    private double weight = 0;
    private int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        updateBMI();
        updateFoodChart();


    }

    private void updateBMI() {
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        String paino = prefs.getString("paino",
                "0");
        String s1 = paino.substring(paino.indexOf(":") + 1); //thx to ItamarG3 from stackoverflow
        paino = s1.replace(" kg", "");
        s1 = paino.replace("\"]", "");
        paino = s1.split("\"")[0]; // thx to assylias from stackoverflow

        weight = Double.parseDouble(paino);

        SharedPreferences prefs2 = getDefaultSharedPreferences(getApplicationContext());
        height = prefs2.getInt("user_height",
                0);

        double bmi = weight / ((double) height / 100) / ((double) height / 100);
        String bmi2 = String.format("%.2f", bmi);

        TextView textView = findViewById(R.id.tv_bmi);
        textView.setText("Painoindeksi: " + bmi2);
    }

    private void updateFoodChart() {
        BarChart chart = (BarChart) findViewById(R.id.severityBarChart);


        List<BarEntry> entries = new ArrayList<BarEntry>();

        chart.getDescription().setEnabled(false);

        for (int i = 1; i < 8; i++) {

            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.DATE, -7+i);
            String currentDateKey = DateFormat.getDateInstance().format(calendar.getTime());

            SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
            int foodCalories = prefs.getInt((currentDateKey),
                    0);


            entries.add(new BarEntry(i, foodCalories));

        }


/*        entries.add(new BarEntry(2, 1));
        entries.add(new BarEntry(3, 2));
        entries.add(new BarEntry(4, 3));
        entries.add(new BarEntry(5, 4));
        entries.add(new BarEntry(6, 5));
        entries.add(new BarEntry(7, 6));
*/

        BarDataSet dataSet = new BarDataSet(entries, "Kaloreita syöty");
        BarData data = new BarData(dataSet);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        chart.setData(data);
        chart.invalidate();
    }

    public class DayAxisValueFormatter extends ValueFormatter {

        Calendar calendar = Calendar.getInstance();
        String currentDateKey = DateFormat.getDateInstance().format(calendar.getTime());

        String[] mDays = {String.valueOf(Calendar.DATE-7), "TU", "WE", "TH", "FR", "SA", "SU",};


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


