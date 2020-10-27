package com.mygdx.game.VideoHandler;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.mygdx.game.DebugLog;
import com.mygdx.game.Exceptions.InvalidVideoSplicerType;
import com.mygdx.game.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * The type Video splicer factory test.
 */
@RunWith(MockitoJUnitRunner.class)
public class VideoSplicerFactoryTest {
    private final int defaultSDK = android.os.Build.VERSION.SDK_INT;
    private MediaMetadataRetriever metadataRetriever;
    private Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    /**
     * Sets up.
     */
    @Before
    public void setUp() {

        DebugLog.log(Integer.toString(defaultSDK));
        final AssetFileDescriptor afd = targetContext.getResources().openRawResourceFd(R.raw.example_video);
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        this.metadataRetriever = metadataRetriever;
    }


    /**
     * New sdk validation.
     */
    @Test
    public void newSDKValidation() {
        try {
            VideoSplicer videoSplicer = VideoSplicerFactory.getVideoSplicer(this.metadataRetriever);
            assertEquals(VideoSplicerUri.class.getName(), videoSplicer.getClass().getName());
        } catch (InvalidVideoSplicerType ivs) {
            Log.e("VideoSplicer", ivs.getLocalizedMessage());
        }
    }

    /**
     * Old sdk validation.
     */
    @Test
    @TargetApi(24)
    public void oldSDKValidation() {
        try {
            VideoSplicer videoSplicer = VideoSplicerFactory.getVideoSplicer(this.metadataRetriever);
            assertEquals(VideoSplicerUriLegacy.class.getName(), videoSplicer.getClass().getName());
        } catch (InvalidVideoSplicerType ivs) {
            Log.e("VideoSplicer", ivs.getLocalizedMessage());
        }
    }

    /**
     * Invalid sdk validation.
     */
    @Test
    @TargetApi(22)
    public void invalidSDKValidation() {
        try {
            setFinalStatic(android.os.Build.VERSION.class.getField("SDK_INT"), 20);
            VideoSplicer videoSplicer = VideoSplicerFactory.getVideoSplicer(this.metadataRetriever);
            fail();
        } catch (InvalidVideoSplicerType ivs) {
            // Intentionally left blank
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Reverting sdk version.
     */
    @After
    public void revertingSDKVersion() {
    }


    private void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }
}
