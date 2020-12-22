package com.manna.parsing2.Mccheyne;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.manna.parsing2.Model.Mccheyne;
import com.manna.parsing2.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MccheyneFragment extends Fragment {

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_mccheyne, container, false);

        ViewPager vp= v.findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        vp.setAdapter(adapter);

        TabLayout tab=v.findViewById(R.id.tab);
        tab.setupWithViewPager(vp);

        return v;
    }
}