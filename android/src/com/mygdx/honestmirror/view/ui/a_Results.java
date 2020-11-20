package com.mygdx.honestmirror.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mygdx.honestmirror.R;

import java.util.ArrayList;

/**
 * Screen that shows the presentation result cards.
 */
public class a_Results extends AppCompatActivity {
    /**
     * Image button that returns the user to the homescreen.
     */
    ImageView b_Home;

    /**
     * List of android cards.
     */
    RecyclerView cardList;

    /**
     * Adapter that holds all individual cards.
     */
    c_CardAdapter cardAdapter;

    /**
     * Activity layout used to inflate according to the nr of cards.
     */
    LinearLayoutManager layoutManager;

    /**
     * Android function override, stops the activity and pushes it off the stack.
     */
    @Override
    public void onStop() {
        super.onStop();
        finish();
    }

    /**
     * Android function override, sets the transition animation when pressing the back button.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Android default constructor.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_results);
        AAL.setTitleBar(getWindow());

        b_Home = findViewById(R.id.homeButton);
        b_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(a_Results.this, a_Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        cardList = findViewById(R.id.resultCardList);
        layoutManager = new LinearLayoutManager(this);
        cardList.setLayoutManager(layoutManager);
        cardAdapter = new c_CardAdapter(this, getCardList());
        cardList.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
    }

    /**
     * This is where the actual cards with properties are defined for now.
     * @return generated card list.
     */
    private ArrayList<c_ResultCard> getCardList() {
        ArrayList<c_ResultCard> cards = new ArrayList<>();

        c_ResultCard card = new c_ResultCard();
        card.setTitle("Hands Above Head");
        card.setDescription("Did you keep your hands above your head? @4:31");
        card.setImage(R.drawable.ic_launcher);
        for (int i = 0; i < 10; i++) {
            cards.add(card);
        }

        return cards;
    }
}
