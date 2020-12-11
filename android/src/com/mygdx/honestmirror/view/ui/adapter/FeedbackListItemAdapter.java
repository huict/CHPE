package com.mygdx.honestmirror.view.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.application.domain.feedback.RawFeedbackElement;

import java.util.List;


public class FeedbackListItemAdapter extends RecyclerView.Adapter<FeedbackListItemAdapter.ViewHolder> {

    private final List<RawFeedbackElement> mValues;

    public FeedbackListItemAdapter(List<RawFeedbackElement> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mIdView.setText(mValues.get(position).getFeedback().toString());

        holder.mContentView.setText(mValues.get(position).getShortFeedback());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public RawFeedbackElement mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.description_textView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}