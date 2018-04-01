package fi.konstal.bullet_your_life.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.drive.DriveId;

import java.io.FileNotFoundException;
import java.io.Serializable;

import fi.konstal.bullet_your_life.DriveDownloadListener;

import fi.konstal.bullet_your_life.DriveUploadListener;
import fi.konstal.bullet_your_life.Helper;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.BaseActivity;

/**
 * Created by e4klehti on 29.3.2018.
 */

public class CardImage extends CardItem implements Serializable, DriveDownloadListener<Bitmap>, DriveUploadListener {
    private static final String TAG = "CardImage";
    private String imageUriString;
    private String driveId;

    private transient Bitmap image;

    public CardImage(DriveId driveId) {
        super();
        this.driveId = driveId.encodeToString();
    }

    public CardImage(Uri imageUri) {
        super();
        setImageUri(imageUri);
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
        //new Thread(()-> this.image = Helper.getResizedBitmap(image, Helper.SCALE_BY_HEIGHT, 300) ).start();
        this.image = Helper.getResizedBitmap(image, Helper.SCALE_BY_HEIGHT, 300);
    }

    public Uri getImageUri() {
        return Uri.parse(imageUriString);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUriString = imageUri.toString();
    }

    @Override
    public void buildView(Context context, ViewGroup parent, View.OnClickListener onClickListener) {

        //if null, start downloading the image
        if(image == null) {
            if(getImageUri() == null) {
                //Start downloading the picture
                BaseActivity baseActivity = ((BaseActivity) context);
                baseActivity.downloadDriveImage(getDriveId(), this);
            } else {
                try {
                    //Load image from URI
                    setImage(BitmapFactory.decodeStream(context.getContentResolver().openInputStream(getImageUri())));
                } catch (FileNotFoundException e) {
                    //URI is invalid, remove it
                    setImageUri(null);
                    e.printStackTrace();
                }
            }

        }



        View v = LayoutInflater.from(context).inflate(R.layout.partial_card_item_image, null);
        ImageView imgView = v.findViewById(R.id.card_item_image);
        imgView.setImageBitmap(image);
        parent.addView(v);

    }

    @Override
    public void onDownloadSuccess(Bitmap data) {
        image =  Helper.getResizedBitmap(data, Helper.SCALE_BY_HEIGHT, 300);
    }

    @Override
    public void onDownloadError(Exception exception) {
        Log.e(TAG, exception.toString());
        exception.printStackTrace();
    }

    @Override
    public void onUploadSuccess(DriveId driveId) {
        setDriveId(driveId);
        Log.i(TAG, "Drive ID: " + this.driveId + " has been successfully saved");
    }

    @Override
    public void onUploadFailure(Exception e) {
        e.printStackTrace();
    }
}
