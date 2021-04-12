package com.mygdx.honestmirror.view.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.view.service.ForegroundService;
import com.mygdx.honestmirror.view.ui.adapter.FeedbackListItemAdapter;

public class MediaControllerActivity extends AppCompatActivity {
    Uri uri = ForegroundService.getOtherUri();
    String feedback_name = FeedbackListItemAdapter.getFeedbackName();
    String feedback_description = FeedbackListItemAdapter.getFeedbackDescription();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback_item_details);
        VideoView videoView = findViewById(R.id.videoView);

        //Set MediaController to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        DebugLog.log("------------------------feedback in activity: " + feedback_name + " --------------------------------");
        DebugLog.log("------------------------short feedback in activity: " + feedback_description + " --------------------------------");

        TextView feedbackView = findViewById(R.id.title);
        TextView shortFeedbackView = findViewById(R.id.shortFeedbackView);
        if(feedback_name == null){
            feedbackView.setText("no feedback retrieved");
        }
        if(feedback_description == null){
            shortFeedbackView.setText("no short feedback retrieved");
        }
        else{
            feedbackView.setText(feedback_name);
            shortFeedbackView.setText(feedback_description);
        }


        //Starting VideoView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        //videoView.seekTo(5000);
        videoView.start();
    }

    //TODO: seekto() with the appropiate timestamp
    //TODO: show bigger feedback
    //TODO: add title

}
