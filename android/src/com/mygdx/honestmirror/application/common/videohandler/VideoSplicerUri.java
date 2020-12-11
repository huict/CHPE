package com.mygdx.honestmirror.application.common.videohandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.application.common.exceptions.InvalidFrameAccess;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.application.common.exceptions.InvalidFrameAccess;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.PoseNetHandler;

/**
 * The type Video splicer.
 */
public class VideoSplicerUri implements VideoSplicer {
    private static final String TAG = VideoSplicerUri.class.getSimpleName();
    private static final int META_VIDEO_FRAME_COUNT = 32;
    private static final int META_VIDEO_FRAME_RATE = 25; // METADATA_KEY_CAPTURE_FRAMERATE
    private static final int META_VIDEO_DURATION = 9;

    /**
     * The Media metadata retriever.
     */
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    /**
     * The length of the video
     */
    long totalTime = 0;
    /**
     * The Frame count.
     */
    long frameCount = -1;
    /**
     * The Frames processed.
     */
    int framesProcessed = 0;
    int timeProcessed = 0;

    /**
     * Instantiates a new Video splicer.
     *
     * @param uri the uri
     */
    public VideoSplicerUri(String uri) {

        // Accessing the file
        this.mediaMetadataRetriever.setDataSource(uri);

        // Getting the amount of frames in video
        this.getAmountOfFrames();
    }


    /**
     * Instantiates a new Video splicer.
     *
     * @param uri     the uri
     * @param context the context
     */
    public VideoSplicerUri(Uri uri, Context context) {

        // Accessing the file
        this.mediaMetadataRetriever.setDataSource(context, uri);

        // Getting the video duration
        this.getVideoDuration();

        // Getting the amount of frames in video
    }

    /**
     * Instantiates a new Video splicer uri.
     *
     * @param retriever the retriever
     */
    public VideoSplicerUri(MediaMetadataRetriever retriever){
        this.mediaMetadataRetriever = retriever;

        this.getVideoDuration();
        this.getAmountOfFrames();
    }


    /**
     * Gets frame count.
     *
     * @return the frame count
     */
    public long getFrameCount() {
        return this.frameCount;
    }

    /**
     * Gets frames processed.
     *
     * @return the frames processed
     */
    public long getFramesProcessed() {
        return framesProcessed;
    }

    /**
     * Gets video duration.
     *
     * @return the video duration
     * @throws NumberFormatException the number format exception
     */
    long getVideoDuration() throws NumberFormatException {
        try {
            String sTotalTime = this.mediaMetadataRetriever.extractMetadata(META_VIDEO_DURATION);
            this.totalTime = Long.parseLong(sTotalTime);
            return totalTime;
        } catch (NumberFormatException nfe) {
            DebugLog.log("125: Line Exception" + nfe);
            throw new NumberFormatException();
        }

    }

    private void getAmountOfFrames() {
        try {
            String sFrameCount = this.mediaMetadataRetriever.extractMetadata(META_VIDEO_FRAME_COUNT);
            this.frameCount = Integer.parseInt(sFrameCount);
        } catch (NumberFormatException nfe) {
            // TODO: Notify user of invalid file.
            Log.e(TAG, "NumberFormatException: " + nfe.getMessage());
        }
    }


    /**
     * Is next frame available boolean.
     *
     * @return the boolean
     */
    public boolean isNextFrameAvailable() {
        return this.framesProcessed + 1 <= this.frameCount;
    }

    public boolean isNextTimeAvailable() {
        return this.timeProcessed + 1 <= (this.totalTime * 1000 );
    }

    /**
     * Gets next frame.
     *
     * @param frame the frame
     * @return the next frame
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public Bitmap getNextFrame(int frame) {
        return this.mediaMetadataRetriever.getFrameAtIndex(
                frame);
    }

    /**
     * Gets next frame.
     *
     * @return the next frame
     * @throws InvalidFrameAccess the invalid frame access
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public Bitmap getNextFrame() throws InvalidFrameAccess {

        if (this.frameCount <= 0) {this.getAmountOfFrames();}

        if (isNextTimeAvailable()) {
            Bitmap mp;
            try {
                mp = this.mediaMetadataRetriever.getScaledFrameAtTime(this.timeProcessed, 0, 257, 257);
                //per 24 frames
                this.timeProcessed += 400000;

            } catch (IllegalStateException ise) {
                mp = Bitmap.createBitmap(257, 257, Bitmap.Config.ALPHA_8);
            }
            return mp;
        }
        throw new InvalidFrameAccess("InvalidFrameAccess", new Throwable("Next Frame doesn't exist."));
    }

    @SuppressWarnings("Convert2MethodRef")
    @RequiresApi(api = Build.VERSION_CODES.P)
    public List<Person> getPersons(PoseNetHandler pnh){
        List<Person> personsThread1;
        List<Person> personsThread2;
        List<Person> personsThread3;
        List<Person> personsThread4;
        List<Person> personsThread5;

        Thread1 thread1 = new Thread1(this.totalTime, pnh, this.mediaMetadataRetriever);
        Thread2 thread2 = new Thread2(this.totalTime, pnh, this.mediaMetadataRetriever);
        Thread3 thread3 = new Thread3(this.totalTime, pnh, this.mediaMetadataRetriever);
        Thread4 thread4 = new Thread4(this.totalTime, pnh, this.mediaMetadataRetriever);
        Thread5 thread5 = new Thread5(this.totalTime, pnh, this.mediaMetadataRetriever);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        personsThread1 = thread1.getPersons();
        personsThread2 = thread2.getPersons();
        personsThread3 = thread3.getPersons();
        personsThread4 = thread4.getPersons();
        personsThread5 = thread5.getPersons();

        return Stream.of(personsThread1, personsThread2, personsThread3,
                personsThread4, personsThread5).flatMap(x -> x.stream())
                .collect(Collectors.toList());
    }


}
