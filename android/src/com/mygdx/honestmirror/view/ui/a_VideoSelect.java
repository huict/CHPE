package com.mygdx.honestmirror.view.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.application.common.DebugLog;

import java.io.File;

//This screen is used open the vieo gallery and select a video to process.
public class a_VideoSelect extends AppCompatActivity {
    //Permission we need to proceed.
    public static final String[] allPermissions = new String[] {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.VIBRATE
    };

    //Uuri to the selected video.
    Uri videoUri;

    //Dialog box to confirm the selected video.
    Dialog dialog;

    // Filepath retrieved from the Uri.
    String filepath;

    // Selected video played back to the user through this view.
    VideoView videoView;

    // Button to return to the homescreen.
    ImageButton b_Home;

    // Button to open the video gallery.
    ImageButton b_selectVideo;

    //Thing that shows the media controls.
    MediaController mediaController;

    //Check to see if we've selected a video.
    boolean videoIsSelected = false;

    // Request code.
    final int SELECT_VIDEO_REQUEST = 1;

    //Android function override, asks for permissions of not granted already. Opens the video gallery after.
    // requestCode random request code.
    //permissions String array of permissions
    //grantResults Results to see if permissions have been granted or not.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(AAL.permissionsGranted(getApplicationContext(), allPermissions)) {
            openVideoGallery();
        }
    }

    //Default android constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video_select);
        AAL.setTitleBar(getWindow());

        b_Home = findViewById(R.id.homeButton);
        b_Home.setOnClickListener(v -> finish());

        dialog = new Dialog(this);
        mediaController = new MediaController(a_VideoSelect.this) {
            @Override
            public void show(int timeout) {
                super.show(timeout);
                setVisibility(VISIBLE);
            }

            @Override
            public void hide() {
                super.hide();
                setVisibility(INVISIBLE);
            }
        };

        b_selectVideo = findViewById(R.id.select_button);
        b_selectVideo.setOnClickListener(v -> {
            // if all permissions were granted already we can just open the video gallery
            // if not, request the missing permissions
            if(AAL.permissionsGranted(getApplicationContext(), allPermissions)) {
                //DebugLog.log("logger: Opening gallery");
                openVideoGallery();
            } else {
                //DebugLog.log("logger: Not opening gallery");
                AAL.requestPermissions(getApplicationContext(), a_VideoSelect.this, allPermissions);
            }
        });
    }

    //Shows the selected video confirmation dialog.
    public void showDialog() {
        dialog.setContentView(R.layout.layout_dialog);
        videoView = dialog.findViewById(R.id.videoView2);
        Button b_OK = dialog.findViewById(R.id.ok_button);
        Button b_Cancel = dialog.findViewById(R.id.cancel_button);
        TextView textView = dialog.findViewById(R.id.textView4);
        textView.setText(filepath);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        b_OK.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), a_Loading.class);
            intent.setData(videoUri);
            startActivity(intent);
        });
        b_Cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    // Initializes the video player used in the confirmation dialog.
    // name String version of the video URI
    public void initializePlayer(String name) {
        videoUri = Uri.parse(name);
        videoView.setVideoURI(videoUri);
        videoIsSelected = true;
        videoView.setOnPreparedListener(mp -> mp.setOnVideoSizeChangedListener((mp1, width, height) -> {
            mediaController.hide();
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            ((ViewGroup) mediaController.getParent()).removeView(mediaController);
            ((FrameLayout) dialog.findViewById(R.id.framertje))
                    .addView(mediaController);
        }));
        videoView.setVideoURI(videoUri);
        videoView.start();
    }

    // Opens the device's default video gallery in order to select a video.
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

    /*
    Gets the actual filename from a Uri.
    Very verbose to prevent compatibility issues.
    Source: https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content/25005243#25005243
     */
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    // Android function override, opens the confirmation dialog after selecting a video in the gallery.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO_REQUEST) {
                Toast toast = Toast.makeText(getApplicationContext(), data.getData().getPath(), Toast.LENGTH_LONG);
                toast.show();
                filepath = data.getData().getPath();
                new File(data.getData().getPath());
                filepath = getFileName(data.getData());
                showDialog();
                initializePlayer(data.getData().toString());
            }
        }
    }
}
