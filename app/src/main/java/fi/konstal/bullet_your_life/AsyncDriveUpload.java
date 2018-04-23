package fi.konstal.bullet_your_life;

import android.content.Context;
import android.net.Uri;
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

import fi.konstal.bullet_your_life.task.CardItem;
import fi.konstal.bullet_your_life.view_models.EditCardViewModel;

/**
 * Created by e4klehti on 30.3.2018.
 */

public class AsyncDriveUpload {
    private static final String TAG = "AsyncDriveUpload";

    private EditCardViewModel viewModel;
    private CardItem cardItem;
    private Uri imgUri;
    private Context context;
    private DriveResourceClient driveResourceClient;

    public AsyncDriveUpload(Context context, EditCardViewModel viewModel, CardItem cardItem,
                            Uri imgUri, DriveResourceClient driveResourceClient) {
        this.context = context;
        this.imgUri = imgUri;
        this.cardItem = cardItem;
        this.viewModel = viewModel;
        this.driveResourceClient = driveResourceClient;
    }


    // TODO CLEAN THIS THREADING AND OPTIMIZE CPU USAGE
    public void execute() {

        final Task<DriveFolder> appFolderTask = driveResourceClient.getAppFolder();
        final Task<DriveContents> createContentsTask = driveResourceClient.createContents();


        Log.i(TAG, "beginning upload" + Thread.currentThread());
        Log.i(TAG, "context = " + context);
        Tasks.whenAll(appFolderTask, createContentsTask) //when both tasks are ready
                .continueWithTask(task -> {
                    /*new Thread(()-> {*/


                    DriveFolder parent = appFolderTask.getResult();
                    DriveContents contents = createContentsTask.getResult();


                    try (OutputStream os = contents.getOutputStream();
                         InputStream is = context.getContentResolver().openInputStream(imgUri)) {

                        byte[] buffer = new byte[4096];
                        int n;
                        Log.i(TAG, "inside upload " + Thread.currentThread());

                        while ((n = is.read(buffer)) > 0) {
                            os.write(buffer, 0, n);
                        }


                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle("uploaded image")
                                .setMimeType("image/jpeg")
                                .build();

                        uploadDone(driveResourceClient.createFile(parent, changeSet, contents));

                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    return null;
                });
    }

    public void uploadDone(Task<DriveFile> createdFileTask) {
        Tasks.whenAll(createdFileTask)
                .continueWith((createdFile) -> {

                    try {
                        cardItem.setDriveId(createdFileTask.getResult().getDriveId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    viewModel.updateCardItems(cardItem);

                    return null;
                });
    }
}
