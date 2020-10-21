package com.mygdx.game.UI;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mygdx.game.R;

/**
 * Describes a single card holder and its properties.
 */
public class c_CardHolder extends RecyclerView.ViewHolder {

    /**
     * preview image of the presentation.
     */
    ImageView imageView;

    /**
     * title and description of the presentation.
     */
    TextView title, description;

    /**
     * Constructor.
     * @param itemView External view to use for the card.
     */
    c_CardHolder(@NonNull View itemView) {
        super(itemView);

        this.imageView = itemView.findViewById(R.id.resultCardImage);
        this.title = itemView.findViewById(R.id.resultCardTitle);
        this.description = itemView.findViewById(R.id.resultCardDescription);
    }

}
