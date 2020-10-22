package com.mygdx.game.VideoHandler;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.webkit.URLUtil;

import com.mygdx.game.Exceptions.InvalidVideoSplicerType;

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


    /**
     * Gets video splicer.
     *
     * @param m the m
     * @return the video splicer
     * @throws InvalidVideoSplicerType the invalid video splicer type
     */
    public static VideoSplicer getVideoSplicer(String m) throws InvalidVideoSplicerType {
        if (URLUtil.isValidUrl(m)) {

            if (android.os.Build.VERSION.SDK_INT > 28) {
                // Function call getFrameAtIndex based on frame count requires
                // only for Pie (28) and newer versions
                return new VideoSplicerUri(m);
            }
            return new VideoSplicerUriLegacy(m);
        }
        // Assuming we aren't talking about a Uri instance
        throw new InvalidVideoSplicerType("Factory is running on invalid string",
                new Throwable("Invalid splicer type: " + m));
    }

    /**
     * Gets video splicer.
     *
     * @param uri     the uri
     * @param context the context
     * @return the video splicer
     * @throws InvalidVideoSplicerType the invalid video splicer type
     */
    public static VideoSplicer getVideoSplicer(Uri uri, Context context) throws InvalidVideoSplicerType {
        if (android.os.Build.VERSION.SDK_INT > 28) {
            // Function call getFrameAtIndex based on frame count requires
            // only for Pie (28) and newer versions
            return new VideoSplicerUri(uri, context);
        }

        if (android.os.Build.VERSION.SDK_INT == 26 || android.os.Build.VERSION.SDK_INT == 27) {
            return new VideoSplicerUriLegacy(uri, context);
        }
        throw new InvalidVideoSplicerType("Factory is running on invalid configuration",
                new Throwable("Android SDK version too low, minimum of 24"));
    }

    /**
     * Gets video splicer.
     *
     * @param path the path
     * @param t    the t
     * @return the video splicer
     */
    public static VideoSplicer getVideoSplicer(String path, boolean t) {
        return new VideoSplicerUriLegacy(path, new HashMap<String, String>());

    }


}
