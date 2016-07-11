package com.scantamburlo.shoubu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.scantamburlo.shoubu.ui.MainFragment;
import com.scantamburlo.shoubu.ui.SettingsFragment;

public class ScoreBoardActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private CustomPagerAdapter mCustomPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_score_board);

        this.mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this);

        this.mViewPager = (ViewPager) findViewById(R.id.pager);
        this.mViewPager.setAdapter(mCustomPagerAdapter);

    }

    private static class CustomPagerAdapter extends FragmentStatePagerAdapter {

        protected Context mContext;

        public CustomPagerAdapter(FragmentManager fm, ScoreBoardActivity context) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
                    return new SettingsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
