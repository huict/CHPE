package com.mygdx.game.VideoHandler;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;

import androidx.test.platform.app.InstrumentationRegistry;

import com.mygdx.game.Exceptions.InvalidFrameAccess;
import com.mygdx.game.R;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;


import static com.mygdx.game.VideoHandler.HelperTests.getBlue;
import static com.mygdx.game.VideoHandler.HelperTests.getGreen;
import static com.mygdx.game.VideoHandler.HelperTests.getRed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * The type Video splicer uri test.
 */
@RunWith(MockitoJUnitRunner.class)
public class VideoSplicerUriTest {
    /**
     * The Collector.
     */
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    private Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private VideoSplicerUri videoSplicerUri;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        final AssetFileDescriptor afd = targetContext.getResources().openRawResourceFd(R.raw.example_video);
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        this.videoSplicerUri = new VideoSplicerUri(metadataRetriever);
    }

    /**
     * Validate frame count.
     */
    @Test
    public void validateFrameCount() {
        assertEquals(900, videoSplicerUri.getFrameCount());
    }

    /**
     * Outside of scope call.
     */
    @Test
    public void OutsideOfScopeCall() {
        videoSplicerUri.framesProcessed = 900;
        try {
            videoSplicerUri.getNextFrame();
            fail();
        } catch (InvalidFrameAccess invalidFrameAccess) {
            // Body left empty on purpose.
        }
    }

    /**
     * Dummy iteration.
     */
    @Test
    public void DummyIteration() {
        // Validating frames
        int multiplier = 30;

        // Iterating 0 seconds
        int pixel = videoSplicerUri.getNextFrame(0).getPixel(200, 200);


        collector.checkThat(63, CoreMatchers.is(getRed(pixel)));
        collector.checkThat(203, CoreMatchers.is(getBlue(pixel)));
        collector.checkThat(73, CoreMatchers.is(getGreen(pixel)));

        // Iterating 5 seconds
        int pixel6 = videoSplicerUri.getNextFrame(6 * multiplier).getPixel(200, 200);

        collector.checkThat(128, CoreMatchers.is(getRed(pixel6)));
        collector.checkThat(128, CoreMatchers.is(getBlue(pixel6)));
        collector.checkThat(128, CoreMatchers.is(getGreen(pixel6)));

        // Iterating 5 seconds
        int pixel10= videoSplicerUri.getNextFrame(11 * multiplier).getPixel(200, 200);
        collector.checkThat(234, CoreMatchers.is(getRed(pixel10)));
        collector.checkThat(35, CoreMatchers.is(getBlue(pixel10)));
        collector.checkThat(29, CoreMatchers.is(getGreen(pixel10)));
        // Iterating 5 seconds

        int pixel16 = videoSplicerUri.getNextFrame(16 * multiplier).getPixel(200, 200);
        collector.checkThat(162, CoreMatchers.is(getRed(pixel16)));
        collector.checkThat(163, CoreMatchers.is(getBlue(pixel16)));
        collector.checkThat(74, CoreMatchers.is(getGreen(pixel16)));

    }

}
