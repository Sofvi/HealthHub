package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText addCourseText = findViewById(R.id.et_food_goal);
        addCourseText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            changeUserFood();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        int calories_goal = prefs.getInt("user_food_goal",
                0);
        TextView textView = findViewById(R.id.et_food_goal);
        textView.setText(String.valueOf(calories_goal));
    }

    public void changeUserFood(){
        Toast.makeText(getApplicationContext(),"Päivittäinen kaloritavoite asetettu.", Toast.LENGTH_SHORT).show();

        EditText editText = findViewById(R.id.et_food_goal);
        int n = Integer.parseInt(editText.getText().toString());

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_food_goal", n);
        editor.commit();

    }
}