package fi.konstal.bullet_your_life.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.BaseActivity;

/**
 * Created by e4klehti on 29.3.2018.
 */

public class CardImage extends CardItem implements Serializable {
    private static final String TAG = "CardImage";
    private String driveId;

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

    @Override
    public void buildView(Context context, ViewGroup parent, View.OnClickListener onClickListener) {


        DriveFile driveFile = this.getDriveId().asDriveFile();
        BaseActivity baseActivity = ((BaseActivity) context);
        Log.i(TAG, "context: " +baseActivity);
        DriveResourceClient driveResourceClient = baseActivity.getDriveResourceClient();
        Log.i(TAG, "Drive resource: " +driveResourceClient);


        Task<DriveContents> fileTask = driveResourceClient.openFile(driveFile, DriveFile.MODE_READ_ONLY);
        Tasks.whenAll(fileTask)
            .addOnSuccessListener((success) -> {
                DriveContents imgFile = fileTask.getResult();

                Bitmap img;
                try(InputStream is = imgFile.getInputStream()) {
                    img = BitmapFactory.decodeStream(is);

                    View v = LayoutInflater.from(context).inflate(R.layout.partial_card_item_image, null);
                    ImageView imgView = v.findViewById(R.id.card_item_image);
                    imgView.setImageBitmap(img);
                    parent.addView(v);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(context, "Image reading failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener((failure)-> {
                Toast.makeText(context, "Image building failed", Toast.LENGTH_SHORT).show();
        });
    }
}
