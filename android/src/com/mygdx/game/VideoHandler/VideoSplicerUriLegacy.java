package com.mygdx.game.VideoHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.mygdx.game.DebugLog;
import com.mygdx.game.Exceptions.InvalidFrameAccess;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Random;

import static android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

/**
 * The type Video splicer.
 */
public class VideoSplicerUriLegacy extends VideoSplicerUri {
    private static final String TAG = VideoSplicerUriLegacy.class.getSimpleName();
    /**
     * The M uri.
     */
    private String mUri;
    /**
     * The Uri.
     */
    public Uri uri;
    /**
     * The Iter time us.
     */
    public long iterTimeUs; // Used to indicate how long a single frame is on screen
    /**
     * The Total time.
     */
    private long totalTime; // TODO: Update to allow longer video's. The current limit would be the size of an integer.

    private static long microSecondsToMiliseconds = 1000;


    /**
     * Instantiates a new Video splicer.
     *
     * @param uri the uri
     */
    VideoSplicerUriLegacy(String uri) {
        super(uri);
    }

    /**
     * Instantiates a new Video splicer uri legacy.
     *
     * @param metadataRetriever the metadata retriever
     */
    public VideoSplicerUriLegacy(MediaMetadataRetriever metadataRetriever){
        super(metadataRetriever);
        initialiseVideoSplicerLegacy();
    }

    /**
     * Instantiates a new Video splicer uri legacy.
     *
     * @param uri     the uri
     * @param context the context
     */
    public VideoSplicerUriLegacy(Uri uri, Context context) {
        super(uri, context);
        this.uri = uri;

        initialiseVideoSplicerLegacy();
    }

    /**
     * Instantiates a new Video splicer uri legacy.
     *
     * @param uri the uri
     * @param map the map
     */
    public VideoSplicerUriLegacy(String uri, HashMap<String, String> map) {
        super(Uri.parse(uri), getApplicationContext());
        initialiseVideoSplicerLegacy();
    }


    private void initialiseVideoSplicerLegacy() {
        this.totalTime = getVideoDuration();

        // Calculating the iter frame count based on those values
        this.iterTimeUs = getFrameIterTime();


        // DebugLog.log(this.iterTimeUs + " duur frame per seconde");
        //this.frameCount = getAmountOfFrames();
        getAmountOfFrames();

    }

    private void getAmountOfFrames() {
        DebugLog.log(this.totalTime + " totale duur");
        DebugLog.log(this.iterTimeUs + " duur frame per seconde");
        long l = this.iterTimeUs / microSecondsToMiliseconds;
        DebugLog.log(l + " geconverteerde frame tijd");

        this.frameCount = totalTime / l;
        DebugLog.log(this.frameCount + " frame count");
    }


    /**
     * Gets next frame.
     *
     * @param frame the frame, MUST be atleast 1
     * @return the next frame
     */
    @Override
    public Bitmap getNextFrame(int frame) {

        if (frame == 0 || frame < 0 || frame > this.frameCount) {
            throw new InvalidParameterException("Value must between 1 - " + this.frameCount);
        }
        return this.mediaMetadataRetriever.getFrameAtTime(
                frame * this.iterTimeUs);
    }

    /**
     * Gets frame iter time.
     *
     * @return the frame iter time
     */
    private long getFrameIterTime() {

        Bitmap bp;
        long ms = 0;

        // Get initial frame
        bp = getFrameAtTime(ms);
        do{
            ms+=1; // Warning: Terribly slow. TODO: Dynamic increment
        }while(bp.sameAs(getFrameAtTime(ms)) );

        return ms;

    }

    /**
     * Gets next frame.
     *
     * @return the next frame
     * @throws InvalidFrameAccess the invalid frame access
     */
    @Override
    public Bitmap getNextFrame() throws InvalidFrameAccess {
        if (isNextFrameAvailable()) {

            // TODO: return this.mediaMetadataRetriever.getFrameAtIndex(this.framesProcessed);
            Bitmap mp = this.mediaMetadataRetriever.getFrameAtTime(
                    this.framesProcessed * this.iterTimeUs);
            this.framesProcessed += 1;
            return mp;
        }
        throw new InvalidFrameAccess("InvalidFrameAccess", new Throwable("Next Frame doesn't exist."));
    }


    private Bitmap getFrameAtTime(long ms) {
        return this.mediaMetadataRetriever.getFrameAtTime(
                ms);

    }


}
