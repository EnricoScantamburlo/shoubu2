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

    public void rightIppon() {
        model.addPoint(rightKarateka, ShoubuModel.PointChange.IPPON);
    }

    public void leftWazaAri() {
        model.addPoint(leftKarateka, ShoubuModel.PointChange.WAZA_ARI);
    }

    public void rightWazaAri() {
        model.addPoint(rightKarateka, ShoubuModel.PointChange.WAZA_ARI);
    }

    public void leftYuko() {
        model.addPoint(leftKarateka, ShoubuModel.PointChange.YUKO);
    }

    public void rightYuko() {
        model.addPoint(rightKarateka, ShoubuModel.PointChange.YUKO);
    }

    public void leftMistake() {
        model.addPoint(leftKarateka, ShoubuModel.PointChange.MISTAKE);
    }

    public void rightMistake() {
        model.addPoint(rightKarateka, ShoubuModel.PointChange.MISTAKE);
    }

    public void setPenalty(ShoubuModel.Karateka karateka, ShoubuModel.Category cat, ShoubuModel.Penalty pen) {
        model.addPenality(karateka, cat, pen);
    }
}
