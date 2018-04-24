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
 * Class that handles Google Drive downloading asynchronously
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public class AsyncDriveDownload extends AsyncTask<Void, Void, Bitmap> {
    private DriveId driveId;
    private DriveResourceClient driveResourceClient;
    private DriveDownloadListener<Bitmap> driveDownloadListener;


    /**
     * The constructor
     *
     * @param driveId               Signifies the ID of the file to download
     * @param driveResourceClient   The resourceClient that handles the download process
     * @param driveDownloadListener Called when download is done
     */
    public AsyncDriveDownload(DriveId driveId, DriveResourceClient driveResourceClient,
                              DriveDownloadListener<Bitmap> driveDownloadListener) {
        this.driveId = driveId;
        this.driveResourceClient = driveResourceClient;
        this.driveDownloadListener = driveDownloadListener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Bitmap doInBackground(Void... voids) {
        DriveFile driveFile = driveId.asDriveFile();
        Bitmap bitmap = null;

        Task<DriveContents> fileTask = driveResourceClient.openFile(driveFile, DriveFile.MODE_READ_ONLY);
        try {
            Tasks.await(fileTask, 30_000, TimeUnit.MILLISECONDS);

            DriveContents driveContent = fileTask.getResult();

            //Used for easy stream closing
            try (InputStream is = driveContent.getInputStream()) {
                bitmap = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPostExecute(Bitmap bitmap) {
        driveDownloadListener.onDownloadSuccess(bitmap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancelled() {
        //TODO: Custom exception class
        driveDownloadListener.onDownloadError(new RuntimeException("Imagedownload failed!"));
    }
}
