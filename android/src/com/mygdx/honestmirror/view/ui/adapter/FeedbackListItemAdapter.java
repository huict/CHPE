package com.mygdx.honestmirror.view.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.view.activity.MediaControllerActivity;
import org.jetbrains.annotations.NotNull;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackItem;

import java.util.List;

public class FeedbackListItemAdapter extends RecyclerView.Adapter<FeedbackListItemAdapter.ViewHolder> {
    View view;
    static String feedback;
    static String shortFeed;

    public static String getFeedback() {
        return feedback;
    }

    public static String getShortFeed() {
        return shortFeed;
    }

    private final List<FeedbackItem> mValues;

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

        feedback = mValues.get(position).getFeedback();
        DebugLog.log("------------------------feedback: " + feedback + " --------------------------------");
        holder.mIdView.setText(feedback);

        shortFeed = mValues.get(position).getShortFeedback();
        holder.mContentView.setText(shortFeed);

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