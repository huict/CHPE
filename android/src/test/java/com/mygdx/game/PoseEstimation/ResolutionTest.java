package com.mygdx.honestmirror.application.nnanalysis.poseestimation;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.junit.Assert.assertThat;


public class ResolutionTest {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    private int width = 200;
    private int height = 200;
    private int modelWidth = 300;
    private int modelHeight = 400;

    private int randomInt = 100;
    private float randomFloat = 13.4f;

    @Test
    public void fixedConstructorTest() {
        Resolution resolution = new Resolution(
                this.width,
                this.height,
                this.modelWidth,
                this.modelHeight
        );

        collector.checkThat(this.width, CoreMatchers.is(resolution.getScreenWidth()));
        collector.checkThat(this.height, CoreMatchers.is(resolution.getScreenHeight()));
        collector.checkThat(this.modelWidth, CoreMatchers.is(resolution.getModelWidth()));
        collector.checkThat(this.modelHeight, CoreMatchers.is(resolution.getModelHeight()));
    }

    @Test
    public void adaptiveConstructorTest() {
        Resolution resolution = new Resolution(
                this.width,
                this.height,
                this.modelWidth
        );

        collector.checkThat(this.width, CoreMatchers.is(resolution.getScreenWidth()));
        collector.checkThat(this.height, CoreMatchers.is(resolution.getScreenHeight()));
        collector.checkThat(this.modelWidth, CoreMatchers.is(resolution.getModelWidth()));
        collector.checkThat(this.modelWidth, CoreMatchers.is(resolution.getModelHeight()));
    }


    @Test
    public void getWidthByRatioTestInt() {

        Resolution resolution = new Resolution(
                this.width,
                this.height,
                this.modelWidth
        );

        assertThat(resolution.getWidthByRatio(randomInt), CoreMatchers.is(66));
    }

    @Test
    public void getWidthByRatioTestFloat() {

        Resolution resolution = new Resolution(
                this.width,
                this.height,
                this.modelWidth
        );

        assertThat(resolution.getWidthByRatio(randomFloat), CoreMatchers.is(8));
    }


    @Test
    public void getHeightByRatioTestInt() {

        Resolution resolution = new Resolution(
                this.width,
                this.height,
                this.modelWidth
        );

        assertThat(resolution.getHeightByRatio(randomInt), CoreMatchers.is(66));

    }


    @Test
    public void getHeightByRatioTestFloat() {

        Resolution resolution = new Resolution(
                this.width,
                this.height,
                this.modelWidth
        );

        assertThat(resolution.getHeightByRatio(randomFloat), CoreMatchers.is(8));

    }

    @Test(expected = NumberFormatException.class)
    public void TooSmallHeightExceptionTest() {
        Resolution resolution = new Resolution(
                this.width,
                this.height,
                this.modelWidth
        );

        resolution.getHeightByRatio(-10.0f);
    }

    @Test(expected = NumberFormatException.class)
    public void TooLargeHeightExceptionTest() {
        Resolution resolution = new Resolution(
                this.width,
                this.height,
                this.modelWidth
        );

        resolution.getHeightByRatio(this.height * 2);
    }


    @Test(expected = NumberFormatException.class)
    public void TooSmallWidthExceptionTest() {
        Resolution resolution = new Resolution(
                this.width,
                this.height,
                this.modelWidth
        );

        resolution.getWidthByRatio(-10.0f);

    }

    @Test(expected = NumberFormatException.class)
    public void TooLargeWidthExceptionTest() {
        Resolution resolution = new Resolution(
                this.width,
                this.height,
                this.modelWidth
        );

        resolution.getWidthByRatio(this.width * 2);

    }
}