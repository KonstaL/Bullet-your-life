package fi.konstal.bullet_your_life.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fi.konstal.bullet_your_life.Helper;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.EditCardActivity;
import fi.konstal.bullet_your_life.activities.LogsActivity;
import fi.konstal.bullet_your_life.data.CardDataHandler;
import fi.konstal.bullet_your_life.recycler_view.CardListDiffCallback;
import fi.konstal.bullet_your_life.recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.recycler_view.RecyclerItemClickListener;
import fi.konstal.bullet_your_life.recycler_view.RecyclerViewClickListener;
import fi.konstal.bullet_your_life.task.CardImage;
import fi.konstal.bullet_your_life.task.CardItem;

public class WeeklyLog extends Fragment implements FragmentInterface {
    private static final String TAG = "WeeklyLog";

    public final static int MODIFY_CARD = 10;

    private RecyclerView recyclerView;
    private CardViewAdapter cardAdapter;
    private CardDataHandler cardDataHandler;
    private List<DayCard> cardList;

    private FragmentInterface fragmentInterface;
    private EditCardInterface editCardInterface;


    public WeeklyLog() {}

    public static WeeklyLog newInstance() {
        WeeklyLog fragment = new WeeklyLog();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cardDataHandler = new CardDataHandler(getContext());
        Helper.seedCardData(getContext(), cardDataHandler);
        cardList = cardDataHandler.getDayCardList();
        ((LogsActivity)getActivity()).setCardList(cardList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weekly_log, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterface) {
            fragmentInterface = (FragmentInterface) context;
            editCardInterface = (EditCardInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInterface = null;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.collapsing_toolbar_items);
        CollapsingToolbarLayout mCollapsingToolbarLayout = getActivity().findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(),  "raleway_regular.ttf");
        mCollapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        mCollapsingToolbarLayout.setExpandedTitleTypeface(tf);

        AppBarLayout appBarLayout = getActivity().findViewById(R.id.app_bar_layout);
        Drawable drawable = toolbar.getMenu().getItem(0).getIcon();
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow;
            int scrollRange = -1;



            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //collapse map
                    isShow = true;
                    drawable.setColorFilter(getResources().getColor(R.color.font_black), PorterDuff.Mode.SRC_ATOP);
                } else if (isShow) {
                    //expanded map
                    isShow = false;
                    drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        recyclerView =  getView().findViewById(R.id.recycler_view);
        //recyclerView.setNestedScrollingEnabled(false);
        cardAdapter= new CardViewAdapter(getContext(), null, cardList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardAdapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        DayCard myTest = cardList.get(position);

                        Log.d("fuck", editCardInterface.toString());
                        Intent intent = new Intent(getContext() , EditCardActivity.class);
                        intent.putExtra("dayCard", myTest);
                        intent.putExtra("index", position);

                        startActivityForResult(intent, MODIFY_CARD);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MODIFY_CARD) {
            if (resultCode == Activity.RESULT_OK) {
                int index = data.getIntExtra("index", -1);
                DayCard modifiedCard = (DayCard) data.getSerializableExtra("DayCard");

                // TODO: modify this so that there is no need for new lists and cloning
                ArrayList<DayCard> newList = new ArrayList<>();

                //Copy the array and replace the target card so with the modified
                for (int i = 0; i < cardList.size(); i++) {
                    if(i == index) newList.add(modifiedCard);
                    else newList.add(cardList.get(i));
                }

                //Compares differences and update only modified DayCards/DayCard fields
                cardAdapter.updateCardList(newList);

                Toast.makeText(getContext(), "data received from edit intent", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "MOFIFY CARD NOT OK", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onCardClicked(DayCard card) {
        fragmentInterface.onCardClicked(card);
    }

    public CardDataHandler getCardDataHandler() {
        return cardDataHandler;
    }

}
