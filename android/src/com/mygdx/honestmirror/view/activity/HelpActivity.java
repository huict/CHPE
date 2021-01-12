package com.mygdx.honestmirror.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.view.ui.a_Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity {
    Button previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        previous = findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(HelpActivity.this, a_Home.class);
            startActivity(intent);
            }
        });
    }




}