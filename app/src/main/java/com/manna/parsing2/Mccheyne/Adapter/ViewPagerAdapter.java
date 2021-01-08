package com.manna.parsing2.Mccheyne.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.manna.parsing2.Mccheyne.Fragment.FragmentMc1;
import com.manna.parsing2.Mccheyne.Fragment.FragmentMc2;
import com.manna.parsing2.Mccheyne.Fragment.FragmentMc3;
import com.manna.parsing2.Mccheyne.Fragment.FragmentMc4;

import java.util.ArrayList;
/***
 * Create by Jinyeob
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> items;
    private final ArrayList<String> titles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        items = new ArrayList<>();
        items.add(new FragmentMc1());
        items.add(new FragmentMc2());
        items.add(new FragmentMc3());
        items.add(new FragmentMc4());

        titles.add("⚽️");
        titles.add("🏀");
        titles.add("🏈");
        titles.add("⚾️");
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
