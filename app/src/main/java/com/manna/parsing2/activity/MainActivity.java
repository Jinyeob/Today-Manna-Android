package com.manna.parsing2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.manna.parsing2.Manna.NewMannaFragment;
import com.manna.parsing2.Model.Mccheyne;
import com.manna.parsing2.R;
import com.manna.parsing2.Manna.MannaFragment;
import com.manna.parsing2.Mccheyne.MccheyneFragment;
import com.manna.parsing2.login.LoginActivity;
import com.manna.parsing2.login.SaveSharedPreference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private  NewMannaFragment mannaFragment;
    private  MccheyneFragment mccheyneFragment;

    public static final List<List<Mccheyne>> AllList = new ArrayList<>();
    public static String[] mcString=new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Execute();

        //툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);

        // 첫 화면 지정
        mannaFragment = new NewMannaFragment();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, mannaFragment).commit();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_manna: {
                        if(mannaFragment==null) {
                            mannaFragment=new NewMannaFragment();
                            fragmentManager.beginTransaction().add(R.id.frameLayout, mannaFragment).commit();
                        }
                        if(mannaFragment!=null) {
                            fragmentManager.beginTransaction().show(mannaFragment).commit();
                        }
                        if(mccheyneFragment!=null){
                            fragmentManager.beginTransaction().hide(mccheyneFragment).commit();
                        }
                        break;
                    }
                    case R.id.action_mc: {
                        if(mccheyneFragment==null) {
                            mccheyneFragment=new MccheyneFragment();
                            fragmentManager.beginTransaction().add(R.id.frameLayout, mccheyneFragment).commit();
                        }
                        if(mannaFragment!=null) {
                            fragmentManager.beginTransaction().hide(mannaFragment).commit();
                        }
                        if(mccheyneFragment!=null){
                            fragmentManager.beginTransaction().show(mccheyneFragment).commit();
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                Intent intent1 = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(intent1);
                return true;
//            case R.id.re_login:
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                SaveSharedPreference.clearUser(MainActivity.this);
//                startActivity(intent);
//                finish();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Execute(){
        JsoupAsyncTask_mcchain1 jsoupAsyncTask_mcchain1 = new JsoupAsyncTask_mcchain1();
        JsoupAsyncTask_mcchain2 jsoupAsyncTask_mcchain2 = new JsoupAsyncTask_mcchain2();
        JsoupAsyncTask_mcchain3 jsoupAsyncTask_mcchain3 = new JsoupAsyncTask_mcchain3();
        JsoupAsyncTask_mcchain4 jsoupAsyncTask_mcchain4 = new JsoupAsyncTask_mcchain4();

        jsoupAsyncTask_mcchain1.execute();
        jsoupAsyncTask_mcchain2.execute();
        jsoupAsyncTask_mcchain3.execute();
        jsoupAsyncTask_mcchain4.execute();
    }

    public static String  GetData(Document doc){
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
        String result="";
        for(Mccheyne node : MList){
            result+= (node.getTitle() + node.getPoint() + "\n");
            result+=(node.getText()+"\n\n");
        }
        AllList.add(MList);

        return result;
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
                mcString[0] = GetData(doc);

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

                mcString[1] = GetData(doc);

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

                mcString[2] = GetData(doc);

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

                mcString[3] = GetData(doc);

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