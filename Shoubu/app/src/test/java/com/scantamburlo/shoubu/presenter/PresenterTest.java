package com.scantamburlo.shoubu.presenter;

import com.scantamburlo.shoubu.model.ShoubuModel;
import static org.junit.Assert.*;

/**
 * Created by scanti.rulla at gmail.com on 01/07/16.
 */
public class PresenterTest {

    @org.junit.Test
    public void shouldCalculateTheRightString(){
        ShoubuModel model = new ShoubuModel();
        ShoubuPresenter presenter = new ShoubuPresenter(model);

        {
            model.setMatchNanoLength(toNano(6, 0, 0));
            String remainingTime = presenter.getRemainingTime();
            assertEquals("06:00:00", remainingTime);
        }

        {
            model.setMatchNanoLength(toNano(11, 9, 0));
            String remainingTime = presenter.getRemainingTime();
            assertEquals("11:09:00", remainingTime);
        }

        {
            model.setMatchNanoLength(toNano(3, 4, 6));
            String remainingTime = presenter.getRemainingTime();
            assertEquals("03:04:06", remainingTime);
        }
    }

    private long toNano(int min, int secs, int dec){
        long acc = 0;

        //mins
        acc += min;

        //secs
        acc *= 60;
        acc += secs;

        // dec
        acc *= 10;
        acc += dec;

        //milli
        acc *= 100;

        //nano
        acc *= ShoubuModel.MILLIS_TO_NANOS;

        return acc;
    }

}
