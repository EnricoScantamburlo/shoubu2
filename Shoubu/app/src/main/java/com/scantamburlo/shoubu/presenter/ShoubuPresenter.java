package com.scantamburlo.shoubu.presenter;

import com.scantamburlo.shoubu.model.ShoubuModel;

/**
 * Created by scanti.rulla at gmail.com on 01/07/16.
 */
public class ShoubuPresenter {


    private final ShoubuModel model;

    private final ShoubuModel.Karateka leftKarateka;
    private final ShoubuModel.Karateka rightKarateka;

    public ShoubuPresenter(ShoubuModel model) {
        this.model = model;
        this.leftKarateka = model.getLeftKarateka();
        this.rightKarateka = model.getRightKarateka();
    }

    public void startResumeOrPause(){
        model.startResumeOrPause();
    }

    public String getRemainingTime() {
        long elapsedNanoTime = model.getElapsedNanoTime();
        long matchNanoLength = model.getMatchNanoLength();

        final long nanoRemainingTime = matchNanoLength - elapsedNanoTime;
        final long milliRemainingTime = nanoRemainingTime / ShoubuModel.MILLIS_TO_NANOS;

        long remainingTime = milliRemainingTime;

        long minutes = (long) Math.floor(remainingTime / (1000 * 60));

        remainingTime = remainingTime - minutes * 1000 * 60;

        long seconds = (long) Math.floor(remainingTime / 1000);

        remainingTime = remainingTime - seconds * 1000;

        long decs = (long) Math.floor(remainingTime / 100);


        return String.format("%02d:%02d:%02d", minutes, seconds, decs);
    }

    public void reset() {
        model.reset();
    }

    public void leftIppon() {
        model.addPoint(leftKarateka, ShoubuModel.PointChange.IPPON);
    }
}
