package com.manna.parsing2;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
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

import static android.content.Context.CLIPBOARD_SERVICE;

public class Menu1Fragment extends Fragment {
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

    private Button shareButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //로그인 정보
        Intent loginIntent = getActivity().getIntent();
        ID = loginIntent.getStringExtra("id");
        PASSWD = loginIntent.getStringExtra("passwd");

        setHasOptionsMenu(true);


        View v = inflater.inflate(R.layout.fragment_menu1, container, false);

        shareButton=(Button)v.findViewById(R.id.button_share);

                //일요일일때
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        String weekDay = weekdayFormat.format(currentTime);
        if (weekDay.equals("일")) {
            Toast.makeText(getActivity(), "일요일은 지원하지 않습니다.", Toast.LENGTH_LONG).show();
           // Intent intent__ = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
          // startActivity(intent__);
          //  getActivity().finish();
        }
        else {
            MannaView = (TextView) v.findViewById(R.id.textView);

            //파싱 시작
            jsoupAsyncTask1 = new JsoupAsyncTask1();
            jsoupAsyncTask1.execute();

            //공유 버튼 리스너
            shareButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("TEXT", allString);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(getActivity(), "텍스트가 복사되었습니다.", Toast.LENGTH_LONG).show();
                }
            });
        }
        return v;
    }

    private class JsoupAsyncTask1 extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //프로세스 다이얼로그
            progressDialog = new ProgressDialog(getActivity());
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
                            Toast.makeText(getActivity(), "로그인 실패, 다시 로그인 해주세요.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                            SaveSharedPreference.clearUser(getActivity());
                            startActivity(intent);
                            getActivity().finish();
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

                    System.out.println(allString);

                    //만나 말씀
                    for (Element e2 : content_bible) {
                        String text =e2.html();
                        text = text.replaceAll("<br>", "\n\n");
                        allString+=text;
                      //  allString += e2.text().trim() + "\n\n";
                    }

                    System.out.println(allString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            MannaView.setText(allString);
            shareButton.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        }
    }
}