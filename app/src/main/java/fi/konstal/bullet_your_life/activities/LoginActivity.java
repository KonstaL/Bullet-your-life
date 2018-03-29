package fi.konstal.bullet_your_life.activities;

import fi.konstal.bullet_your_life.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        findViewById(R.id.no_sign_in).setOnClickListener(this);
    }

    @Override
    protected void onDriveClientReady() {
        SharedPreferences sharedpreferences = getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("is_auth", true);
        editor.putBoolean("init_done", true);
        editor.commit();
        finish();
    }

    public void startMainActivityNoAuth() {
        SharedPreferences sharedpreferences = getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("is_auth", false);
        editor.putBoolean("init_done", true);
        editor.commit();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button_google:
                super.signIn();
                break;
            case R.id.no_sign_in:
                startMainActivityNoAuth();
                break;
        }
    }
}
