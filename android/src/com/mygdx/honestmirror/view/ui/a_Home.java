package com.mygdx.honestmirror.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.view.activity.HelpAppActivity;

// Home screen class. This is the first screen you see when you start the app.
public class a_Home extends AppCompatActivity {

    Button b_archive;

    Button b_start;

    Button b_help_app;

    Button b_help_gloves;


    // Android function override.
    //This closes the app.
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    // Android default constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        b_start = findViewById(R.id.b_start);
        b_help_app = findViewById(R.id.b_help_app);
        b_help_gloves = findViewById(R.id.b_help_gloves);

        //b_start_2 = findViewById(R.id.b_start2);


        AAL.setTitleBar(getWindow());

        b_start.setOnClickListener(v -> launchIntent(a_VideoSelect.class));

        b_help_app.setOnClickListener(v -> launchIntent(HelpAppActivity.class));

        b_help_gloves.setOnClickListener(v -> launchIntent(HelpAppActivity.class));
    }

    //Helper function for creating and launching an intent.
    public void launchIntent(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
