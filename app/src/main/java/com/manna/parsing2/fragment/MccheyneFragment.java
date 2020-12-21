package com.manna.parsing2.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.AsyncTask;
import android.widget.TextView;

import com.manna.parsing2.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class MccheyneFragment extends Fragment {
    private TextView mccheyneView_content;

    private String mccheyneString = "";
  //  private String[] titles=new String[10];
    private List<String> titles;

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_mccheyne, container, false);

        //맥체인 텍스트뷰
        mccheyneView_content = v.findViewById(R.id.textView2);

        MccheyneFragment.JsoupAsyncTask_mcchain jsoupAsyncTask_mcchain = new JsoupAsyncTask_mcchain();
        jsoupAsyncTask_mcchain.execute();

        return v;
    }

    @SuppressLint("StaticFieldLeak")
    private class JsoupAsyncTask_mcchain extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //맥체인
                Document doc2 = Jsoup.connect("http://www.bible4u.pe.kr/zbxe/read")
                        .get();
                Elements titleElements = doc2.select("div.content.xe_content tr[bgcolor=#FFFFFF][align=center][height=20] td");

                for(Element e : titleElements){
                    titles.add(e.text());
                }

                //맥체인 범위 스트링
                //mccheyneString = titles2.text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mccheyneView_content.setText(mccheyneString);
        }
    }
}