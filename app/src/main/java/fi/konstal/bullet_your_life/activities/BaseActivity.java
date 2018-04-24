package fi.konstal.bullet_your_life.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import fi.konstal.bullet_your_life.AsyncDriveDownload;
import fi.konstal.bullet_your_life.AsyncDriveUpload;
import fi.konstal.bullet_your_life.DriveDownloadListener;
import fi.konstal.bullet_your_life.task.CardItem;
import fi.konstal.bullet_your_life.view_models.EditCardViewModel;

/**
 * An abstract activity that handles authorization and connection to the Drive
 * services.
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * Value for activity result
     */
    protected static final int REQUEST_CODE_SIGN_IN = 400;

    /**
     * Value for activity result
     */
    protected static final int REQUEST_CODE_OPEN_ITEM = 401;

    /**
     * TAG for logging
     */
    private static final String TAG = "BaseActivity";

    /**
     * Drive Resource client, which handles all drive related files
     */
    private static DriveResourceClient mDriveResourceClient;

    /**
     * Drive client which handles drive authentication
     */
    private DriveClient mDriveClient;

    /**
     * Gets preferences whether the user has already been authenticated
     */
    private SharedPreferences preferences;

    /**
     * Checks whether user is auhenticated, and if he is, starts background login
     */
    @Override
    protected void onStart() {
        super.onStart();

        preferences = getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);
        // If user is authenticated, start background sign in process
        if (preferences.getBoolean("is_auth", false)) {
            signIn();
        }
    }

    /**
     * Starts the sign-in process and initializes the Drive client.
     */
    @SuppressLint("RestrictedApi")
    protected void signIn() {
        List<Scope> requiredScopes = new ArrayList<>(2);
        requiredScopes.add(Drive.SCOPE_FILE);
        requiredScopes.add(Drive.SCOPE_APPFOLDER);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null && signInAccount.getGrantedScopes().containsAll(requiredScopes)) {
            initializeDriveClient(signInAccount);
        } else {
            GoogleSignInOptions signInOptions =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestScopes(Drive.SCOPE_FILE)
                            .requestScopes(Drive.SCOPE_APPFOLDER)
                            .build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);

            startActivityForResult(googleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
        }
    }

    /**
     * Removes Drive authentication
     */
    public void signOut() {
        //For some reason, these are required
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .requestScopes(Drive.SCOPE_APPFOLDER)
                        .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
        Task<Void> task = googleSignInClient.signOut();
        task.addOnSuccessListener((e) ->
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show())
                .addOnFailureListener((e) ->
                        Toast.makeText(this, "Sign out failed", Toast.LENGTH_SHORT).show());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode != RESULT_OK) {
                    // Sign-in may fail or be cancelled by the user
                    Log.e(TAG, "Sign-in failed.");
                    return;
                }

                @SuppressLint("RestrictedApi") Task<GoogleSignInAccount> getAccountTask =
                        GoogleSignIn.getSignedInAccountFromIntent(data);
                if (getAccountTask.isSuccessful()) {
                    initializeDriveClient(getAccountTask.getResult());
                } else {
                    Log.e(TAG, "Sign-in failed.");
                    showMessage("Google sign in failed");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Continues the sign-in process, initializing the Drive clients with the current
     * user's account.
     */
    private void initializeDriveClient(GoogleSignInAccount signInAccount) {
        mDriveClient = Drive.getDriveClient(getApplicationContext(), signInAccount);
        mDriveResourceClient = Drive.getDriveResourceClient(getApplicationContext(), signInAccount);
        onDriveClientReady();
    }


    /**
     * Dowloads an image from Google drive if user is authenticated
     *
     * @param driveId               The ID of the file to be downloaded
     * @param driveDownloadListener Callback function to execute after download is complete
     */
    public void downloadDriveImage(DriveId driveId, DriveDownloadListener<Bitmap> driveDownloadListener) {
        if (preferences.getBoolean("is_auth", false)) {
            new AsyncDriveDownload(driveId, mDriveResourceClient, driveDownloadListener).execute();
        } else {
            showMessage("Please login to download images from drive");
        }
    }


    /**
     * Uploads image to google drive
     *
     * @param context Context for getting upload streams from {@link Uri}
     * @param viewModel Updates {@link fi.konstal.bullet_your_life.data.CardRepository}
     * @param cardItem where the DriveID is put after upload
     * @param imgUri URI of the image to upload
     */
    public void uploadDriveImage(Context context, EditCardViewModel viewModel, CardItem cardItem, Uri imgUri) {
        if (preferences.getBoolean("is_auth", false)) {
            new AsyncDriveUpload(context, viewModel, cardItem, imgUri, mDriveResourceClient).execute();
        } else {
            showMessage("Please login to upload images to drive");
        }
    }

    /**
     * Shows a toast message.
     */
    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Called after the user has signed in and the Drive client has been initialized.
     */
    protected abstract void onDriveClientReady();

    /**
     * Returns the driveclient
     *
     * @return the driveclient
     */
    protected DriveClient getDriveClient() {
        return mDriveClient;
    }
}