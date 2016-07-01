package com.scantamburlo.shoubu.model;

/**
 * Created by scanti.rulla at gmail.com on 30/06/16.
 */
public class ShoubuOptions implements IShoubuOptions{

    public  ShoubuOptions(){

    }



    public long getDefaultMatchLengthNano(){
        return ShoubuModel.MILLIS_TO_NANOS * 5 * 60 * 1000;
    }
}
