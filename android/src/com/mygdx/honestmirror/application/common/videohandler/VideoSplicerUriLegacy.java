package com.mygdx.honestmirror.application.common.videohandler;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.application.common.exceptions.InvalidFrameAccess;

import java.security.InvalidParameterException;

public class VideoSplicerUriLegacy extends VideoSplicerUri {

    private String mUri;

    public Uri uri;

    // Used to indicate how long a single frame is on screen
    public long iterTimeUs;

    private long totalTime;

    private static final long microSecondsToMiliseconds = 1000;


    //Instantiates a new Video splicer uri legacy.
    public VideoSplicerUriLegacy(MediaMetadataRetriever metadataRetriever){
        super(metadataRetriever);
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
        //DebugLog.log(this.totalTime + " total time");
        //DebugLog.log(this.iterTimeUs + " duration frame per second");
        long l = this.iterTimeUs / microSecondsToMiliseconds;
        //DebugLog.log(l + " converted frame time");

        this.frameCount = totalTime / l;
        //DebugLog.log(this.frameCount + " frame count");
    }


    //Gets next frame.
    //the frame MUST be at least 1
    public Bitmap getNextFrame(int frame) {

        if (frame == 0 || frame < 0 || frame > this.frameCount) {
            throw new InvalidParameterException("Value must be between 1 - " + this.frameCount);
        }
        return this.mediaMetadataRetriever.getFrameAtTime(
                frame * this.iterTimeUs);
    }

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

    @Deprecated
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
