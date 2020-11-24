package com.mygdx.honestmirror.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.view.HomeScreen;

/**
 * Class to show the user previous received results
 * @author Gianluca Piccardo
 */
public class PreviousResultActivity extends AppCompatActivity {
    /**
     * Button to return the user to the home screen
     */
    Button homeScreenButton;
    /**
     * Constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_result);
        /**
         * Initializes the button
         */
        homeScreenButton = findViewById(R.id.homeScreenButton);
        homeScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviousResultActivity.this, HomeScreen.class);
                startActivity(intent);
            }
        });
    }
}
