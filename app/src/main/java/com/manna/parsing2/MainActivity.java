package com.manna.parsing2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private String htmlPageUrl = "https://community.jbch.org/confirm.php";
    private JsoupAsyncTask1 jsoupAsyncTask1;
    private JsoupAsyncTask1_webview JsoupAsyncTask1_webview;
    private JsoupAsyncTask2 jsoupAsyncTask2;
    private JsoupAsyncTask3 jsoupAsyncTask3;

    private Map<String, String> loginCookie;
    private Document doc;
    private Element getURL;

    private TextView textviewHtmlDocument;

    private TextView mchain1;
    private TextView mchain2;

    private String viewPageUrl = "";
    private WebView webView;

    private String htmlContentInStringFormat = "";
    private String htmlContentInStringFormat2 = "";


    String ID = "";
    String PASSWD = "";

    private TextView info_text;
    private String thumbURL;
    private String today_bible = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //로그인 정보
        Intent loginIntent = getIntent();
        ID = loginIntent.getStringExtra("id");
        PASSWD = loginIntent.getStringExtra("passwd");

        //툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.myAppName);
        setSupportActionBar(toolbar);

        //일요일일때
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        String weekDay = weekdayFormat.format(currentTime);
        if (weekDay.equals("일")) {
            Toast.makeText(MainActivity.this, "일요일은 지원하지 않습니다.", Toast.LENGTH_LONG).show();
            Intent intent__ = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent__);
            finish();
        }

        //말씀용 텍스트뷰
        info_text = (TextView) findViewById(R.id.textView5);
        info_text.setMovementMethod(new ScrollingMovementMethod()); //스크롤 가능한 텍스트뷰로 만들기

        //날짜 텍스트뷰
        textviewHtmlDocument = (TextView) findViewById(R.id.textView);
        textviewHtmlDocument.setMovementMethod(new ScrollingMovementMethod()); //스크롤 가능한 텍스트뷰로 만들기

        //맥체인 텍스트뷰
        mchain1 = (TextView) findViewById(R.id.textView_mc_info);
        mchain2 = (TextView) findViewById(R.id.textView_mc);

        //만나범위 웹뷰
        webView = (WebView) findViewById(R.id.webView);

        //파싱 시작
        jsoupAsyncTask1 = new JsoupAsyncTask1();
        jsoupAsyncTask1.execute();

        JsoupAsyncTask1_webview = new JsoupAsyncTask1_webview();
        JsoupAsyncTask1_webview.execute();

        jsoupAsyncTask2 = new JsoupAsyncTask2();
        jsoupAsyncTask2.execute();

        jsoupAsyncTask3 = new JsoupAsyncTask3();
        jsoupAsyncTask3.execute();
    }

    private class JsoupAsyncTask1 extends AsyncTask<Void, Void, Void> {
        //진행바
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //프로세스 다이얼로그
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("불러오는 중...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //깨사모
                Connection.Response res = Jsoup.connect(htmlPageUrl)
                        .data("user_id", ID)
                        .data("saveid", "1")
                        .data("passwd", PASSWD)
                        .data("mode", "")
                        .data("go", "yes")
                        .data("url", "http://community.jbch.org/")
                        .data("LoginButton", "LoginButton")
                        .timeout(5000)
                        .maxBodySize(0)
                        .method(Connection.Method.POST)
                        .execute();

                loginCookie = res.cookies();
                doc = Jsoup.connect("https://community.jbch.org/")
                        .cookies(loginCookie)
                        .get();

                Elements titles = doc.select("div.conbox.active div.condate.font-daum");

                System.out.println("-----------------------------------------------------------------------------------------------------------");

                //만나 날짜 스트링
                for (Element e : titles) {
                    System.out.println(e.text());
                    htmlContentInStringFormat = e.text().trim() + "\n";
                }

                //로그인 실패 조건문
                if (htmlContentInStringFormat.equals("")) {
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "로그인 실패, 다시 로그인 해주세요.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            SaveSharedPreference.clearUser(MainActivity.this);
                            startActivity(intent);
                            finish();
                        }
                    }, 0);
                }

                System.out.println("-------------------------------------------------------------------------------------------------------");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //만나 날짜
            textviewHtmlDocument.setText(htmlContentInStringFormat);

            progressDialog.dismiss();
        }
    }

    private class JsoupAsyncTask1_webview extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Elements contentATags = doc.select("div.conbox.active img");

            //만나 범위 웹뷰용 URL
            viewPageUrl = contentATags.attr("abs:src");

            //로그인 실패 조건문
            if (viewPageUrl.equals("")) {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "로그인 실패, 다시 로그인 해주세요.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        SaveSharedPreference.clearUser(MainActivity.this);
                        startActivity(intent);
                        finish();
                    }
                }, 0);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //만나범위
            webView.loadUrl(viewPageUrl);
        }
    }

    private class JsoupAsyncTask2 extends AsyncTask<Void, Void, Void> {
        //진행바
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //프로세스 다이얼로그
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("불러오는 중...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //실제 말씀 url
                getURL = doc.select("div.conbox.active div.content").first();
                thumbURL = getURL
                        .attr("onclick")
                        .replace("getUrl('", "")
                        .replace("', '')", "");

                String target = "?uid=";
                int target_num = thumbURL.indexOf(target);
                String result;
                result = thumbURL.substring(target_num + 5, thumbURL.indexOf("&") + 1);

                //실제 말씀 구절 url로 접속
                Document doc_bible = Jsoup.connect("http://community.jbch.org/meditation/board/process.php")
                        .header("Origin", "http://community.jbch.org")
                        .header("Referer", "http://community.jbch.org/")
                        .data("mode", "load_post")
                        .data("post_uid", result)
                        .cookies(loginCookie)
                        .post();

                Elements content_bible = doc_bible.select("div.contentbox.fr-view p");

                System.out.println("-----------------------------------------------------------------------------------------------------------");

                //만나 실제 말씀
                for (Element e2 : content_bible) {
                    today_bible += e2.text().trim() + "\n";
                }
                System.out.println("-------------------------------------------------------------------------------------------------------");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //말씀
            info_text.setText(today_bible);

            progressDialog.dismiss();
        }
    }

    private class JsoupAsyncTask3 extends AsyncTask<Void, Void, Void> {
        //진행바
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //프로세스 다이얼로그
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("불러오는 중...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //맥체인
                Document doc2 = Jsoup.connect("http://www.bible4u.pe.kr/zbxe/read")
                        .get();
                Elements titles2 = doc2.select("div.content.xe_content tr[bgcolor=#FFFFFF][align=center][height=20]");

                System.out.println("-----------------------------------------------------------------------------------------------------------");
                //맥체인 범위 스트링
                htmlContentInStringFormat2 = titles2.text();
                System.out.println("맥체인범위: " + htmlContentInStringFormat2);

                System.out.println("-------------------------------------------------------------------------------------------------------");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //맥체인
            mchain1.setText("오늘의 맥체인");
            mchain2.setText(htmlContentInStringFormat2);

            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                Intent intent1 = new Intent(getApplicationContext(), app_info.class);
                startActivity(intent1);
                return true;
            case R.id.re_login:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                SaveSharedPreference.clearUser(MainActivity.this);
                startActivity(intent);
                finish();
                return true;
            case R.id.refresh:
                Toast.makeText(getApplicationContext(), "새로고침", Toast.LENGTH_SHORT).show();
                //파싱 시작
                jsoupAsyncTask1 = new JsoupAsyncTask1();
                jsoupAsyncTask1.execute();

                JsoupAsyncTask1_webview = new JsoupAsyncTask1_webview();
                JsoupAsyncTask1_webview.execute();

                jsoupAsyncTask2 = new JsoupAsyncTask2();
                jsoupAsyncTask2.execute();

                jsoupAsyncTask3 = new JsoupAsyncTask3();
                jsoupAsyncTask3.execute();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}