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
    private JsoupAsyncTask jsoupAsyncTask;
    private TextView textviewHtmlDocument;
    private TextView textviewHtmlDocument2;

    private String viewPageUrl = "";
    private WebView webView;

    private String htmlContentInStringFormat = "";
    private String htmlContentInStringFormat2 = "";

    String ID = "";
    String PASSWD = "";

    private TextView info_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //안내 텍스트뷰
        info_text = (TextView) findViewById(R.id.textView5);

        //날짜 텍스트뷰
        textviewHtmlDocument = (TextView) findViewById(R.id.textView);
        textviewHtmlDocument.setMovementMethod(new ScrollingMovementMethod()); //스크롤 가능한 텍스트뷰로 만들기

        //맥체인 텍스트뷰
        textviewHtmlDocument2 = (TextView) findViewById(R.id.textView_mc);

        //만나범위 웹뷰
        webView = (WebView) findViewById(R.id.webView);

        //새로고침 아이콘
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        //로그인 정보
        Intent loginIntent = getIntent();
        ID = loginIntent.getStringExtra("id");
        PASSWD = loginIntent.getStringExtra("passwd");
        System.out.println("@@@@ ID = " + ID);
        System.out.println("@@@@ PASSWD = " + PASSWD);

        //파싱 시작
        jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        //새로고침 아이콘 이벤트리스너
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "새로고침", Toast.LENGTH_SHORT).show();
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
            }
        });
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
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

                Map<String, String> loginCookie = res.cookies();
                Document doc = Jsoup.connect("https://community.jbch.org/")
                        .cookies(loginCookie)
                        .get();

                Elements titles = doc.select("div.conbox.active div.condate.font-daum");
                Elements contentATags = doc.select("div.conbox.active img");

                //맥체인
                Document doc2 = Jsoup.connect("http://www.bible4u.pe.kr/zbxe/read")
                        .get();
                 Elements titles2 = doc2.select("div.content.xe_content tr[bgcolor=#FFFFFF][align=center][height=20]");

                System.out.println("-------------------------------------------------------------");

                //만나 날짜 스트링
                for (Element e : titles) {
                    //System.out.println(e.text());
                    htmlContentInStringFormat = e.text().trim() + "\n";
                }

                //만나 범위 웹뷰용 URL
                viewPageUrl = contentATags.attr("abs:src");
                //System.out.println(viewPageUrl);

                //맥체인 범위 스트링
                htmlContentInStringFormat2=titles2.text();


                if (htmlContentInStringFormat.equals("") || viewPageUrl.equals("")) {
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
                System.out.println("-------------------------------------------------------------");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textviewHtmlDocument.setText(htmlContentInStringFormat);
            webView.loadUrl(viewPageUrl);

            textviewHtmlDocument2.setText(htmlContentInStringFormat2);

            info_text.setText("오늘도 주님과 화이팅!");
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
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}