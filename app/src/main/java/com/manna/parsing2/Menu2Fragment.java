package com.manna.parsing2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.AsyncTask;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
public class Menu2Fragment extends Fragment {
    private JsoupAsyncTask_mcchain JsoupAsyncTask_mcchain;
    private TextView mcchainView_content;

    private String mcchainString = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_menu2, container, false);

        //맥체인 텍스트뷰
        mcchainView_content = (TextView) v.findViewById(R.id.textView2);

        JsoupAsyncTask_mcchain = new JsoupAsyncTask_mcchain();
        JsoupAsyncTask_mcchain.execute();

        return v;
    }

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
                Elements titles2 = doc2.select("div.content.xe_content tr[bgcolor=#FFFFFF][align=center][height=20]");

                //맥체인 범위 스트링
                mcchainString = titles2.text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mcchainView_content.setText(mcchainString);
        }
    }
}