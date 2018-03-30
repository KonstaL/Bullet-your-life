package fi.konstal.bullet_your_life;

import java.io.Serializable;

/**
 * Created by e4klehti on 30.3.2018.
 */

public interface DownloadReceiver<T> extends Serializable {
    void onSuccess(T data);
    void onError(Exception exception);
}
