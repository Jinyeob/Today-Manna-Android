package com.manna.parsing2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private Menu1Fragment menu1Fragment = new Menu1Fragment();
    private Menu2Fragment menu2Fragment = new Menu2Fragment();
    private Menu3Fragment menu3Fragment = new Menu3Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);

        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, menu1Fragment).commitAllowingStateLoss();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.action_manna: {
                        transaction.replace(R.id.frameLayout, menu1Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.action_mc: {
                        transaction.replace(R.id.frameLayout, menu2Fragment).commitAllowingStateLoss();
                        break;
                    }/*
                    case R.id.action_mypage: {
                        transaction.replace(R.id.frameLayout, menu3Fragment).commitAllowingStateLoss();
                        break;
                    }*/
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}