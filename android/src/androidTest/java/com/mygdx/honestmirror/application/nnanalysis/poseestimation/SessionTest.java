package com.mygdx.honestmirror.application.nnanalysis.poseestimation;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;

import androidx.test.platform.app.InstrumentationRegistry;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.application.common.exceptions.InvalidVideoSplicerType;
import com.mygdx.honestmirror.application.common.videohandler.VideoSplicer;
import com.mygdx.honestmirror.application.common.videohandler.VideoSplicerFactory;
import com.mygdx.honestmirror.application.common.videohandler.VideoSplicerUriLegacy;
import com.mygdx.honestmirror.data.persistance.AppDatabase;
import com.mygdx.honestmirror.data.persistance.PersistenceClient;
import com.mygdx.honestmirror.data.persistance.Video.NNVideo;

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
    private final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();


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
