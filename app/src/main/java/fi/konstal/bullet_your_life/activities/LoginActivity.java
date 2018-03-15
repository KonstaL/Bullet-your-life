package fi.konstal.bullet_your_life.activities;

import fi.konstal.bullet_your_life.R;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        setContentView(R.layout.activity_login);
    }


    public void startMainActivity(View v) {
        Intent intent = new Intent(this, WeeklyLogsActivity.class);
        startActivity(intent);
    }
}
