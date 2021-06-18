package com.mygdx.honestmirror.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mygdx.honestmirror.GlobalApplication;
import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.view.ui.a_Home;

public class HelpAppActivity extends AppCompatActivity {
    Button b_previous;
    TextView t_HelpPage;
    TextView t_intro;
    GlobalApplication globalApplication = a_Home.getGlobalApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_app);

        b_previous = findViewById(R.id.previous);
        t_HelpPage = findViewById(R.id.HelpPage);
        t_intro = findViewById(R.id.intro);

        String previous = globalApplication.getLayoutMessages().get(4);
        String helpIntro = globalApplication.getLayoutMessages().get(3);
        String helpPage = globalApplication.getLayoutMessages().get(2);

        b_previous.setText(previous);
        t_intro.setText(helpIntro);
        t_HelpPage.setText(helpPage);

        b_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(HelpAppActivity.this, a_Home.class);
            startActivity(intent);
            }
        });
    }




}