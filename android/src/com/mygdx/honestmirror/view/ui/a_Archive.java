package com.mygdx.honestmirror.view.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mygdx.honestmirror.R;

//Archive class that handles the presentation archive.
//It contains the previous sessions and allows the user to revisit or delete previous presentations.
public class a_Archive extends AppCompatActivity {

    //Android Activity constructor.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_archive);
        AAL.setTitleBar(getWindow());
    }
}
