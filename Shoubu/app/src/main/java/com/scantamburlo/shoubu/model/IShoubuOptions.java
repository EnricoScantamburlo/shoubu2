package com.scantamburlo.shoubu.model;

/**
 * Created by scanti.rulla at gmail.com on 30/06/16.
 */
public interface IShoubuOptions {

    long getDefaultMatchLengthNano();

    void setDefaultMatchLengthNano(long time);

    String getLeftName();

    String getRightName();

    int getLeftColor();

    int getRightColor();

}
