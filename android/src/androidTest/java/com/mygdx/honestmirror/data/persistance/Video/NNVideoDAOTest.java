package com.mygdx.honestmirror.data.persistance.Video;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mygdx.honestmirror.data.persistance.AppDatabase;
import com.mygdx.honestmirror.data.persistance.PersistenceClient;

import static org.junit.Assert.assertEquals;

/**
 * The type Nn video dao test.
 */
@RunWith(AndroidJUnit4.class)
public class NNVideoDAOTest {
    private AppDatabase appDatabase;
    private final int frameCount = 24 * 5;
    private final int framesPerSecond = 24;
    private final int width=1920;
    private final int height=1080;
    private NNVideoDAO nnVideoDAO;

    /**
     * Sets up the configuration for tests
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() {
        // Ensure that the database name is NOT the actual database name
        this.appDatabase = PersistenceClient.getInstance(ApplicationProvider.getApplicationContext(), "debugDB").getAppDatabase();
        NNVideo nnSession = new NNVideo();
        nnSession.frame_count = this.frameCount;
        nnSession.frames_per_second = this.framesPerSecond;
        nnSession.height = this.height;
        nnSession.width = this.width;
        this.nnVideoDAO = this.appDatabase.nnVideoDAO();
        this.nnVideoDAO.insert(nnSession);

    }

    /**
     * FPS validator.
     */
    @Test
    public void getFramesPerSecond() {
        assertEquals(this.framesPerSecond,
                this.nnVideoDAO.getLastSession().frames_per_second,
                0.0);
    }

    /**
     * Gets frame count validator.
     */
    @Test
    public void getFrameCount() {
        assertEquals(this.frameCount, this.nnVideoDAO.getLastSession().frame_count, 0.0);
    }

    /**
     * Get width validator.
     */
    @Test
    public void getWidth(){
        assertEquals(this.width, this.nnVideoDAO.getLastSession().width, 0.0);
    }

    /**
     * Get height validator.
     */
    @Test
    public void getHeight(){
        assertEquals(this.height, this.nnVideoDAO.getLastSession().height, 0.0);
    }

    /**
     * Tear down.
     *
     */
    @After
    public void tearDown()  {
        this.nnVideoDAO.nukeTable();
    }
}