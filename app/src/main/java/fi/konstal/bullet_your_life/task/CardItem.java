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
import android.widget.TextView;

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

public class CardItem implements Serializable, DriveDownloadListener<Bitmap>, DriveUploadListener {

    public static final int CARD_TASK = 1;
    public static final int CARD_IMAGE = 2;

    //private String type; //for deserialization
    private static final String TAG = "CardItemGeneric";


    private int type; //Determine type
    private String text;
    private boolean crossed;
    private int taskIconRef;
    private String imageUriString;
    private String driveId;
    private transient Bitmap image;

    //Card task
    public CardItem(String text, int taskIconRef) {

        this.type = CARD_TASK;
        this.text = text;
        this.crossed = false;
        this.taskIconRef = taskIconRef;
    }

    //Card image
    public CardItem(DriveId driveId) {
        this.type = CARD_IMAGE;
        this.driveId = driveId.encodeToString();
    }
    //Card image
    public CardItem(Uri imageUri) {

        this.type = CARD_IMAGE;
        setImageUri(imageUri);
    }


    /*   protected CardItem() {
           id = idCounter++;
       }
   */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return crossed;
    }

    public void setDone(boolean done) {
        this.crossed = done;
    }

    public int getTaskIconRef() {
        return taskIconRef;
    }

    public void setTaskIconRef(int taskIconRef) {
        this.taskIconRef = taskIconRef;
    }


    public DriveId getDriveId() {
        return DriveId.decodeFromString(driveId);
    }

    public void setDriveId(DriveId driveId) {
        this.driveId = driveId.encodeToString();
    }


    public fi.konstal.bullet_your_life.task.CardItem replicate() {
        return null;
        //return new CardItemGeneric(getDriveId());
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

    public int getType() {
        return type;
    }

    public void buildView(Context context, ViewGroup parent, View.OnClickListener onClickListener) {

        if (type == CARD_TASK) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.partial_card_item_task, null);
            view.setOnClickListener(onClickListener);

            ImageView icon = view.findViewById(R.id.task_icon);
            icon.setImageResource(taskIconRef);
            TextView tv = view.findViewById(R.id.task_text);
            tv.setText(text);

            parent.addView(view);
        } else {
            if (image == null) {
                //if null, start downloading the image
                if (getImageUri() == null) {
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
    }

    @Override
    public void onDownloadSuccess(Bitmap data) {
        image = Helper.getResizedBitmap(data, Helper.SCALE_BY_HEIGHT, 300);
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





