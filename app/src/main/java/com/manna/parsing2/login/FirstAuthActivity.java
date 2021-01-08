package com.manna.parsing2.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.manna.parsing2.activity.MainActivity;
import com.manna.parsing2.R;

/***
 * Create by Jinyeob
 * Unused Code
 */
public class FirstAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_first_auth);

        Intent intent;
        if (SaveSharedPreference.getUserName(FirstAuthActivity.this).length() == 0 || SaveSharedPreference.getUserPasswd(FirstAuthActivity.this).length() == 0) {
            // call Login Activity
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);

        } else {
            // Call Next Activity
            intent = new Intent(FirstAuthActivity.this, MainActivity.class);
            intent.putExtra("id", SaveSharedPreference.getUserName(this));
            intent.putExtra("passwd", SaveSharedPreference.getUserPasswd(this));

        }
        startActivity(intent);
        this.finish();
    }
}