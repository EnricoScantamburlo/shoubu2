package com.scantamburlo.shoubu.model;

import android.graphics.Color;

/**
 * Created by scanti.rulla at gmail.com on 30/06/16.
 */
public class ShoubuOptions implements IShoubuOptions{

    private long defaultMatchLengthNano = ShoubuModel.MILLIS_TO_NANOS * 5 * 60 * 1000;
    private int leftColor = Color.WHITE;
    private int rightColor = Color.GREEN;

    public  ShoubuOptions(){

    }

    public long getDefaultMatchLengthNano(){
        return this.defaultMatchLengthNano;
    }

    public void setDefaultMatchLengthNano(long value) {
        this.defaultMatchLengthNano = value;
    }

    @Override
    public String getLeftName() {
        return "Aka";
    }

    @Override
    public String getRightName() {
        return "Ao";
    }


    public int getLeftColor() {
        return this.leftColor;
    }

    public void setLeftColor(int color) {
        this.leftColor = color;
    }

    public int getRightColor() {
        return this.rightColor;
    }

    public void setRightColor(int color) {
        this.rightColor = color;
    }
}
