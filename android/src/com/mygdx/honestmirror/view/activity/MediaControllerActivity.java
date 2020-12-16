package com.mygdx.honestmirror.view.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.view.service.ForegroundService;

public class MediaControllerActivity extends AppCompatActivity {
    ForegroundService foregroundService = new ForegroundService();
    Uri uri = foregroundService.getOtherUri();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback_item_details);
        VideoView videoView = findViewById(R.id.videoView);

        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        //Starting VideoView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }
}
