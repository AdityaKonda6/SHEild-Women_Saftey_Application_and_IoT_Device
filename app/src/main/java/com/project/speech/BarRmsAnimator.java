package com.project.speech;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.project.speech.SpeechBar;

import java.util.Random;

public class BarRmsAnimator extends AppCompatActivity implements BarParamsAnimator  {

    private static final float QUIT_RMSDB_MAX = 2f;
    private static final float MEDIUM_RMSDB_MAX = 5.5f;

    private static final long BAR_ANIMATION_UP_DURATION = 130;
    private static final long BAR_ANIMATION_DOWN_DURATION = 500;
    final private SpeechBar bar;
    private float fromHeightPart;
    private float toHeightPart;
    private long startTimestamp;
    private boolean isPlaying;
    private boolean isUpAnimation;

    public BarRmsAnimator(SpeechBar bar) {
        this.bar = bar;
    }

    @Override
    public void start() {
        isPlaying = true;
    }

    @Override
    public void stop() {
        isPlaying = false;
    }

    @Override
    public void animate() {
        if (isPlaying) {
            update();
        }
    }

    public void onRmsChanged(float rmsdB) {
        float newHeightPart;

        if (rmsdB < QUIT_RMSDB_MAX) {
            newHeightPart = 0.2f;
        } else if (rmsdB >= QUIT_RMSDB_MAX && rmsdB <= MEDIUM_RMSDB_MAX) {
            newHeightPart = 0.3f + new Random().nextFloat();
            if (newHeightPart > 0.6f) newHeightPart = 0.6f;
        } else {
            newHeightPart = 0.7f + new Random().nextFloat();
            if (newHeightPart > 1f) newHeightPart = 1f;

        }

        if (newHeightIsSmallerCurrent(newHeightPart)) {
            return;
        }

        fromHeightPart = (float) bar.getHeight() / bar.getMaxHeight();
        toHeightPart = newHeightPart;

        startTimestamp = System.currentTimeMillis();
        isUpAnimation = true;
        isPlaying = true;
    }

    private boolean newHeightIsSmallerCurrent(float newHeightPart) {
        return (float) bar.getHeight() / bar.getMaxHeight() > newHeightPart;
    }

    private void update() {

        long currTimestamp = System.currentTimeMillis();
        long delta = currTimestamp - startTimestamp;

        if (isUpAnimation) {
            animateUp(delta);
        } else {
            animateDown(delta);
        }
    }

    private void animateUp(long delta) {
        boolean finished = false;
        int minHeight = (int) (fromHeightPart * bar.getMaxHeight());
        int toHeight = (int) (bar.getMaxHeight() * toHeightPart);

        float timePart = (float) delta / BAR_ANIMATION_UP_DURATION;

        AccelerateInterpolator interpolator = new AccelerateInterpolator();
        int height = minHeight + (int) (interpolator.getInterpolation(timePart) * (toHeight - minHeight));

        if (height < bar.getHeight()) {
            return;
        }

        if (height >= toHeight) {
            height = toHeight;
            finished = true;
        }

        bar.setHeight(height);
        bar.update();

        if (finished) {
            isUpAnimation = false;
            startTimestamp = System.currentTimeMillis();
        }
    }

    private void animateDown(long delta) {
        int minHeight = bar.getRadius() * 2;
        int fromHeight = (int) (bar.getMaxHeight() * toHeightPart);

        float timePart = (float) delta / BAR_ANIMATION_DOWN_DURATION;

        DecelerateInterpolator interpolator = new DecelerateInterpolator();
        int height = minHeight + (int) ((1f - interpolator.getInterpolation(timePart)) * (fromHeight - minHeight));

        if (height > bar.getHeight()) {
            return;
        }

        if (height <= minHeight) {
            finish();
            return;
        }

        bar.setHeight(height);
        bar.update();
    }

    public void finish() {
        bar.setHeight(bar.getRadius() * 2);
        bar.update();
        isPlaying = false;
    }
}
