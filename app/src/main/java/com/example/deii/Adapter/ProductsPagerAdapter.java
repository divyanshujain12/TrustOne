package com.example.deii.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.LinkedList;

/**
 * Created by deii on 12/6/2015.
 */
public class ProductsPagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 4;
    int topicID = 0;
    private String tabTitles[] = new String[]{"VIDEOS", "AUDIOS", "PDF", "PPT"};
    private LinkedList<String> tabTitle;
    private LinkedList<Fragment> totalFragments;

    public ProductsPagerAdapter(FragmentManager fm, int topicID, LinkedList<String> tabTitle, LinkedList<Fragment> totalFragments) {

        super(fm);
        this.topicID = topicID;
        this.tabTitle = tabTitle;
        this.totalFragments = totalFragments;
    }

    @Override
    public int getCount() {
        return tabTitle.size();
    }

    @Override
    public Fragment getItem(int position) {

        return totalFragments.get(position);
        /*switch (position) {
            case 0:
                return VideosFragment.newInstance(topicID, "");
            case 1:
                return AudioFragment.newInstance(topicID, "");
            case 2:
                return PDFFragment.newInstance(topicID, "");
            case 3:
                return PptFragment.newInstance(topicID, "");

        }*/

        //return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitle.get(position);
    }
}
