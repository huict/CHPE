package com.mygdx.honestmirror.view.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.view.service.ForegroundService;

public class MediaControllerActivity extends Activity {
    ForegroundService foregroundService = new ForegroundService();
    Uri uri = foregroundService.getOtherUri();

    //https://www.tutlane.com/tutorial/android/android-video-player-with-examples
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback_item_details);
        VideoView videoView = findViewById(R.id.videoView);

        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        //Starting VideView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }
}
