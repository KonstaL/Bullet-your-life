package fi.konstal.bullet_your_life.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.drive.DriveId;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.UUID;

import fi.konstal.bullet_your_life.DriveDownloadListener;
import fi.konstal.bullet_your_life.DriveUploadListener;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.BaseActivity;
import fi.konstal.bullet_your_life.util.Helper;


/**
 * Created by e4klehti on 29.3.2018.
 */

public class CardItem implements Serializable, DriveDownloadListener<Bitmap>, DriveUploadListener {

    public static final int CARD_TASK = 1;
    public static final int CARD_IMAGE = 2;


    private static final String TAG = "CardItemGeneric";

    //Determine type of view rendered
    private int type;

    //Used to determine if its the same item in diff calculations
    private String id;

    //For Text based item
    private String text;
    private boolean crossed;
    private int taskIconRef;

    //For Image based item
    private String imageUriString;
    private String driveId;
    private transient Bitmap image;
    private transient ImageView imageView;

    //Card task
    public CardItem(String text, int taskIconRef) {

        this.type = CARD_TASK;
        this.text = text;
        this.crossed = false;
        this.taskIconRef = taskIconRef;
        this.id = UUID.randomUUID().toString();
    }

    //Card image
    public CardItem(DriveId driveId) {
        this.type = CARD_IMAGE;
        this.driveId = driveId.encodeToString();
        this.id = UUID.randomUUID().toString();
    }

    //Card image
    public CardItem(Uri imageUri) {

        this.type = CARD_IMAGE;
        setImageUri(imageUri);
        this.id = UUID.randomUUID().toString();
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
        this.image = image;

        if (imageView != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    // Code here will run in UI thread
                    imageView.setImageBitmap(image);
                }
            });
        }
    }

    public Uri getImageUri() {
        if (imageUriString != null) return Uri.parse(imageUriString);
        return null;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUriString = imageUri.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
            View v = LayoutInflater.from(context).inflate(R.layout.partial_card_item_image, null);
            imageView = v.findViewById(R.id.card_item_image);
            parent.addView(v);

            if (image == null) {
                //if null, start downloading the image
                if (getImageUri() == null) {
                    //Start downloading the picture
                    BaseActivity baseActivity = ((BaseActivity) context);
                    baseActivity.downloadDriveImage(getDriveId(), this);
                } else {
                    //Load image from URI

                    Glide.with(context)
                            .load(getImageUri())
                            .into(imageView);
                    /*Runnable r = (() -> {
                        try {
                            Bitmap img = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(getImageUri()));
                            new ResizeImageTask(imageView).execute(img);
                        } catch (FileNotFoundException e) {
                            //URI is invalid, remove it
                            setImageUri(null);
                            e.printStackTrace();
                        }
                    });
                    new Thread(r).start();*/
                }

            } else {
                imageView.setImageBitmap(image);
            }


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


    private class ResizeImageTask extends AsyncTask<Bitmap, Void, Bitmap> {
        private ImageView imageView;

        public ResizeImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(Bitmap... bitmaps) {
            Bitmap scaledImg = Helper.getResizedBitmap(bitmaps[0], Helper.SCALE_BY_HEIGHT, 300);
            return scaledImg;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}





