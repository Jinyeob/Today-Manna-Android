package com.manna.parsing2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.util.Map;

import android.graphics.Bitmap;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private String htmlPageUrl = "https://community.jbch.org/confirm.php";
    private JsoupAsyncTask jsoupAsyncTask;
    private TextView textviewHtmlDocument;
    private String viewPageUrl = "";
    Bitmap bm;
    private WebView webView;
    private String htmlContentInStringFormat = "";
String ID="";
String PASSWD="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.myAppName);
        setSupportActionBar(toolbar);

        webView = (WebView) findViewById(R.id.webView);

        textviewHtmlDocument = (TextView) findViewById(R.id.textView);
        textviewHtmlDocument.setMovementMethod(new ScrollingMovementMethod()); //스크롤 가능한 텍스트뷰로 만들기

        FloatingActionButton fab=findViewById(R.id.floatingActionButton2);

        Intent loginIntent=getIntent();
        ID=loginIntent.getStringExtra("id");
        PASSWD=loginIntent.getStringExtra("passwd");
        System.out.println("@@@@ ID = "+ID);
        System.out.println("@@@@ PASSWD = "+PASSWD);

        jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
        //새로고침
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "새로고침", Toast.LENGTH_SHORT).show();
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
            }
        });
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
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

                System.out.println("-------------------------------------------------------------");

                for (Element e : titles) {
                    //System.out.println(e.text());
                    htmlContentInStringFormat = e.text().trim() + "\n";
                }

                viewPageUrl = contentATags.attr("abs:src");
                System.out.println(viewPageUrl);
                if(htmlContentInStringFormat.equals("")||viewPageUrl.equals("")){
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"로그인 실패, 다시 로그인 해주세요.",Toast.LENGTH_LONG).show();
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
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.info :
                Intent intent1 = new Intent(getApplicationContext(), app_info.class);
                startActivity(intent1);
                return true;
            case R.id.re_login :
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                SaveSharedPreference.clearUser(MainActivity.this);
                startActivity(intent);
                finish();
                return true;
            case R.id.refresh :
                Toast.makeText(getApplicationContext(), "새로고침", Toast.LENGTH_SHORT).show();
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}