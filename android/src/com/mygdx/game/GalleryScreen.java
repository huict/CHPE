package com.mygdx.game;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.Serializable;
/**
 * Class where the user can select his/her video and start the analysis service
 * @author Gianluca Piccardo
 */
public class GalleryScreen extends AppCompatActivity implements Serializable {
    /**
     * Button declaration for on-screen buttons
     */
    Button videoSelectButton;
    Button homeScreenButton;
    Button startAnalysis;
    /**
     * VideoView and MediaController declaration for the embedded video view with media control UI.
     */
    VideoView videoView;
    MediaController mediaController;
    /**
     * int declaration to enter video function with.
     */
    private final int SELECT_VIDEO_REQUEST = 1;
    /**
     * Uri for the selectable video from the user.
     */
    Uri videoUri;
    /**
     * Toast message to confirm that the user started the analysis process.
     */
    Toast toast;
    /**
     * boolean to check if a video has been selected.
     */
    boolean videoIsSelected;
    /**
     * Constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_screen);
        /**
         * Goes to true when user selected a valid video file
         */
        videoIsSelected = false;
        /**
         * Android Version control for colored status bar
         */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(0.902f,0.188f,0.157f));
        }
        else {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
        }
        /**
         * Initializes buttons for onscreen use
         */
        homeScreenButton = findViewById(R.id.homeScreenButton);
        videoSelectButton = findViewById(R.id.videoGalleryScreenButton);
        startAnalysis = findViewById(R.id.startAnalysis);
        /**
         * Preparing VideoView environment
         */
        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        /**
         * Instantiates homeScreenButton with the correct function.
         */
        homeScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeScreen();
            }
        });
        /**
         * Instantiates videoSelectButton with the correct function.
         */
        videoSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoGallery();
            }
        });
        /**
         * Instantiates startAnalysisButton with the correct function.
         */
        startAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
    }
    /**
     * Starts a new Intent to open up the home-screen.
     * @author Gianluca Piccardo
     * @return void
     */
    public void openHomeScreen() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }
    /**
     * Starts a new Intent to open up the Android gallery and lets the user select
     * his/her video
     * @author Gianluca Piccardo
     * @return void
     */
    public void openVideoGallery() {
        Intent intent;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
        }
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, SELECT_VIDEO_REQUEST);
    }
    /**
     * Starts the Service with the correct Uri and displays a Toast message
     * @author Gianluca Piccardo
     * @return void
     */
    public void startService() {
        if(videoIsSelected) {
            toast = Toast.makeText(getApplicationContext(), "Started video analysis, this could take a while", Toast.LENGTH_LONG);
            toast.show();
            Intent pauseIntent = new Intent(this, ProcessingScreenActivity.class);
            Intent serviceIntent = new Intent(this, ForegroundService.class);
            serviceIntent.putExtra("URI", videoUri.toString());
            serviceIntent.setData(videoUri);
            ContextCompat.startForegroundService(this, serviceIntent);
            startActivity(pauseIntent);
        }
        else {
            toast = Toast.makeText(getApplicationContext(), "Failed to load video path", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    /**
     * Initializes the VideoView with the correct video
     * @author Gianluca Piccardo
     * @param name Uri path from onActivityResult Function
     * @return void
     */
    public void initializePlayer(String name) {
        videoUri = Uri.parse(name);
        videoView.setVideoURI(videoUri);
        videoIsSelected = true;
    }
    /**
     * Goes here after startActivityForResult inside the openGalleryFunction
     * and calls initializePlayer function with the correct data
     * @author Gianluca Piccardo
     * @param requestCode unique number to keep track of request
     * @param resultCode unique number to keep track of result
     * @param data the returning data from startActivityForResult()
     * @return void
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO_REQUEST) {
                initializePlayer(data.getData().toString());
            }
        }
    }
}