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
    public final static String PROP_STATUS = "status"; // NOI18N
    public final static String PROP_ELAPSED_TIME = "elapsedTime"; // NOI18N
    public final static String PROP_POINTS = "points"; // NOI18N
    public final static String PROP_PENALITY = "penalty"; // NOI18N
    private static final long TIMER_PERIOD = 50L;
    private final int MAX_POINT_DIFFERENCE = 6;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final IShoubuOptions options;
    private final Timer timer = new Timer();
    private final Object TIME_LOCK = new Object();
    private final Karateka leftKarateka;
    private final Karateka rightKarateka;
    private Status status = Status.READY;
    private UpdateTimerTask task;
    private volatile long elapsedNanoTime = 0;
    private volatile long startNanoTime;
    private long matchNanoLength;
    private Karateka winner;

    public ShoubuModel(IShoubuOptions options) {
        this.options = options;
        matchNanoLength = options.getDefaultMatchLengthNano();

        leftKarateka = new Karateka(options.getLeftName());
        rightKarateka = new Karateka(options.getRightName());
    }

    public ShoubuModel() {
        this(new ShoubuOptions());
    }

    public void startResumeOrPause() {
        if (status == Status.READY || status == Status.FINISHED) {
            // I start the game
            resetStatus();
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
            storeTime();

            this.status = Status.PAUSED;
            support.firePropertyChange(PROP_STATUS, Status.RUNNING, Status.PAUSED);

        }
    }

    public void reset() {
        Object old = this.status;
        this.status = Status.READY;
        resetStatus();
        support.firePropertyChange(PROP_STATUS, old, Status.READY);
    }

    private void resetStatus() {
        if (task != null) {
            task.cancel();
            task = null;
        }

        synchronized (TIME_LOCK) {
            this.elapsedNanoTime = 0;
            this.startNanoTime = System.nanoTime();
            matchNanoLength = options.getDefaultMatchLengthNano();
        }

        winner = null;
        leftKarateka.reset();
        rightKarateka.reset();
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

    public void setMatchNanoLength(long matchNanoLength) {
        this.matchNanoLength = matchNanoLength;
    }

    public void finish() {
        storeTime();
        // finds the winner
        declareWinner();

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

    public void addPoint(Karateka player, PointChange point) {
        // TODO Stop the timer
        player.addPoint(point);

        // I reset the winner because it could
        // have been resetting the winning point

        boolean fireStatusReset = false;
        if (point == PointChange.MISTAKE && status == Status.FINISHED && winner != null) {
            //  TODO I also have to reset the status of the Model
            if (!hasWinner(true)) {
                winner = null;
                status = Status.PAUSED;
                fireStatusReset = true;
            }
        }

        // If the status was reset no need to check
        // for a winner
        if (!fireStatusReset && hasWinner()) {
            finish();
        }

        support.firePropertyChange(PROP_POINTS, 0, point.getScore());
        if (fireStatusReset) {
            support.firePropertyChange(PROP_STATUS, Status.FINISHED, Status.PAUSED);
        }
    }

    public void addPenality(Karateka player, Category cat, Penalty pen) {
        // TODO Stop the timer

        player.addPenalty(cat, pen);
        if (hasWinner()) {
            declareWinner();
            finish();
        }

        support.firePropertyChange(PROP_PENALITY, null, null);
    }

    public void removePenality(Karateka player, Category cat, Penalty pen) {
        player.removePenalty(cat, pen);
        support.firePropertyChange(PROP_PENALITY, null, null);
    }

    public void addProperyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    private void storeTime() {
        if (task != null) {
            boolean cancelled = task.cancel();
            long now = System.nanoTime();

            if (!cancelled) {
                synchronized (TIME_LOCK) {
                    elapsedNanoTime += (now - startNanoTime);
                }
            }
            task = null;
        }
    }

    /**
     * @return sets the winner
     */
    private void declareWinner() {

        if (Math.abs(leftKarateka.getPoints() - rightKarateka.getPoints()) >= MAX_POINT_DIFFERENCE) {
            winner = leftKarateka.getPoints() > rightKarateka.getPoints() ? leftKarateka : rightKarateka;
        } else if (leftKarateka.getCat1Penalties().contains(Penalty.HANSOKU) || leftKarateka.getCat2Penalties().contains(Penalty.HANSOKU)) {
            winner = rightKarateka;
        } else if (rightKarateka.getCat1Penalties().contains(Penalty.HANSOKU) || rightKarateka.getCat2Penalties().contains(Penalty.HANSOKU)) {
            winner = leftKarateka;
        } else if (status == Status.FINISHED) {
            if (leftKarateka.getPoints() != rightKarateka.getPoints()) {
                winner = leftKarateka.getPoints() > rightKarateka.getPoints() ? leftKarateka : rightKarateka;
            }
        }
    }

    private boolean hasWinner() {
        return hasWinner(false);
    }

    private boolean hasWinner(boolean ignoreStatus) {
        if (Math.abs(leftKarateka.getPoints() - rightKarateka.getPoints()) >= MAX_POINT_DIFFERENCE) {
            return true;
        }

        if (leftKarateka.getCat1Penalties().contains(Penalty.HANSOKU) || leftKarateka.getCat2Penalties().contains(Penalty.HANSOKU)) {
            return true;
        }

        if (rightKarateka.getCat1Penalties().contains(Penalty.HANSOKU) || rightKarateka.getCat2Penalties().contains(Penalty.HANSOKU)) {
            return true;
        }

        if (!ignoreStatus && status == Status.FINISHED) {
            return true;
        }

        return false;
    }

    public enum Status {READY, RUNNING, PAUSED, FINISHED}


    // Private stuff


    public enum PointChange {
        IPPON(3), WAZA_ARI(2), YUKO(1), MISTAKE(-1);


        private final int score;

        PointChange(int i) {
            this.score = i;
        }

        public int getScore() {
            return score;
        }
    }

    public enum Category {ONE, TWO}

    public enum Penalty {
        CHUKOKU(1), KEIKOKU(2), HANSOKU_CHUI(3), HANSOKU(4);


        private final int level;

        Penalty(int i) {
            this.level = i;
        }

        public int getLevel() {
            return level;
        }
    }

    public class Karateka {

        private final EnumSet<Penalty> cat1Penalties = EnumSet.noneOf(Penalty.class);
        private final EnumSet<Penalty> cat2Penalties = EnumSet.noneOf(Penalty.class);
        private final String name;


        private int points = 0;

        public Karateka(String name) {
            this.name = name;
        }


        public int getPoints() {
            return points;
        }

        public Set<Penalty> getCat1Penalties() {
            return EnumSet.copyOf(cat1Penalties);

        }

        public Set<Penalty> getCat2Penalties() {
            return EnumSet.copyOf(cat2Penalties);
        }

        private void addPoint(PointChange point) {
            points += point.getScore();
            if (points < 0) {
                points = 0;
            }
        }

        private void addPenalty(Category cat, Penalty pen) {
            if (cat == Category.ONE) {
                cat1Penalties.add(pen);
            } else if (cat == Category.TWO) {
                cat2Penalties.add(pen);
            }
        }

        private void removePenalty(Category cat, Penalty pen) {
            if (cat == Category.ONE) {
                cat1Penalties.remove(pen);
            } else if (cat == Category.TWO) {
                cat2Penalties.remove(pen);
            }
        }

        private void reset() {
            points = 0;
            cat1Penalties.clear();
            cat2Penalties.clear();
        }

        public String getName() {
            return this.name;
        }
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
