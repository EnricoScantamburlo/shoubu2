package com.scantamburlo.shoubu.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.EnumSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by scanti.rulla at gmail.com  on 30/06/16.
 */
public class ShoubuModel {
    public static final long MILLIS_TO_NANOS = 1000000L;
    public final static String PROP_STATUS = "status";
    public final static String PROP_ELAPSED_TIME = "elapsedTime";
    public final static String PROP_POINTS = "points";
    public final static String PROP_PENALITY = "penality";


    public enum Status {READY, RUNNING, PAUSED, FINISHED}
    public enum PointChange {IPPON(3), WAZA_ARI(2), YOKO(1), MISTAKE(-1);


        private final int score;

        PointChange(int i) {
            this.score = i;
        }

        public int getScore() {
            return score;
        }
    }

    public enum Category {ONE, TWO}
    public enum Penalty { CHUKOKU(1),KEIKOKU(2),HANSOKU_CHUI(3), HANSOKU(4);


        private final int level;

        Penalty(int i) {
            this.level = i;
        }
    }

    private final int MAX_POINT_DIFFERENCE = 6;
    private static final long TIMER_PERIOD = 100L;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);



    private Status status = Status.READY;

    private final Timer timer = new Timer();
    private UpdateTimerTask task;

    private final Object TIME_LOCK = new Object();
    private volatile long elapsedNanoTime = 0;
    private volatile long startNanoTime;

    private long matchNanoLength;

    private Karateka leftKarateka = new Karateka();
    private Karateka rightKarateka = new Karateka();
    private Karateka winner;

    public ShoubuModel(IShoubuOptions options) {
        matchNanoLength = options.getDefaultMatchLengthNano();
    }

    public ShoubuModel() {
        this(new ShoubuOptions());
    }

    public void setMatchNanoLength(long matchNanoLength) {
        this.matchNanoLength = matchNanoLength;
    }

    public void startResumeOrPause() {
        if (status == Status.READY) {
            // I start the game
            elapsedNanoTime = 0;
            startNanoTime = System.nanoTime();
            task = new UpdateTimerTask();
            timer.scheduleAtFixedRate(task, 0, TIMER_PERIOD);

            this.status = Status.RUNNING;
            support.firePropertyChange(PROP_STATUS, Status.READY, Status.RUNNING);
        } else if (status == Status.PAUSED) {
            // I resume the game
            startNanoTime = System.nanoTime();

            task = new UpdateTimerTask();
            timer.scheduleAtFixedRate(task, 0, TIMER_PERIOD);

            this.status = Status.RUNNING;
            support.firePropertyChange(PROP_STATUS, Status.PAUSED, Status.RUNNING);
        } else if (status == Status.RUNNING) {
            // I pause the game
            boolean cancelled = task.cancel();
            long now = System.nanoTime();

            if (!cancelled) {
                synchronized (TIME_LOCK) {
                    elapsedNanoTime += (now - startNanoTime);
                }
            }

            this.status = Status.PAUSED;
            support.firePropertyChange(PROP_STATUS, Status.RUNNING, Status.PAUSED);

        }
    }

    public void reset() {
        if (task != null) {
            task.cancel();
        }

        synchronized (TIME_LOCK) {
            this.elapsedNanoTime = 0;
            this.startNanoTime = 0;
        }

        leftKarateka.reset();
        rightKarateka.reset();

        Object old = this.status;
        this.status = Status.READY;
        support.firePropertyChange(PROP_STATUS, old, Status.READY);
    }

    public Status getStatus() {
        return status;
    }

    public long getElapsedNanoTime() {
        return elapsedNanoTime;
    }

    public long getMatchNanoLength() {
        return matchNanoLength;
    }

    public void finish(){
        if(task != null){
            task.cancel();
        }

        // finds the winner
        hasWinner();

        this.status = Status.FINISHED;
        support.firePropertyChange(PROP_STATUS, Status.RUNNING, Status.FINISHED);
    }


    public Karateka getLeftKarateka() {
        return leftKarateka;
    }

    public Karateka getRightKarateka() {
        return rightKarateka;
    }

    public Karateka getWinner() {
        return winner;
    }

    public void addPoint(Karateka player, PointChange point){
        // TODO Stop the timer
        player.addPoint(point);

        // I reset the winner because it could
        // have been resetting the winning point
        //  TODO I also have to reset the status of the Model
        winner = null;

        if(hasWinner()){
            finish();
        }

        support.firePropertyChange(PROP_POINTS, 0, point.getScore());
    }

    public void addPenality(Karateka player, Category cat, Penalty pen){
        // TODO Stop the timer

        player.addPenality(cat, pen);
        if(pen == Penalty.HANSOKU){
            finish();
        }

        support.firePropertyChange(PROP_PENALITY, null, null);
    }

    public void removePenality(Karateka player, Category cat, Penalty pen){
        player.removePenality(cat, pen);
        support.firePropertyChange(PROP_PENALITY, null, null);
    }

    public void addProperyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public class Karateka {

        private final EnumSet<Penalty> cat1Penalties =  EnumSet.noneOf(Penalty.class);
        private final EnumSet<Penalty> cat2Penalties = EnumSet.noneOf(Penalty.class);

        private int points = 0;


        public int getPoints() {
            return points;
        }

        public Set<Penalty> getCat1Penalties() {
            return EnumSet.copyOf(cat1Penalties);

        }

        public Set<Penalty> getCat2Penalties() {
            return EnumSet.copyOf(cat2Penalties);
        }

        private void addPoint(PointChange point){
            points += point.getScore();
            if(points < 0){
                points = 0;
            }
        }

        private void addPenality(Category cat, Penalty pen){
            if(cat == Category.ONE){
                cat1Penalties.add(pen);
            } else if(cat == Category.TWO){
                cat2Penalties.add(pen);
            }
        }

        private void removePenality(Category cat, Penalty pen){
            if(cat == Category.ONE){
                cat1Penalties.remove(pen);
            } else if(cat == Category.TWO){
                cat2Penalties.remove(pen);
            }
        }

        private void reset(){
            points = 0;
            cat1Penalties.clear();
            cat2Penalties.clear();
        }
    }


    // Private stuff

    /**
     *
     * @return true is a winner is found
     */
    private boolean hasWinner(){
        if(winner != null){
            return true;
        }

        if( Math.abs(leftKarateka.getPoints() - rightKarateka.getPoints()) >= MAX_POINT_DIFFERENCE){
            winner = leftKarateka.getPoints() > rightKarateka.getPoints() ? leftKarateka : rightKarateka;
            return true;
        }

        if(status == Status.FINISHED){
            winner = leftKarateka.getPoints() > rightKarateka.getPoints() ? leftKarateka : rightKarateka;
            return true;
        }

        if(leftKarateka.getCat1Penalties().contains(Penalty.HANSOKU) || leftKarateka.getCat2Penalties().contains(Penalty.HANSOKU)){
            winner = rightKarateka;
            return true;
        }

        if(rightKarateka.getCat1Penalties().contains(Penalty.HANSOKU) ||rightKarateka.getCat2Penalties().contains(Penalty.HANSOKU)){
            winner =  leftKarateka;
            return true;
        }

        // TODO check penalties
        return false;
    }

    private class UpdateTimerTask extends TimerTask {

        @Override
        public void run() {
            long now = System.nanoTime();
            synchronized (TIME_LOCK) {
                elapsedNanoTime += (now - startNanoTime);
                startNanoTime = now;
            }

            // TODO check for ATOSHIBARAKU

            if (elapsedNanoTime >= matchNanoLength) {
                finish();
            }

            support.firePropertyChange(PROP_ELAPSED_TIME, 0, elapsedNanoTime);

            // Does not work on unit tests
            // Method i in android.util.Log not mocked. See http://g.co/androidstudio/not-mocked for details.
            //Log.i(ShoubuModel.class.getName(), "Elapsed " + elapsedNanoTime);
        }
    }

}
