package fi.konstal.bullet_your_life.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;




import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.edit_recycler_view.CardItemViewAdapter;
import fi.konstal.bullet_your_life.recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.recycler_view.CustomLinearLayout;
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.task.CardImage;
import fi.konstal.bullet_your_life.task.CardItem;
import fi.konstal.bullet_your_life.task.CardTask;

public class EditCardActivity extends BaseActivity {
    private static final String TAG = "EditCardActivity";

    private DayCard dayCard;
    private int index;
    private CustomLinearLayout cardContentLayout;
    private RecyclerView recyclerView;
    private CardItemViewAdapter recyclerViewAdapter;

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
        recyclerView = findViewById(R.id.card_item_recycler_view);

        TextView cardTitle = findViewById(R.id.title);
        TextView cardDate = findViewById(R.id.card_date);

        if(dayCard != null) {
            cardTitle.setText(dayCard.getTitle());
            cardDate.setText(dayCard.getDateString());

            recyclerViewAdapter= new CardItemViewAdapter(this, null, dayCard.getCardItems());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(recyclerViewAdapter);



         /*   for (CardItem cardItem : dayCard.getCardItems()) {
                cardItem.buildView(this, cardContentLayout, (event)-> {
                    Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
                });
            }*/

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
        switch (v.getId()) {
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


    public void addTask(int drawableRef) {
        mainFab.callOnClick();
        View addTaskView = getLayoutInflater().inflate(R.layout.add_task, null);

        ImageView icon = addTaskView.findViewById(R.id.task_icon);
        icon.setImageResource(drawableRef);

        EditText textField = addTaskView.findViewById(R.id.task_text);
        textField.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        FloatingActionButton cancelFab = addTaskView.findViewById(R.id.cancel_fab);
        FloatingActionButton proceedFab = addTaskView.findViewById(R.id.proceed_fab);

        cancelFab.setOnClickListener((event)-> {
            removeView(addTaskView);
        });

        proceedFab.setOnClickListener((event)-> {
            CardItem cardItem = new CardTask(textField.getText().toString(), drawableRef);

            removeView(addTaskView);
            addItemData(cardItem);
            addCardItemToView(cardItem);

        });

        cardContentLayout.addView(addTaskView);
    }

    public void addItemData(CardItem cardItem) {
        this.dayCard.addCardItems(cardItem);
    }

    public void addCardItemToView(CardItem cardItem) {
        cardItem.buildView(this, cardContentLayout, null);
    }

    public void removeView(View v) {
        cardContentLayout.removeView(v);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
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

    public void getImageIntent() {
        mainFab.callOnClick();
        //OLD
/*        Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image*//*");
    *//*    photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        photoPickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        photoPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);*//*
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);*/

        ImagePicker.create(this)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .limit(10) // max images can be selected (99 by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .start(RESULT_LOAD_IMG); // start image picker activity with request code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == RESULT_LOAD_IMG) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "ImageIntent OK", Toast.LENGTH_SHORT).show();

                List<Image> images = ImagePicker.getImages(data);

                for (Image image : images) {
                    Uri imageUri = Uri.fromFile(new File(image.getPath()));
                    createFileInAppFolder(imageUri);
                }


                //OLD
                //final int flags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                /*Uri imageUri = data.getData();

                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                createFileInAppFolder(imageUri);*/
            }
        }
    }

    private void createFileInAppFolder(Uri imgUri) {
        CardImage cardImage = new CardImage(imgUri);
        uploadDriveImage(this, cardImage, imgUri); // Start Async uploading and adds DriveID when done
        addCardItemToView(cardImage);
        dayCard.addCardItems(cardImage);
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


                                    DriveFile driveFile = metadataBuffer.get(6).getDriveId().asDriveFile();
                                    Task<DriveContents> file = getDriveResourceClient().openFile(driveFile, DriveFile.MODE_READ_ONLY);

                                    Tasks.whenAll(file)
                                           .addOnSuccessListener((success) -> {
                                               DriveContents imgFile = file.getResult();

                                               Bitmap img;
                                               try(InputStream is = imgFile.getInputStream()) {
                                                   img = BitmapFactory.decodeStream(is);

                                                   View v = getLayoutInflater().inflate(R.layout.partial_card_item_image, null);
                                                   ImageView imgView = v.findViewById(R.id.card_item_image);
                                                   imgView.setImageBitmap(img);
                                                   cardContentLayout.addView(v);
                                               } catch (Exception e) {
                                                    Log.d(TAG, e.toString());
                                                   Toast.makeText(this, "Image adding failed", Toast.LENGTH_SHORT).show();
                                               }
                                           });

                                    metadataBuffer.release();

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

    @Override
    protected void onDriveClientReady() {
        Log.i(TAG, "has received driveClient");
        this.driveClient = getDriveClient();
    }

}