package com.killain.organizer.packages.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.killain.organizer.packages.fragments.GeneratedFragment;
import com.killain.organizer.packages.fragments.TasksFragment;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return TasksFragment.newInstance();
        } else if (position > 0) {
            return GeneratedFragment.newInstance("Generated");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

