package fi.konstal.bullet_your_life;

import com.google.android.gms.drive.DriveId;

/**
 * Created by e4klehti on 30.3.2018.
 */

public interface DriveUploadListener {
    void onUploadSuccess(DriveId driveId);
    void onUploadFailure(Exception e);
}
