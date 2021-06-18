package com.mygdx.honestmirror.view.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackItem;
import com.mygdx.honestmirror.view.activity.MediaControllerActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FeedbackListItemAdapter extends RecyclerView.Adapter<FeedbackListItemAdapter.ViewHolder> {
    View view;
    static String feedback;
    static String shortFeed;
    private final List<FeedbackItem> mValues;

    public static String getFeedbackName() {
        return feedback;
    }

    public static String getFeedbackDescription() {
        return shortFeed;
    }

    public FeedbackListItemAdapter(List<FeedbackItem> items) {
        mValues = items;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_feedback_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mIdView.setText(mValues.get(position).getName());
        holder.mContentView.setText(mValues.get(position).getDescription());
        feedback = mValues.get(position).getName();
        shortFeed = mValues.get(position).getDescription();

        holder.mButton.setOnClickListener(v -> {
            //https://stackoverflow.com/questions/28767413/how-to-open-a-different-activity-on-recyclerview-item-onclick
            Intent intent = new Intent(holder.context, MediaControllerActivity.class);
            holder.context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        Button mButton;
        Context context;
        public FeedbackItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.description_textView);
            mButton = view.findViewById(R.id.button3);
            this.context = view.getContext();
        }

//        @NotNull
//        @Override
//        public String toString() {
//            return super.toString() + " contentView: '" + mContentView.getText() + "'";
//        }
    }
}