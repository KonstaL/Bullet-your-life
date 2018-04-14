package fi.konstal.bullet_your_life.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fi.konstal.bullet_your_life.App;
import fi.konstal.bullet_your_life.Helper;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.EditCardActivity;
import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;
import fi.konstal.bullet_your_life.recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.recycler_view.RecyclerItemClickListener;
import fi.konstal.bullet_your_life.view_models.WeeklyLogViewModel;

public class WeeklyLog extends Fragment implements FragmentInterface {
    public final static int MODIFY_CARD = 10;
    private static final String TAG = "WeeklyLog";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @Inject
    CardRepository cardRepository;

    private CardViewAdapter cardAdapter;
    private FragmentInterface fragmentInterface;
    private EditCardInterface editCardInterface;

    private WeeklyLogViewModel viewModel;


    public WeeklyLog() {
    }

    public static WeeklyLog newInstance() {
        WeeklyLog fragment = new WeeklyLog();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "ON CREATE");


        //Setup Dagger2
        ((App) getActivity().getApplication()).getAppComponent().inject(this);


        //Helper.seedCardData(getContext(), cardRepository);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weekly_log, container, false);
        ButterKnife.bind(this, v); // bind ButterKnife to this fragment

        Log.i(TAG, "ON CREATEVIEW");

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "ON ATTACH");
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
        Log.i(TAG, "ON DETACH");
        super.onDetach();
        fragmentInterface = null;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Log.i(TAG, "ON ACTIVITY CREATED");

        viewModel = ViewModelProviders.of(this).get(WeeklyLogViewModel.class);
        viewModel.init(cardRepository);
        viewModel.getDayCards().observe(this, cardList -> {
            Log.i(TAG, "OBSERVER");
            if (cardList == null) {
                Log.i(TAG, "arvo on null");
            } else {
                Log.i(TAG, "OBSERVER DATA RECEIVED");

                if (recyclerView.getAdapter() == null) {
                    cardAdapter = new CardViewAdapter(getContext());
                    recyclerView.setAdapter(cardAdapter);
                    cardAdapter.setCardList(cardList);
                } else {

                    cardAdapter.updateCardList(cardList);
                }
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.collapsing_toolbar_items);
        CollapsingToolbarLayout mCollapsingToolbarLayout = getActivity().findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "raleway_regular.ttf");
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


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //DayCard myTest = cardList.get(position);

                        Log.d("fuck", editCardInterface.toString());
                        Intent intent = new Intent(getContext(), EditCardActivity.class);
                        //intent.putExtra("dayCard", myTest);
                        intent.putExtra("id", cardAdapter.getCardList().get(position).getId());

                        startActivityForResult(intent, MODIFY_CARD);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == MODIFY_CARD) {
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
        }*/
    }


    @Override
    public void onCardClicked(DayCard card) {
        fragmentInterface.onCardClicked(card);
    }

    public CardRepository getCardRepository() {
        return cardRepository;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        viewModel.getDayCards().removeObservers(this);
    }
}
