package fi.konstal.bullet_your_life.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import fi.konstal.bullet_your_life.App;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.data.Card;
import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;
import fi.konstal.bullet_your_life.data.NoteCard;
import fi.konstal.bullet_your_life.daycard_recycler_view.CustomLinearLayout;
import fi.konstal.bullet_your_life.edit_recycler_view.CardItemViewAdapter;
import fi.konstal.bullet_your_life.edit_recycler_view.SimpleItemTouchHelperCallback;
import fi.konstal.bullet_your_life.task.CardItem;
import fi.konstal.bullet_your_life.view_models.EditCardViewModel;

/**
 * Activity for editing Cards
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public class EditCardActivity extends BaseActivity {
    /**
     * Used for logging
     */
    private static final String TAG = "EditCardActivity";

    /**
     * Used for image picker activity result
     */
    private final static int RESULT_LOAD_IMG = 10;

    /**
     * Single cards layout which holds {@link CardItem}
     */
    @BindView(R.id.card_content_layout)
    CustomLinearLayout cardContentLayout;

    /**
     * Holds reference to activitys {@link RecyclerView}
     */
    @BindView(R.id.card_item_recycler_view)
    RecyclerView recyclerView;

    /**
     * Holds reference to activitys {@link FloatingActionButton}
     */
    @BindView(R.id.main_fab)
    FloatingActionButton mainFab;

    /**
     * Holds a list of sub-{@link FloatingActionButton}
     */
    @BindViews({R.id.addTodo,
            R.id.addPicture,
            R.id.addEvent,
            R.id.addText})
    List<FloatingActionButton> fabs;

    /**
     * Holds a reference to {@link CardRepository}
     */
    @Inject
    CardRepository cardRepository;

    /**
     * Holds a reference to {@link EditCardViewModel}
     */
    private EditCardViewModel viewModel;

    /**
     * Holds a reference to {@link CardItemViewAdapter}
     */
    private CardItemViewAdapter recyclerViewAdapter;

    /**
     * Determines if the card has been initialized, used in initViewModel
     *
     * @see EditCardActivity#initViewModel()
     */
    private boolean initialized = false;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        ((App) getApplication()).getAppComponent().inject(this); //Setup Dagger2
        ButterKnife.bind(this); // bind ButterKnife to this fragment

        initViewModel();
        initAnimation();
    }

    /**
     * Handles floating actionbutton clicks
     *
     * @param v originating {@link FloatingActionButton}
     */
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

    /**
     * Adds a task adding view to parent
     *
     * @param drawableRef Bullet type (event, task, memo etc..)
     */
    public void addTask(int drawableRef) {
        mainFab.callOnClick();
        View addTaskView = getLayoutInflater().inflate(R.layout.add_task, null);

        ImageView icon = addTaskView.findViewById(R.id.task_icon);
        icon.setImageResource(drawableRef);

        EditText textField = addTaskView.findViewById(R.id.task_text);
        textField.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        addTaskView.findViewById(R.id.cancel_fab).setOnClickListener((event) -> {
            removeView(addTaskView);
        });

        addTaskView.findViewById(R.id.proceed_fab).setOnClickListener((event) -> {
            CardItem cardItem = new CardItem(textField.getText().toString(), drawableRef);
            removeView(addTaskView);
            addItemToDb(cardItem);
        });

        cardContentLayout.addView(addTaskView);
    }

    /**
     * Adds item to the {@link EditCardViewModel}, and through that, to the {@link CardRepository}
     *
     * @param cardItem The card to be added
     */
    public void addItemToDb(CardItem cardItem) {
        viewModel.addCardItems(cardItem);
    }


    /**
     * Removes a add task view when cancel is pressed
     *
     * @param v The view to be removed
     */
    public void removeView(View v) {
        cardContentLayout.removeView(v);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * Starts {@link ImagePicker}
     */
    public void getImageIntent() {
        mainFab.callOnClick();
        ImagePicker.create(this)
                .returnMode(ReturnMode.CAMERA_ONLY)
                .folderMode(true)
                .toolbarFolderTitle("Pick a Folder")
                .toolbarImageTitle("Tap to select")
                .toolbarArrowColor(Color.WHITE)
                .limit(5)
                .imageDirectory("Camera")
                .enableLog(false)
                .start(RESULT_LOAD_IMG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG) {
            if (resultCode == RESULT_OK) {
                List<Image> images = ImagePicker.getImages(data);

                for (Image image : images) {
                    Uri imageUri = Uri.fromFile(new File(image.getPath()));
                    createFileInAppFolder(imageUri);
                }
            }
        }
    }

    /**
     * Starts uploading the picture to Drive if user is authenticated
     * and saves image to the current card
     *
     * @param imgUri {@link Uri} of the card to add
     */
    private void createFileInAppFolder(Uri imgUri) {
        CardItem cardImage = new CardItem(imgUri);
        // Quick and dirty fix
        SharedPreferences sharedpreferences = getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);
        if (sharedpreferences.getBoolean("is_auth", false)) {
            uploadDriveImage(this, viewModel, cardImage, imgUri); // Start Async uploading and adds DriveID when done
        }
        addItemToDb(cardImage);
    }

    /**
     * Initializes the Activitys ViewModel
     * @see  EditCardViewModel
     */
    public void initViewModel() {
        //Determines the type of Viewmodel to use
        int id = getIntent().getIntExtra("id", -1);
        String type = getIntent().getStringExtra("type");
        viewModel = ViewModelProviders.of(this).get(EditCardViewModel.class);
        if (type.equals("DayCard")) {
            viewModel.init(cardRepository, Card.CARD_TYPE_DAYCARD, id);
        } else if (type.equals("NoteCard")) {
            viewModel.init(cardRepository, Card.CARD_TYPE_NOTECARD, id);
        } else {
            throw new RuntimeException("Card type has not been setup");
        }

        /*
        * RecyclerView needs to be configured before data is received, but after viewmodel is
        * initialized
        * */
        initRecyclerView();

        viewModel.getCard().observe(this, card -> {
            if (card == null) {
                Log.i(TAG, "dayCard on null");
            } else {
                if (!initialized) {
                    if (card instanceof NoteCard) {
                        TextView cardTitle = findViewById(R.id.title);
                        cardTitle.setText(((NoteCard) card).getTitle());
                    }
                    if (card instanceof DayCard) {
                        TextView cardDate = findViewById(R.id.card_date);
                        cardDate.setText(((DayCard) card).getDateString());
                    }
                    initialized = true;
                }
                recyclerViewAdapter.updateList(card.getCardItems());
            }
        });

    }

    /**
     * Initializes this activitys {@link RecyclerView}
     */
    public void initRecyclerView() {
        recyclerViewAdapter = new CardItemViewAdapter(this, viewModel, null, new ArrayList<>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);

        //Enable drag and drop functionality
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(
                recyclerViewAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Loads and initializes the activity's animations
     */
    public void initAnimation() {
        /*Init floating action button animations and listeners */
        ImageView dimming_layer = findViewById(R.id.dimming_layer);

        Animation mShowButton = AnimationUtils.loadAnimation(this, R.anim.fab_show);
        Animation mHideButton = AnimationUtils.loadAnimation(this, R.anim.fab_hide);
        Animation mShowLayout = AnimationUtils.loadAnimation(this, R.anim.show_layout);
        Animation mHideLayout = AnimationUtils.loadAnimation(this, R.anim.hide_layout);

        mainFab.setOnClickListener((View view) -> {
            if (fabs.get(0).getVisibility() == View.VISIBLE) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDriveClientReady() {
        //do nothing
    }
}