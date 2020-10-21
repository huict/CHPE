package com.mygdx.game.PoseEstimation;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import com.mygdx.game.R;

import androidx.test.platform.app.InstrumentationRegistry;

import com.mygdx.game.Exceptions.InvalidVideoSplicerType;
import com.mygdx.game.Persistance.AppDatabase;
import com.mygdx.game.Persistance.PersistenceClient;
import com.mygdx.game.Persistance.Video.NNVideo;
import com.mygdx.game.VideoHandler.VideoSplicer;
import com.mygdx.game.VideoHandler.VideoSplicerFactory;
import com.mygdx.game.VideoHandler.VideoSplicerUriLegacy;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

/**
 * The type Session test.
 */
public class SessionTest {
    /**
     * The Collector enables multiple asserts
     */
    @Rule
    public ErrorCollector collector = new ErrorCollector();
    private MediaMetadataRetriever metadataRetriever;
    private Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();


    /**
     * Sets up.
     * Initializes the mediaMetaDataRetriever for later usage.
     */
    @Before
    public void setUp() {
        final AssetFileDescriptor afd = targetContext.getResources().openRawResourceFd(R.raw.example_person);
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        this.metadataRetriever = metadataRetriever;
    }

    /**
     * Validate default run.
     */
    @Test
    public void validateDefaultRun() {
        try {
            VideoSplicer videoSplicer = VideoSplicerFactory.getVideoSplicer(this.metadataRetriever);
            Session session = new Session(targetContext, videoSplicer);
            session.runVideo();

            AppDatabase appDatabase = PersistenceClient.getInstance(targetContext).getAppDatabase();

            NNVideo nnVideo = appDatabase.nnVideoDAO().getLastSession();

            collector.checkThat(nnVideo.height, is(1080));
            collector.checkThat(nnVideo.width, is(1920));
            collector.checkThat(nnVideo.frame_count, is((100L)));
            collector.checkThat(nnVideo.frames_per_second, is(24.54f));
            collector.checkThat(videoSplicer.isNextFrameAvailable(), is(false));
            collector.checkThat(videoSplicer.getFrameCount(), is(100L));
        } catch (InvalidVideoSplicerType invalidVideoSplicerType) {
            fail();
        }

    }

    /**
     * Validate legacy run.
     */
    @Test
    public void validateLegacyRun() {

        VideoSplicer videoSplicer = new VideoSplicerUriLegacy(this.metadataRetriever);
        Session session = new Session(targetContext, videoSplicer);
        session.runVideo();

        AppDatabase appDatabase = PersistenceClient.getInstance(targetContext).getAppDatabase();

        NNVideo nnVideo = appDatabase.nnVideoDAO().getLastSession();

        collector.checkThat(nnVideo.height, is(1080));
        collector.checkThat(nnVideo.width, is(1920));
        collector.checkThat(nnVideo.frame_count, is((100L)));
        collector.checkThat(nnVideo.frames_per_second, is(24.54f));
        collector.checkThat(videoSplicer.isNextFrameAvailable(), is(false));
        collector.checkThat(videoSplicer.getFrameCount(), is(100L));

    }


}
