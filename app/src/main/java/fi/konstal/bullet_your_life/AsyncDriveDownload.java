package fi.konstal.bullet_your_life;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by e4klehti on 30.3.2018.
 */

public class AsyncDriveDownload extends AsyncTask<Void, Void, Bitmap> {
    private DriveId driveId;
    private DriveResourceClient driveResourceClient;
    private DownloadReceiver<Bitmap> downloadReceiver;



    public AsyncDriveDownload(DriveId driveId, DriveResourceClient driveResourceClient,
                              DownloadReceiver<Bitmap> downloadReceiver) {
        this.driveId = driveId;
        this.driveResourceClient = driveResourceClient;
        this.downloadReceiver = downloadReceiver;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        DriveFile driveFile = driveId.asDriveFile();
        Bitmap bitmap = null;

        Task<DriveContents> fileTask = driveResourceClient.openFile(driveFile, DriveFile.MODE_READ_ONLY);
        try {
            Tasks.await(fileTask, 30_000, TimeUnit.MILLISECONDS);

            DriveContents driveContent = fileTask.getResult();

            //Used for easy stream closing
            try(InputStream is = driveContent.getInputStream()) {
                bitmap = BitmapFactory.decodeStream(is);
            } catch (Exception e) { e.printStackTrace(); }

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onPostExecute(Bitmap bitmap) {
        downloadReceiver.onSuccess(bitmap);
    }

    @Override
    public void onCancelled() {
        //TODO: Custom exception class
        downloadReceiver.onError(new RuntimeException("Imagedownload failed!"));
    }


}
