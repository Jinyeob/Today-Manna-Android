package com.manna.parsing2.Mccheyne;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> items;
    private ArrayList<String> titles=new ArrayList<String>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        items = new ArrayList<Fragment>();
        items.add(new FragmentMc1());
        items.add(new FragmentMc2());
        items.add(new FragmentMc3());
        items.add(new FragmentMc4());

        titles.add("1");
        titles.add("2");
        titles.add("3");
        titles.add("4");
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
