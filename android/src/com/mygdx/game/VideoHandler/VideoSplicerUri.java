package com.mygdx.game.VideoHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.mygdx.game.DebugLog;
import com.mygdx.game.Exceptions.InvalidFrameAccess;

/**
 * The type Video splicer.
 */
public class VideoSplicerUri implements VideoSplicer {
    private static final String TAG = VideoSplicerUri.class.getSimpleName();
    private static final int META_VIDEO_FRAME_COUNT = 32;
    private static final int META_VIDEO_FRAME_RATE = 25; // METADATA_KEY_CAPTURE_FRAMERATE
    private static final int META_VIDEO_DURATION = 9;
    /**
     * The M uri.
     */
    private String mUri;
    /**
     * The Uri.
     */
    private Uri uri;

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
    long framesProcessed = 0;

    /**
     * Instantiates a new Video splicer.
     *
     * @param uri the uri
     */
    public VideoSplicerUri(String uri) {
        this.mUri = uri;

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
        this.uri = uri;

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
     * Gets iter time us.
     *
     * @return the iter time us
     */
    long getIterTimeUs() {
        return getIterTimeUs();
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

    /**
     * Gets next frame.
     *
     * @param frame the frame
     * @return the next frame
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public Bitmap getNextFrame(int frame) {
        Bitmap mp = this.mediaMetadataRetriever.getFrameAtIndex(
                frame);
        return mp;
    }


    /**
     * Gets next frame.
     *
     * @return the next frame
     * @throws InvalidFrameAccess the invalid frame access
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public Bitmap getNextFrame() throws InvalidFrameAccess {

        // TODO: Replace frameCount validator

        if (this.frameCount == -1) {
            this.getAmountOfFrames();
        }
        if (isNextFrameAvailable()) {
            Bitmap mp;
            try {
                mp = this.mediaMetadataRetriever.getFrameAtIndex(
                        Math.toIntExact(this.framesProcessed)); //TODO: Solve type cast juggling
                this.framesProcessed += 1;

            } catch (IllegalStateException ise) {
                mp = Bitmap.createBitmap(200, 200, Bitmap.Config.ALPHA_8);
            }
            return mp;
        }
        throw new InvalidFrameAccess("InvalidFrameAccess", new Throwable("Next Frame doesn't exist."));
    }


}
