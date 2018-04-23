package fi.konstal.bullet_your_life.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fi.konstal.bullet_your_life.App;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.EditCardActivity;
import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;
import fi.konstal.bullet_your_life.daycard_recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.daycard_recycler_view.RecyclerItemClickListener;
import fi.konstal.bullet_your_life.util.Helper;
import fi.konstal.bullet_your_life.view_models.MonthlyLogViewModel;


public class MonthlyLogFragment extends Fragment implements FragmentInterface {
    private static final String TAG = "MonthlyLogFragment";
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.recycler_view_temp)
    RecyclerView recyclerView;
    @Inject
    CardRepository cardRepo;
    private MonthlyLogViewModel viewModel;
    private CardViewAdapter adapter;
    private FragmentInterface fragmentInterface;
    private Unbinder unbinder;

    public MonthlyLogFragment() {
    }

    public static MonthlyLogFragment newInstance() {
        MonthlyLogFragment fragment = new MonthlyLogFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup Dagger2
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_monthly_log, container, false);

        //Style the calendar
        styleCalendar(fragmentView);

        //setup ButterKnife
        unbinder = ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterface) {
            fragmentInterface = (FragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupCalendar();
        setupRecyclerView();

        viewModel = ViewModelProviders.of(this).get(MonthlyLogViewModel.class);
        viewModel.init(cardRepo, Helper.currentDateString());
        viewModel.getDayCards().observe(this, cardList -> {
            if (cardList != null) {
                if (recyclerView.getAdapter() == null) {
                    adapter = new CardViewAdapter(getContext());
                    recyclerView.setAdapter(adapter);
                    adapter.setCardList(cardList);
                } else {
                    if (cardList.size() == 0) {
                        DayCard dayCard = new DayCard(calendarView.getFirstSelectedDate().getTime());
                        cardRepo.insertDayCards(dayCard);
                    }
                    adapter.updateCardList(cardList);
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInterface = null;
    }

    @Override
    public void onCardClicked(DayCard card) {
        fragmentInterface.onCardClicked(card);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void addCard(Date date) {
        cardRepo.getDayCard(Helper.dateToString(date))
                .observe(this, (dayCard -> {
                    if (dayCard != null) {
                        //Render card
                        Log.i("test", dayCard.toString());
                    } else {
                        //Render empty card
                    }
                }));

    }

    public void setupCalendar() {
        //Try to set initially selected date
        try {
            Calendar calendar = Calendar.getInstance();
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        calendarView.setOnDayClickListener((event) -> {
            Calendar currentMonth = calendarView.getCurrentPageDate();
            Calendar clickedDay = event.getCalendar();

            //If the clicked day is in the currently viewed month
            if (currentMonth.get(Calendar.MONTH) == clickedDay.get(Calendar.MONTH)) {
                Date date = clickedDay.getTime();
                viewModel.updateDate(Helper.dateToString(date));
            }
        });
    }

    public void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(), EditCardActivity.class);
                        intent.putExtra("type", "DayCard");
                        intent.putExtra("id", adapter.getCardList().get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    public void styleCalendar(View fragmentView) {
        //Customize calendar
        ConstraintLayout parent = fragmentView.findViewById(R.id.calendarHeader);
        AppCompatImageButton prev = fragmentView.findViewById(R.id.previousButton);
        AppCompatImageButton forw = fragmentView.findViewById(R.id.forwardButton);
        AppCompatTextView monthLabel = fragmentView.findViewById(R.id.currentDateLabel);

        //Remove unnecessary next & previous buttons
        parent.removeView(prev);
        parent.removeView(forw);

        parent.setMaxHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                40,
                getResources().getDisplayMetrics()
        ));

        monthLabel.setHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                40,
                getResources().getDisplayMetrics()
        ));
        monthLabel.setWidth((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                140,
                getResources().getDisplayMetrics()
        ));


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(parent);
        constraintSet.connect(R.id.currentDateLabel,
                ConstraintSet.RIGHT,
                R.id.calendarHeader,
                ConstraintSet.RIGHT,
                0);


        int lineId = 42;
        View line = new View(getContext());
        line.setId(lineId);
        line.setLayoutParams(new LinearLayout.LayoutParams((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                130,
                getResources().getDisplayMetrics()
        ),
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        5,
                        getResources().getDisplayMetrics()
                )));
        // you cant assign a integer, has to be a reference
        line.setBackground(getResources().getDrawable(R.drawable.section_subsection_underline));


        parent.addView(line);

        constraintSet.connect(lineId,
                ConstraintSet.TOP,
                R.id.currentDateLabel,
                ConstraintSet.BOTTOM,
                0);
        constraintSet.connect(lineId,
                ConstraintSet.RIGHT,
                R.id.calendarHeader,
                ConstraintSet.RIGHT,
                0);

        constraintSet.applyTo(parent);
    }
}
