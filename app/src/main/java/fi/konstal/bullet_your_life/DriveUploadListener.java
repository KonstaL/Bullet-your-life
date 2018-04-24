package fi.konstal.bullet_your_life;

import com.google.android.gms.drive.DriveId;


/**
 * Interface which helps listen to DriveUploads
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public interface DriveUploadListener {
    /**
     * Called when Drive upload was succesfull
     * @param driveId The Drive ID of the uploaded file
     */
    void onUploadSuccess(DriveId driveId);

    /**
     *  Called when the upload fails
     * @param e the exception
     */
    void onUploadFailure(Exception e);
}
