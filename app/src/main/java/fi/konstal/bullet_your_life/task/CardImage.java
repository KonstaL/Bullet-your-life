package fi.konstal.bullet_your_life.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.InputStream;
import java.io.Serializable;

import fi.konstal.bullet_your_life.DownloadReceiver;

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.BaseActivity;

/**
 * Created by e4klehti on 29.3.2018.
 */

public class CardImage extends CardItem implements Serializable, DownloadReceiver<Bitmap> {
    private static final String TAG = "CardImage";
    private Uri imageUri;
    private String driveId;

    private transient Bitmap image;

    public CardImage(DriveId driveId) {
        super();
        this.driveId = driveId.encodeToString();
    }

    public DriveId getDriveId() {
        return DriveId.decodeFromString(driveId);
    }

    public void setDriveId(DriveId driveId) {
        this.driveId = driveId.encodeToString();
    }

    @Override
    public CardItem replicate() {
        return new CardImage(getDriveId());
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public void buildView(Context context, ViewGroup parent, View.OnClickListener onClickListener) {

        //if null, start downloading the image
        if(image == null) {
            BaseActivity baseActivity = ((BaseActivity) context);
            baseActivity.downloadDriveImage(getDriveId(), this);
        }



        View v = LayoutInflater.from(context).inflate(R.layout.partial_card_item_image, null);
        ImageView imgView = v.findViewById(R.id.card_item_image);
        imgView.setImageBitmap(image);
        parent.addView(v);

    }

    @Override
    public void onSuccess(Bitmap data) {
        image = data;
    }

    @Override
    public void onError(Exception exception) {
        Log.e(TAG, exception.toString());
        exception.printStackTrace();
    }
}
