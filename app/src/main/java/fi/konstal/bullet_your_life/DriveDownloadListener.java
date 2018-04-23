package fi.konstal.bullet_your_life;

import java.io.Serializable;

/**
 * Interface which helps listen to Drive Downloads
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public interface DriveDownloadListener<T> extends Serializable {
    /**
     * Called then the download was succesfull
     *
     * @param data the downloaded data
     */
    void onDownloadSuccess(T data);

    /**
     * Called when the download failed
     *
     * @param exception The reason it failed
     */
    void onDownloadError(Exception exception);
}
