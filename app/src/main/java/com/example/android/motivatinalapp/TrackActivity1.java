package com.example.android.motivatinalapp;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TrackActivity1 extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */


    /**
     * The {@link ViewPager} that will host the section contents.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track1);

        TabLayout tablayout =(TabLayout) findViewById(R.id.tabs);
        AppBarLayout appBarLayout =(AppBarLayout)findViewById(R.id.appbar);
        ViewPager mViewPager =(ViewPager)findViewById(R.id.container);
        SectionsPagerAdapter viewPagerAdapter =new SectionsPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.AddFragment(new Frag1(),"Fragment");
        viewPagerAdapter.AddFragment(new Frag2(),"Fragment");




        mViewPager.setAdapter(viewPagerAdapter);
        tablayout.setupWithViewPager(mViewPager);



    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList= new ArrayList<>();
        private final List<String> FragmentListTitles =new ArrayList<>();



        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
           return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return FragmentListTitles.size();
        }
        @Override
        public CharSequence getPageTitle(int position)
        {
            return FragmentListTitles.get(position);
        }
        public void AddFragment(Fragment fragment, String Title)
        {
            fragmentList.add(fragment);
            FragmentListTitles.add(Title);
        }

    }

}
