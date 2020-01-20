package com.manna.parsing2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {
    private String htmlPageUrl = "https://community.jbch.org/confirm.php";
    private JsoupAsyncTask1 jsoupAsyncTask1;

    private Map<String, String> loginCookie;
    private Document doc;
    private Element getURL;

    private TextView MannaView;

    private String ID = "";
    private String PASSWD = "";

    private String thumUrlString = "";
    private String allString = "";

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

        MannaView = (TextView) findViewById(R.id.textView);

        //파싱 시작
        jsoupAsyncTask1 = new JsoupAsyncTask1();
        jsoupAsyncTask1.execute();
    }

    private class JsoupAsyncTask1 extends AsyncTask<Void, Void, Void> {
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

                Element today_date = doc.select("div.conbox.active div.condate.font-daum").first();

                //로그인 실패 조건문
                if (today_date == null) {
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
                } else {
                    //만나 날짜 스트링
                    allString = today_date.text() + "\n\n\n";

                    getURL = doc.select("div.conbox.active div.content").first();
                    thumUrlString = getURL
                            .attr("onclick")
                            .replace("getUrl('", "")
                            .replace("', '')", "");

                    String target = "?uid=";
                    int target_num = thumUrlString.indexOf(target);
                    String result;
                    result = thumUrlString.substring(target_num + 5, thumUrlString.indexOf("&") + 1);

                    //실제 말씀 구절 url로 접속
                    Document doc_bible = Jsoup.connect("http://community.jbch.org/meditation/board/process.php")
                            .header("Origin", "http://community.jbch.org")
                            .header("Referer", "http://community.jbch.org/")
                            .data("mode", "load_post")
                            .data("post_uid", result)
                            .cookies(loginCookie)
                            .post();

                    Element title_bible = doc_bible.select("div.titlebox div.title").first();
                    Elements content_bible = doc_bible.select("div.contentbox.fr-view p");

                    //만나 범위
                    allString += title_bible.text() + "\n\n\n";

                    //만나 말씀
                    for (Element e2 : content_bible) {
                        allString += e2.text().trim() + "\n\n";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            MannaView.setText(allString);
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
            case R.id.mc_cheyne:
                Intent intent_mc = new Intent(getApplicationContext(), MccheyneActivity.class);
                startActivity(intent_mc);
                return true;
            case R.id.refresh:
                Toast.makeText(getApplicationContext(), "새로고침", Toast.LENGTH_SHORT).show();
                //파싱 시작
                jsoupAsyncTask1 = new JsoupAsyncTask1();
                jsoupAsyncTask1.execute();
                return true;
            case R.id.re_login:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                SaveSharedPreference.clearUser(MainActivity.this);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}