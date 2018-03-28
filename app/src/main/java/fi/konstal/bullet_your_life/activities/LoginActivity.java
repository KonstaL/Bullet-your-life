package fi.konstal.bullet_your_life.activities;

import fi.konstal.bullet_your_life.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER), new Scope(Scopes.DRIVE_FILE))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton googleSib = findViewById(R.id.sign_in_button_google);
        googleSib.setSize(SignInButton.SIZE_WIDE);
        googleSib.setOnClickListener(this);

        findViewById(R.id.no_sign_in).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        startMainActivity(account);
    }

    public void startMainActivityNoAuth() {

        SharedPreferences sharedpreferences = getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("no_auth", true);
        editor.putString("google_id", null);
        editor.commit();

        Intent intent = new Intent(this, LogsActivity.class);
        startActivity(intent);
    }

    public void startMainActivity(GoogleSignInAccount gsa) {
        if(gsa != null) {
            Intent intent = new Intent(this, LogsActivity.class);
            startActivity(intent);
        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button_google:
                googleSignIn();
                break;
            case R.id.no_sign_in:
                startMainActivityNoAuth();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, save login ID and show authenticated UI.
            saveLogin(account.getIdToken());
            startMainActivity(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w("error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Authentication failed, code " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            startMainActivity(null);
        }
    }

    @SuppressLint("ApplySharedPref")
    private void saveLogin(String id_token){

        SharedPreferences sharedpreferences = getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("google_id", id_token);
        editor.putBoolean("no_auth", false);
        editor.commit();
    }
}
