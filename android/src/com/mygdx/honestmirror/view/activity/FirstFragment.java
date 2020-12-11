package com.mygdx.honestmirror.view.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.application.nnanalysis.feedback.FeedbackController;
import com.mygdx.honestmirror.view.ui.adapter.FeedbackListItemAdapter;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });

        TextView summary = view.findViewById(R.id.summary_textview);

        FeedbackController feedbackController = FeedbackController.getInstance();




        if (summary != null)
            summary.setText(feedbackController.getSummary());

        RecyclerView recyclerView = view.findViewById(R.id.feedback_item_recyclerview);

        FeedbackListItemAdapter adapter = new FeedbackListItemAdapter(feedbackController.getFeedbackElements());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
    }
}