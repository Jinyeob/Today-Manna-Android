package com.manna.parsing2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText idView;
    private EditText passwdView;
    private Button loginButton;
    private CheckBox autoLogin;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idView = (EditText) findViewById(R.id.editText_id);
        passwdView = (EditText) findViewById(R.id.editText_passwd);
        loginButton = (Button) findViewById(R.id.button_login);
        autoLogin = (CheckBox) findViewById(R.id.checkBox);
        scrollView = (ScrollView) findViewById(R.id.scroll_area);

        if (SaveSharedPreference.getUserName(LoginActivity.this).length() != 0 && SaveSharedPreference.getUserPasswd(LoginActivity.this).length() != 0) {
            idView.setText(SaveSharedPreference.getUserName(this).toString());
            passwdView.setText(SaveSharedPreference.getUserPasswd(this).toString());
            autoLogin.setChecked(true);
        }

        idView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollBy(0, 800);
                        }
                    }, 100);
                }
            }
        });
        passwdView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollBy(0, 800);
                        }
                    }, 100);
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idView.getText().toString();
                String passwd = passwdView.getText().toString();

                if (autoLogin.isChecked()) {
                    SaveSharedPreference.setUser(LoginActivity.this, idView.getText().toString(), passwdView.getText().toString());
                } else {
                    SaveSharedPreference.clearUser(LoginActivity.this);
                }

                if (id.length() > 0 && passwd.length() > 0) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("passwd", passwd);

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "ID와 PW를 입력해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                Intent intent_ = new Intent(getApplicationContext(), app_info.class);
                startActivity(intent_);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
