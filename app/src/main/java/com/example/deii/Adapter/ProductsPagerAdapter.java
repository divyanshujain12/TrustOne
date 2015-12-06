package com.example.deii.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.deii.ProductFragments.AudioFragment;
import com.example.deii.ProductFragments.PDFFragment;
import com.example.deii.ProductFragments.PptFragment;
import com.example.deii.ProductFragments.VideosFragment;

/**
 * Created by deii on 12/6/2015.
 */
public class ProductsPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    int topicID = 0;
    private String tabTitles[] = new String[]{"VIDEOS", "AUDIOS", "PDF","PPT"};

    public ProductsPagerAdapter(FragmentManager fm, int topicID) {

        super(fm);
        this.topicID = topicID;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return VideosFragment.newInstance(topicID, "");
            case 1:
                return AudioFragment.newInstance(topicID, "");
            case 2:
                return PDFFragment.newInstance(topicID, "");
            case 3:
                return PptFragment.newInstance(topicID, "");

        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
