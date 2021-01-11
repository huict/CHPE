package com.mygdx.honestmirror.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.view.activity.HelpActivity;
import com.mygdx.honestmirror.view.activity.MainFeedbackActivity;

/**
 * Home screen class. This is the first screen you see when you start the app.
 */
public class a_Home extends AppCompatActivity {
    /**
     * Button to go the presentation archive.
     */
    Button b_archive;
    /**
     * Button to start the video selection process.
     */
    Button b_start;

    Button b_help;


    /**
     * Android function override.
     * This closes the app.
     */
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    /**
     * Android default constructor.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        b_archive = findViewById(R.id.b_archive);
        b_start = findViewById(R.id.b_start);
        b_help = findViewById(R.id.b_help);


        AAL.setTitleBar(getWindow());

        b_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchIntent(a_Results.class);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchIntent(a_VideoSelect.class);
            }
        });

        b_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchIntent(HelpActivity.class);
            }
        });
    }

    /**
     * Helper function for creating and launching an intent.
     * @param cls
     */
    public void launchIntent(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
