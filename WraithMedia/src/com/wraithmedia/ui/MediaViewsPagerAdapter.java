package com.wraithmedia.ui;

import java.util.ArrayList;
import java.util.List;

import com.viewpagerindicator.TitleProvider;
import com.wraithmedia.ui.views.SongsView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MediaViewsPagerAdapter extends FragmentPagerAdapter implements TitleProvider {

    private List<Fragment> mViewFragments;

    public MediaViewsPagerAdapter(FragmentManager fm) {
        super(fm);

        setupViewFragments();
    }

    @Override
    public Fragment getItem(int position) {
        return mViewFragments.get(position);
    }

    @Override
    public int getCount() {
        return mViewFragments.size();
    }

    private void setupViewFragments() {
        mViewFragments = new ArrayList<Fragment>();

        mViewFragments.add(new SongsView());
        mViewFragments.add(new SongsView());
    }

    public String getTitle(int position) {
        return "Songs";
    }
}
