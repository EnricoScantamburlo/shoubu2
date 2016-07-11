package com.scantamburlo.shoubu.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scantamburlo.shoubu.R;

/**
 * Created by scanti.rulla at gmail.com on 11/07/16.
 */
public class SettingsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        return view;

    }
}
