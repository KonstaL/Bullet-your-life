package fi.konstal.bullet_your_life.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.task.Task;

public class EditCardActivity extends AppCompatActivity {
    private String title;
    private String dateString;
    private Task[] tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_card);

        DayCard dayCard = (DayCard) getIntent().getSerializableExtra("dayCard");

        if(dayCard != null) {
            this.title = dayCard.getTitle();
            this.dateString = dayCard.getDateString();
            this.tasks = dayCard.getTasks();
        } else {
            this.title = "errors for everyone!";
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton ll1 = findViewById(R.id.fab_1);
        FloatingActionButton ll2 = findViewById(R.id.fab_2);
        FloatingActionButton ll3 = findViewById(R.id.fab_3);
        ImageView dimming_layer = findViewById(R.id.dimming_layer);

        Animation mShowButton = AnimationUtils.loadAnimation(this, R.anim.fab_show);
        Animation mHideButton = AnimationUtils.loadAnimation(this, R.anim.fab_hide);
        Animation mShowLayout = AnimationUtils.loadAnimation(this, R.anim.show_layout);
        Animation mHideLayout = AnimationUtils.loadAnimation(this, R.anim.hide_layout);


        fab.setOnClickListener((View view)-> {
            if(ll1.getVisibility() == View.VISIBLE) {
                dimming_layer.setBackgroundColor(getResources().getColor(R.color.transparent));
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.GONE);
                ll3.setVisibility(View.GONE);
                ll1.startAnimation(mHideLayout);
                ll2.startAnimation(mHideLayout);
                ll3.startAnimation(mHideLayout);
                fab.startAnimation(mHideButton);
            } else {
                dimming_layer.setBackgroundColor(getResources().getColor(R.color.login_image_overlay));
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.VISIBLE);
                ll3.setVisibility(View.VISIBLE);
                ll1.startAnimation(mShowLayout);
                ll2.startAnimation(mShowLayout);
                ll3.startAnimation(mShowLayout);
                fab.startAnimation(mShowButton);
            }
        });


    }
}
