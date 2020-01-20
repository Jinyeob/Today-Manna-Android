package com.manna.parsing2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MccheyneActivity extends AppCompatActivity {
    private JsoupAsyncTask_mcchain JsoupAsyncTask_mcchain;
    private TextView mcchainView_content;
    private String mcchainString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mccheyne);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("오늘의 맥체인");
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        //맥체인 텍스트뷰
        mcchainView_content = (TextView) findViewById(R.id.textView_mc);
        JsoupAsyncTask_mcchain = new JsoupAsyncTask_mcchain();
        JsoupAsyncTask_mcchain.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
