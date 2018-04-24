package fi.konstal.bullet_your_life.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.drive.DriveId;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.UUID;

import fi.konstal.bullet_your_life.DriveDownloadListener;
import fi.konstal.bullet_your_life.DriveUploadListener;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.BaseActivity;
import fi.konstal.bullet_your_life.util.Helper;


/**
 * Holds CardItem data for Cards
 *
 * @author Konsta Lehtinen
 * @author Konstal
 * @version 1.0
 * @since 1.0
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


    /**
     * Constuctor for {@link CardItem#CARD_TASK} type of card item
     *
     * @param text        The task text
     * @param taskIconRef A reference to the tasks icon
     */
    public CardItem(String text, int taskIconRef) {
        this.type = CARD_TASK;
        this.text = text;
        this.crossed = false;
        this.taskIconRef = taskIconRef;
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Constuctor for {@link CardItem#CARD_IMAGE} type of card item
     *
     * @param driveId The DriveId from where to download the image
     */
    public CardItem(DriveId driveId) {
        this.type = CARD_IMAGE;
        this.driveId = driveId.encodeToString();
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Constuctor for {@link CardItem#CARD_IMAGE} type of card item
     *
     * @param imageUri The URI from where to get the image
     */
    public CardItem(Uri imageUri) {
        this.type = CARD_IMAGE;
        setImageUri(imageUri);
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Returns the CardItems text. Only for {@link CardItem#CARD_TASK}
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the CardItems text. Only for {@link CardItem#CARD_TASK}
     *
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns a boolean whether this card items text has been crossed.
     * Only for {@link CardItem#CARD_TASK}
     *
     * @return is CardItem text crossed
     */
    public boolean isCrossed() {
        return crossed;
    }


    /**
     * Sets whether the CardItems text is crossed. Only for {@link CardItem#CARD_TASK}
     *
     * @param crossed whether the text is crossed
     */
    public void setCrossed(boolean crossed) {
        this.crossed = crossed;
    }

    /**
     * Returns the CardItems icon reference. Only for {@link CardItem#CARD_TASK}
     *
     * @return the icon reference
     */
    public int getTaskIconRef() {
        return taskIconRef;
    }

    /**
     * Returns the CardItems text. Only for {@link CardItem#CARD_TASK}
     *
     * @return the text
     */
    public void setTaskIconRef(int taskIconRef) {
        this.taskIconRef = taskIconRef;
    }


    public DriveId getDriveId() {
        return DriveId.decodeFromString(driveId);
    }

    public void setDriveId(DriveId driveId) {
        this.driveId = driveId.encodeToString();
    }


    /**
     * Sets the image for this CardItem. Only for {@link CardItem#CARD_IMAGE}
     *
     * @param image The image to set
     */
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


    /**
     * Builds the Card item into the parents view.
     *
     * @param context         context for image loading from URI´s
     * @param parent          The parent view for in which to build the view
     * @param onClickListener An onClick listener to this CardItem
     */
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
                }
            } else {
                imageView.setImageBitmap(image);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDownloadSuccess(Bitmap data) {
        Log.i(TAG, "File loaded from Drive");
        image = Helper.getResizedBitmap(data, Helper.SCALE_BY_HEIGHT, 300);

        String filename = "systemFile";
        FileOutputStream outputStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDownloadError(Exception exception) {
        Log.e(TAG, exception.toString());
        exception.printStackTrace();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUploadSuccess(DriveId driveId) {
        setDriveId(driveId);
        Log.i(TAG, "Image with Drive ID: " + this.driveId + " has been successfully uploaded!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUploadFailure(Exception e) {
        e.printStackTrace();
    }
}





