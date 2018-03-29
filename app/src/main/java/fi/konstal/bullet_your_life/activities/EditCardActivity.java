package fi.konstal.bullet_your_life.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;




import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.recycler_view.CustomLinearLayout;
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.task.CardTask;

public class EditCardActivity extends BaseActivity {
    private static final String TAG = "EditCardActivity";

    private DayCard dayCard;
    private int index;
    private CustomLinearLayout cardContentLayout;

    private DriveClient driveClient;

    private List<FloatingActionButton> fabs;
    private FloatingActionButton mainFab;


    private final static int RESULT_LOAD_IMG = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_card);

        this.fabs = new ArrayList<>();
        this.dayCard = (DayCard) getIntent().getSerializableExtra("dayCard");
        this.index = getIntent().getIntExtra("index", -1);




           /* Init card */
        cardContentLayout = findViewById(R.id.card_content_layout);
        TextView cardTitle = findViewById(R.id.title);
        TextView cardDate = findViewById(R.id.card_date);

        if(dayCard != null) {
            cardTitle.setText(dayCard.getTitle());
            cardDate.setText(dayCard.getDateString());


            for (CardTask cardTask : dayCard.getCardTasks()) {
                View taskView = getLayoutInflater().inflate(R.layout.display_task, null);
                ImageView icon = taskView.findViewById(R.id.task_icon);
                icon.setImageResource(cardTask.getTaskIconRef());


                TextView tv = taskView.findViewById(R.id.task_text);
                tv.setText(cardTask.getText());

                taskView.setOnClickListener((event)-> {
                    Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
                });

                cardContentLayout.addView(taskView);
            }
        } else {
            cardTitle.setText("errors for everyone!");
        }





        /*Init floating action button */
        mainFab = findViewById(R.id.main_fab);
        ImageView dimming_layer = findViewById(R.id.dimming_layer);
        fabs.add((FloatingActionButton) findViewById(R.id.addTodo));
        fabs.add((FloatingActionButton) findViewById(R.id.addPicture));
        fabs.add((FloatingActionButton) findViewById(R.id.addEvent));
        fabs.add((FloatingActionButton) findViewById(R.id.addText));

        Animation mShowButton = AnimationUtils.loadAnimation(this, R.anim.fab_show);
        Animation mHideButton = AnimationUtils.loadAnimation(this, R.anim.fab_hide);
        Animation mShowLayout = AnimationUtils.loadAnimation(this, R.anim.show_layout);
        Animation mHideLayout = AnimationUtils.loadAnimation(this, R.anim.hide_layout);


        mainFab.setOnClickListener((View view)-> {
            if(fabs.get(0).getVisibility() == View.VISIBLE) {
                dimming_layer.setBackgroundColor(getResources().getColor(R.color.transparent));
                mainFab.startAnimation(mHideButton);

                for (FloatingActionButton fab : fabs) {
                    fab.setVisibility(View.GONE);
                    fab.startAnimation(mHideLayout);
                }
            } else {
                dimming_layer.setBackgroundColor(getResources().getColor(R.color.login_image_overlay));
                mainFab.startAnimation(mShowButton);

                for (FloatingActionButton fab : fabs) {
                    fab.setVisibility(View.VISIBLE);
                    fab.startAnimation(mShowLayout);
                }
            }
        });


    }

    public void fabClick(View v) {
        FloatingActionButton fab = (FloatingActionButton) v;

        switch (fab.getId()) {
            case R.id.addEvent:
                addTask(R.drawable.ic_hollow_circle_16dp);
                break;
            case R.id.addPicture:
                getImageIntent();
                break;
            case R.id.addTodo:
                addTask(R.drawable.ic_task_12dp);
                break;
            case R.id.addText:
                addTask(R.drawable.ic_line_black_24dp);
                break;

        }

    }



    public void addTask(int fabRef) {
        mainFab.callOnClick();
        View addTaskView = getLayoutInflater().inflate(R.layout.add_task, null);

        ImageView icon = addTaskView.findViewById(R.id.task_icon);
        icon.setImageResource(fabRef);

        EditText textField = addTaskView.findViewById(R.id.task_text);
        textField.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        FloatingActionButton cancelFab = addTaskView.findViewById(R.id.cancel_fab);
        FloatingActionButton proceedFab = addTaskView.findViewById(R.id.proceed_fab);

        cancelFab.setOnClickListener((event)-> {
            removeTask(addTaskView);

        });

        proceedFab.setOnClickListener((event)-> {
            addTaskToView(addTaskView, fabRef);

        });

        cardContentLayout.addView(addTaskView);
    }

    public void addTaskToView(View v, int iconRef) {
        View taskView = getLayoutInflater().inflate(R.layout.display_task, null);

        ImageView icon = taskView.findViewById(R.id.task_icon);
        icon.setImageResource(iconRef);

        TextView tv = taskView.findViewById(R.id.task_text);
        EditText editText = v.findViewById(R.id.task_text);
        String tempTxt = editText.getText().toString();
        tv.setText(tempTxt);

        taskView.setOnClickListener((event)-> {
            Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
        });

        cardContentLayout.removeView(v);
        cardContentLayout.addView(taskView);
        dayCard.getCardTasks().add(new CardTask(tempTxt, iconRef));



    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("DayCard", dayCard);
        data.putExtra("index", index);
        setResult(RESULT_OK, data);
        finish();

        super.onBackPressed();
    }

    public void removeTask(View v) {
        cardContentLayout.removeView(v);
    }

    public void getImageIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == RESULT_LOAD_IMG) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "imageLoad OK", Toast.LENGTH_SHORT).show();
                Uri imageUri = data.getData();

                createFileInAppFolder(imageUri);
               /* mDriveClient = Drive.getDriveClient(getApplicationContext(), googleSignInAccount);
                // Build a drive resource client.
                mDriveResourceClient =
                        Drive.getDriveResourceClient(getApplicationContext(), googleSignInAccount);*/

            }
        }
    }

    @Override
    protected void onDriveClientReady() {
        this.driveClient = getDriveClient();
    }

    private void createFileInAppFolder(Uri imgUri) {


        final Task<DriveFolder> appFolderTask = getDriveResourceClient().getAppFolder();
        final Task<DriveContents> createContentsTask = getDriveResourceClient().createContents();
        Tasks.whenAll(appFolderTask, createContentsTask)
                .continueWithTask(new Continuation<Void, Task<DriveFile>>() {
                    @Override
                    public Task<DriveFile> then(@NonNull Task<Void> task) throws Exception {
                        DriveFolder parent = appFolderTask.getResult();
                        DriveContents contents = createContentsTask.getResult();
                        int cursor;

                        try (OutputStream os = contents.getOutputStream();
                        InputStream is = getContentResolver().openInputStream(imgUri)) {

                            while((cursor = is.read())!=-1){
                                os.write(cursor);
                            }
                        }

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle("new img")
                                .setMimeType("image/jpeg")
                                .build();


                        return getDriveResourceClient().createFile(parent, changeSet, contents);
                    }
                })
                .addOnSuccessListener(this,
                            new OnSuccessListener<DriveFile>() {
                                @Override
                                public void onSuccess(DriveFile driveFile) {
                                    showMessage("file created" +
                                            driveFile.getDriveId().encodeToString());
                                    // finish();

                                    temp();




                                }
                            })
                .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to create file", e);
                        showMessage("error creating file");
                       //finish();
                    }
                });
    }


    public void temp() {


            final Task<DriveFolder> appFolderTask = getDriveResourceClient().getAppFolder();


            Tasks.whenAll(appFolderTask)
                    .continueWith(new Continuation<Void, Task<MetadataBuffer>>() {
                        @Override
                        public Task<MetadataBuffer> then(@NonNull Task<Void> task) throws Exception {
                            DriveFolder driveFolder = appFolderTask.getResult();


                            return getDriveResourceClient().listChildren(driveFolder);
                        }
                    })
                    .addOnSuccessListener(this, (Task<MetadataBuffer> metadataBufferTask) -> {
                        Tasks.whenAll(metadataBufferTask)
                                .continueWith((Task<Void> task) -> {
                                    showMessage("for loopin edes");

                                    MetadataBuffer metadataBuffer =  metadataBufferTask.getResult();


                                    Log.i(TAG, "Ennen looppia");
                                    Log.i(TAG, "metadata luku " + metadataBuffer.getCount());
                                    Log.i(TAG, "olio " + metadataBuffer.get(0));
                                    Log.i(TAG, "oliotitle " + metadataBuffer.get(0).getTitle());




                                    for (int i = 0; i < metadataBuffer.getCount(); i++) {
                                        Log.i(TAG, metadataBuffer.get(i).getTitle());
                                        Log.i(TAG, metadataBuffer.get(i).getDriveId().toString());
                                    }
                                    Log.i(TAG, "jälkeen  looppia");

                                    metadataBuffer.release();
                                    metadataBufferTask.getResult().release();


                                    return metadataBuffer;
                                });




                        // finish();
                    })
                    .addOnFailureListener(this, (exception) -> {
                        Log.e(TAG, "Unable read metadata", exception);
                        showMessage("error reading metadata");
                        //finish();

                    });


    }

}






