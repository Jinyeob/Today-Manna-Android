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
    private JsoupAsyncTask1 jsoupAsyncTask1;
    private JsoupAsyncTask_mcchain JsoupAsyncTask_mcchain;

    private Map<String, String> loginCookie;
    private Document doc;
    private Element getURL;

    private TextView dateView;
    private TextView mcchainView_title;
    private TextView mcchainView_content;
    private TextView MannaTitleView;
    private TextView MannaContentView;

    private String ID = "";
    private String PASSWD = "";

    private String thumUrlString = "";
    private String DateString = "";
    private String mcchainString = "";
    private String MannaTitleString = "";
    private String MannaContentString = "";

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

        //만나범위 텍스트뷰
        MannaTitleView = (TextView) findViewById(R.id.today_where);

        //말씀용 텍스트뷰
        MannaContentView = (TextView) findViewById(R.id.textView5);
        //  MannaContentView.setMovementMethod(new ScrollingMovementMethod()); //스크롤 가능

        //날짜 텍스트뷰
        dateView = (TextView) findViewById(R.id.textView);
        //   dateView.setMovementMethod(new ScrollingMovementMethod()); //스크롤 가능

        //맥체인 텍스트뷰
        mcchainView_title = (TextView) findViewById(R.id.textView_mc_info);
        mcchainView_content = (TextView) findViewById(R.id.textView_mc);

        //파싱 시작
        jsoupAsyncTask1 = new JsoupAsyncTask1();
        jsoupAsyncTask1.execute();

        JsoupAsyncTask_mcchain = new JsoupAsyncTask_mcchain();
        JsoupAsyncTask_mcchain.execute();
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
                Connection.Response res = Jsoup.connect("https://community.jbch.org/confirm.php")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36")
                        .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                        .header("Upgrade-Insecure-Requests","1")
                        .data("user_id", ID)
                        .data("saveid", "1")
                        .data("passwd", PASSWD)
                        .data("mode", "")
                        .data("go", "yes")
                        .data("url", "http://community.jbch.org/")
                        .data("LoginButton", "LoginButton")
                        .timeout(5000)
                        .maxBodySize(0)
                        .method(Connection.Method.GET)
                        .execute();

                loginCookie = res.cookies();
                doc = Jsoup.connect("https://community.jbch.org/")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36")
                        .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                        .header("Upgrade-Insecure-Requests","1")
                        .cookies(loginCookie)
                        .get();

                Element today_date = doc.select("div.conbox.active div.condate.font-daum").first();

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

                System.out.println("-----------------------------------------------------------------------------------------------------------");

                //만나 날짜 스트링
                DateString = today_date.text();
                System.out.println("날짜: " + DateString);

                //만나 범위
                MannaTitleString = title_bible.text();
                System.out.println("범위: " + MannaTitleString);

                //만나 실제 말씀
                for (Element e2 : content_bible) {
                    MannaContentString += e2.text().trim() + "\n\n";
                }

                System.out.println("-------------------------------------------------------------------------------------------------------");
                //로그인 실패 조건문
                if (DateString.equals("") || MannaTitleString.equals("")) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //만나 날짜
            dateView.setText(DateString);
            //말씀범위
            MannaTitleView.setText(MannaTitleString);
            //말씀
            MannaContentView.setText(MannaContentString);
            progressDialog.dismiss();
        }
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

                System.out.println("-----------------------------------------------------------------------------------------------------------");
                //맥체인 범위 스트링
                mcchainString = titles2.text();
                System.out.println("맥체인범위: " + mcchainString);

                System.out.println("-------------------------------------------------------------------------------------------------------");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //맥체인
            mcchainView_title.setText("오늘의 맥체인");
            mcchainView_content.setText(mcchainString);
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
                //Toast.makeText(getApplicationContext(), "새로고침", Toast.LENGTH_SHORT).show();
                MannaContentString = "";
                //파싱 시작
                jsoupAsyncTask1 = new JsoupAsyncTask1();
                jsoupAsyncTask1.execute();

                JsoupAsyncTask_mcchain = new JsoupAsyncTask_mcchain();
                JsoupAsyncTask_mcchain.execute();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}