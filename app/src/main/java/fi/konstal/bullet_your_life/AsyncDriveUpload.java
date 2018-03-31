package fi.konstal.bullet_your_life;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import fi.konstal.bullet_your_life.task.CardImage;
import fi.konstal.bullet_your_life.task.CardItem;

/**
 * Created by e4klehti on 30.3.2018.
 */

public class AsyncDriveUpload  {
    private static final String TAG = "AsyncDriveUpload";

    private DriveUploadListener driveUploadListener;
    private Uri imgUri;
    private Context context;
    private DriveResourceClient driveResourceClient;

    public  AsyncDriveUpload(Context context, Uri imgUri, DriveResourceClient driveResourceClient,
                             DriveUploadListener driveUploadListener) {
        this.context = context;
        this.imgUri = imgUri;
        this.driveUploadListener = driveUploadListener;
        this.driveResourceClient = driveResourceClient;
    }


    public void execute() {

        final Task<DriveFolder> appFolderTask = driveResourceClient.getAppFolder();
        final Task<DriveContents> createContentsTask = driveResourceClient.createContents();


        Log.i(TAG, "beginning upload");
        Log.i(TAG, "context = " + context);
        Tasks.whenAll(appFolderTask, createContentsTask) //when both tasks are ready
                .continueWithTask(task -> {
                    DriveFolder parent = appFolderTask.getResult();
                    DriveContents contents = createContentsTask.getResult();


                    int cursor;
                    try (OutputStream os = contents.getOutputStream();
                         InputStream is = context.getContentResolver().openInputStream(imgUri)) {
                        Log.i(TAG, "inside upload ");

                        while((cursor = is.read())!=-1){
                            os.write(cursor);
                        }

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle("uploaded image")
                                .setMimeType("image/jpeg")
                                .build();

                        return  driveResourceClient.createFile(parent, changeSet, contents);

                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        return null;
                    }
                })
                .addOnSuccessListener( driveFile -> {
                    driveUploadListener.onUploadSuccess(driveFile.getDriveId());
                    Log.i(TAG, "driveID created: " + driveFile.getDriveId().toString());

                })
                .addOnFailureListener( e -> {
                            Log.e(TAG, "Unable to upload file", e);
                });



        Log.i(TAG, "end of upload");

    }


}
