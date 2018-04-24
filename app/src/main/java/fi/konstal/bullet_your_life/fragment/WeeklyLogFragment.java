package fi.konstal.bullet_your_life.fragment;

import android.arch.lifecycle.ViewModelProviders;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fi.konstal.bullet_your_life.App;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.EditCardActivity;
import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.daycard_recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.daycard_recycler_view.RecyclerItemClickListener;
import fi.konstal.bullet_your_life.util.Helper;
import fi.konstal.bullet_your_life.util.PopUpHandler;
import fi.konstal.bullet_your_life.view_models.WeeklyLogViewModel;

/**
 * Fragment that displays a weekly view of the DayCards
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public class WeeklyLogFragment extends Fragment {
    private static final String TAG = "WeeklyLogFragment";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    CardRepository cardRepository;

    private CardViewAdapter cardAdapter;
    private WeeklyLogViewModel viewModel;

    /**
     * Empty constructor for database
     */
    public WeeklyLogFragment() {
    }

    /**
     * Returns a new instance of fragment. Meant for initialization
     *
     * @return An instance of WeeklyLogFragment
     */
    public static WeeklyLogFragment newInstance() {
        WeeklyLogFragment fragment = new WeeklyLogFragment();
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup Dagger2
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_weekly_log, container, false);
        ButterKnife.bind(this, v); // bind ButterKnife to this fragment
        return v;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        viewModel = ViewModelProviders.of(this).get(WeeklyLogViewModel.class);
        viewModel.init(cardRepository);
        viewModel.getDayCards().observe(this, cardList -> {
            if (cardList != null) {
                if (recyclerView.getAdapter() == null) {
                    cardAdapter = new CardViewAdapter(getContext());
                    recyclerView.setAdapter(cardAdapter);
                    cardAdapter.setCardList(cardList);

                    // If there isn't a full weeks worth of cards, create the missing ones
                    if (cardList.size() < 7) {
                        Helper.addMissingDays(cardList, cardRepository);
                    }
                } else {
                    cardAdapter.updateCardList(cardList);
                }
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(), EditCardActivity.class);
                        intent.putExtra("type", "DayCard");
                        intent.putExtra("id", cardAdapter.getCardList().get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.collapsing_toolbar_items);
        View popupOpener = getActivity().findViewById(R.id.popup_open);
        popupOpener.setOnClickListener(new PopUpHandler(getContext(), popupOpener));
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        viewModel.getDayCards().removeObservers(this);
    }
}
