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

    static List<List<Mccheyne>> AllList = new ArrayList<>();

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_mccheyne, container, false);

        //맥체인 텍스트뷰

        ViewPager vp= v.findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        vp.setAdapter(adapter);

        TabLayout tab=v.findViewById(R.id.tab);
        tab.setupWithViewPager(vp);

//        int length=AllList.get(0).size();
//        for(int i=0;i<length;i++){
//            AllList.get(0).get(i).getText()
//        }

        MccheyneFragment.JsoupAsyncTask_mcchain1 jsoupAsyncTask_mcchain1 = new JsoupAsyncTask_mcchain1();
        MccheyneFragment.JsoupAsyncTask_mcchain2 jsoupAsyncTask_mcchain2 = new JsoupAsyncTask_mcchain2();
        MccheyneFragment.JsoupAsyncTask_mcchain3 jsoupAsyncTask_mcchain3 = new JsoupAsyncTask_mcchain3();
        MccheyneFragment.JsoupAsyncTask_mcchain4 jsoupAsyncTask_mcchain4 = new JsoupAsyncTask_mcchain4();

        jsoupAsyncTask_mcchain1.execute();
        jsoupAsyncTask_mcchain2.execute();
        jsoupAsyncTask_mcchain3.execute();
        jsoupAsyncTask_mcchain4.execute();


        return v;
    }

    public static List<Mccheyne>  GetData(Document doc){
        Elements textElements = doc.select("tr[class=li_f_size] td");

        List<Mccheyne> MList=new ArrayList<>();

        int i=0;
        Mccheyne content=new Mccheyne();
        for(Element e : textElements){
            if(i==0){
                content.setTitle(e.text());
                i++;
            }
            else if(i==1){
                content.setPoint(e.text());
                i++;
            }
            else if(i==2){
                content.setText(e.text());
                i=0;
                MList.add(content);
                content=new Mccheyne();
            }
        }
        for(Object node : MList){
            System.out.println(node);
        }

        AllList.add(MList);

        return MList;
    }

    @SuppressLint("StaticFieldLeak")
    private class JsoupAsyncTask_mcchain1 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document doc = Jsoup.connect("http://www.bible4u.pe.kr/zbxe/read")
                        .get();
                GetData(doc);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
    @SuppressLint("StaticFieldLeak")
    private class JsoupAsyncTask_mcchain2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("http://bible4u.pe.kr/zbxe/?mid=open_read&ver=korean_krv&b_num=2")
                        .get();

                GetData(doc);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
    @SuppressLint("StaticFieldLeak")
    private class JsoupAsyncTask_mcchain3 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("http://bible4u.pe.kr/zbxe/?mid=open_read&ver=korean_krv&b_num=3")
                        .get();

                GetData(doc);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
    @SuppressLint("StaticFieldLeak")
    private class JsoupAsyncTask_mcchain4 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("http://bible4u.pe.kr/zbxe/?mid=open_read&ver=korean_krv&b_num=4")
                        .get();

                GetData(doc);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
}

//                Elements titleElements = doc2.select("tr[bgcolor=#FFFFFF][align=center][height=20] td");
//                div.content.xe_content

        /*        for(Element e : titleElements){
                        titles.add(e.text());
                        mccheyneString+=e.text()+"\n";
                        }*/