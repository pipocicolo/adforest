package com.scriptsbundle.adforest.messages;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Window;
import android.view.WindowManager;

import com.scriptsbundle.adforest.R;
import com.scriptsbundle.adforest.utills.AnalyticsTrackers;
import com.scriptsbundle.adforest.utills.SettingsMain;

public class Message extends AppCompatActivity {

    public TabLayout tabLayout;
    SettingsMain settingsMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        settingsMain = new SettingsMain(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(settingsMain.getMainColor()));
        }
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
            }
            return true;
        });

        toolbar.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        SendOffers sendOffers=new SendOffers();


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        if (getIntent().getBooleanExtra("receive", false))
            mViewPager.setCurrentItem(1);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        try {
            if (settingsMain.getAnalyticsShow() && !settingsMain.getAnalyticsId().equals(""))
                AnalyticsTrackers.getInstance().trackScreenView("Messages");
            super.onResume();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SendOffers();
                case 1:
                    return new RecievedOffers();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Sent Offers";
                case 1:
                    return "Received Offers";
            }
            return null;
        }
    }

    public interface sendOffersCallBack{
        void sendOffersBack();
    }
}
