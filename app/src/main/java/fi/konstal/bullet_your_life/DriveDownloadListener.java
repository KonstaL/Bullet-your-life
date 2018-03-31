package fi.konstal.bullet_your_life;

import java.io.Serializable;

/**
 * Created by e4klehti on 30.3.2018.
 */

public interface DriveDownloadListener<T> extends Serializable {
    void onDownloadSuccess(T data);
    void onDownloadError(Exception exception);
}
