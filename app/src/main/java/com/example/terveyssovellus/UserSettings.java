package com.example.terveyssovellus;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

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
    }

    public void changeUserFood(){
        Toast.makeText(getApplicationContext(),"Päivittäinen kaloritavoite asetettu.", Toast.LENGTH_SHORT).show();

    }
}