package com.scantamburlo.shoubu.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.Assert.*;

/**
 * Created by scanti.rulla at gmail.com on 30/06/16.
 */
public class ShoubuModelTest {

    @org.junit.Test
    public void shouldStartTheModel() throws Exception {
        ShoubuModel model = new ShoubuModel();

        /*
        model.addProperyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (ShoubuModel.PROP_ELAPSED_TIME.equals(e.getPropertyName())) {
                    System.out.println("ET: " + e.getNewValue());
                }
            }
        });
        */

        assertEquals(ShoubuModel.Status.READY, model.getStatus());

        try {
            model.startResumeOrPause();

            assertEquals(ShoubuModel.Status.RUNNING, model.getStatus());

            Thread.sleep(1100);

            model.startResumeOrPause();

            assertEquals(ShoubuModel.Status.PAUSED, model.getStatus());

            long elapsedNanoTime = model.getElapsedNanoTime();

            assertEquals("Same", elapsedNanoTime, model.getElapsedNanoTime());

            assertTrue("1.elapsed " + elapsedNanoTime, elapsedNanoTime >= 1000 * ShoubuModel.MILLIS_TO_NANOS);
            assertFalse("1. not elapsed " + elapsedNanoTime, elapsedNanoTime > 2 * 1000 * ShoubuModel.MILLIS_TO_NANOS);

            model.startResumeOrPause();

            assertEquals(ShoubuModel.Status.RUNNING, model.getStatus());

            Thread.sleep(2000);

            elapsedNanoTime = model.getElapsedNanoTime();
            assertTrue("2.elapsed " + elapsedNanoTime, elapsedNanoTime >= 3000 * ShoubuModel.MILLIS_TO_NANOS);

            model.reset();
            elapsedNanoTime = model.getElapsedNanoTime();
            assertEquals(ShoubuModel.Status.READY, model.getStatus());

            assertEquals("3.elapsed " + elapsedNanoTime, 0, elapsedNanoTime);
        } finally {
            model.reset();
        }
    }

    @org.junit.Test
    public void shouldChangePlayerPoints() throws Exception {
        ShoubuModel model = new ShoubuModel();

        ShoubuModel.Karateka leftKarateka = model.getLeftKarateka();
        ShoubuModel.Karateka rightKarateka = model.getRightKarateka();

        model.addPoint(leftKarateka, ShoubuModel.PointChange.IPPON);
        model.addPoint(rightKarateka, ShoubuModel.PointChange.WAZA_ARI);
        model.addPoint(rightKarateka, ShoubuModel.PointChange.MISTAKE);

        assertEquals("left", 3, leftKarateka.getPoints());
        assertEquals("right", 1, rightKarateka.getPoints());

        model.addPoint(rightKarateka, ShoubuModel.PointChange.MISTAKE);
        model.addPoint(rightKarateka, ShoubuModel.PointChange.MISTAKE);
        model.addPoint(rightKarateka, ShoubuModel.PointChange.MISTAKE);

        assertEquals("right", 0, rightKarateka.getPoints());

        model.addPoint(leftKarateka, ShoubuModel.PointChange.IPPON);

        ShoubuModel.Karateka winner = model.getWinner();

        assertNotNull("Winner is null", winner);

        assertSame(winner, leftKarateka);

        assertSame(ShoubuModel.Status.FINISHED, model.getStatus());
    }

    @org.junit.Test
    public void shouldChangePlayerPenalties() throws Exception {
        ShoubuModel model = new ShoubuModel();

        ShoubuModel.Karateka leftKarateka = model.getLeftKarateka();
        ShoubuModel.Karateka rightKarateka = model.getRightKarateka();

        model.addPenality(leftKarateka, ShoubuModel.Category.ONE, ShoubuModel.Penalty.KEIKOKU);

        assertTrue(leftKarateka.getCat1Penalties().contains(ShoubuModel.Penalty.KEIKOKU));
        assertFalse(leftKarateka.getCat2Penalties().contains(ShoubuModel.Penalty.KEIKOKU));

        assertNull(model.getWinner());

        model.addPenality(rightKarateka, ShoubuModel.Category.TWO, ShoubuModel.Penalty.HANSOKU);

        assertSame(leftKarateka, model.getWinner());

        assertSame(ShoubuModel.Status.FINISHED, model.getStatus());

    }
}
