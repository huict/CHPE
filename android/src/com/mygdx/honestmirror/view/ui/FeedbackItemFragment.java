package com.mygdx.honestmirror.view.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.application.nnanalysis.feedback.FeedbackController;
import com.mygdx.honestmirror.view.ui.adapter.FeedbackListItemAdapter;

import java.io.IOException;

// A fragment reprenting a list of Items.
public class FeedbackItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    // Mandatory empty constructor for the fragment manager to instantiate the
    // fragment (e.g. upon screen orientation changes).
    public FeedbackItemFragment() {
    }

    public static FeedbackItemFragment newInstance(int columnCount) {
        FeedbackItemFragment fragment = new FeedbackItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        view.findViewById(R.id.button3).setOnClickListener(view1 -> {
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_items_list, container, false);

        FeedbackController feedbackController = FeedbackController.getInstance();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            try {
                recyclerView.setAdapter(new FeedbackListItemAdapter(feedbackController.getFeedbackItems()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}