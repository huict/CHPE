package com.mygdx.honestmirror.application.common.videohandler;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.webkit.URLUtil;

import com.mygdx.honestmirror.application.common.exceptions.InvalidVideoSplicerType;

import java.util.HashMap;


/**
 * The type Video splicer factory.
 */
public class VideoSplicerFactory {

    /**
     * Gets video splicer.
     *
     * @param m the m
     * @return the video splicer
     * @throws InvalidVideoSplicerType the invalid video splicer type
     */
    public static VideoSplicer getVideoSplicer(MediaMetadataRetriever m) throws InvalidVideoSplicerType {
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            // Function call getFrameAtIndex based on frame count requires
            // only for Pie (28) and newer versions
            return new VideoSplicerUri(m);
        }
        if (android.os.Build.VERSION.SDK_INT == 26 || android.os.Build.VERSION.SDK_INT == 27) {
            return new VideoSplicerUriLegacy(m);
        }
        throw new InvalidVideoSplicerType("Factory is running on invalid configuration",
                new Throwable("Android SDK version too low, minimum of 24"));
    }


}
