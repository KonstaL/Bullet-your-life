package fi.konstal.bullet_your_life.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.data.DayCard;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthlyLog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthlyLog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthlyLog extends Fragment implements FragmentInterface {

    private FragmentInterface fragmentInterface;

    public MonthlyLog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthlyLog.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthlyLog newInstance(String param1, String param2) {
        MonthlyLog fragment = new MonthlyLog();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Do nothing
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View calendar = inflater.inflate(R.layout.fragment_monthly_log, container, false);

        //Customize calendar
        ConstraintLayout parent = calendar.findViewById(R.id.calendarHeader);
        AppCompatImageButton prev = calendar.findViewById(R.id.previousButton);
        AppCompatImageButton forw = calendar.findViewById(R.id.forwardButton);
        AppCompatTextView monthLabel = calendar.findViewById(R.id.currentDateLabel);

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

   /*     <View
        android:id="@+id/view"
        android:layout_width="130dp"
        android:layout_height="2dp"
        android:layout_marginStart="5dp"
        android:layout_marginTop="1dp"
        android:background="@drawable/section_header_underline"
        android:visibility="visible"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView" />
*/











        return calendar;
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
    public void onDetach() {
        super.onDetach();
        fragmentInterface = null;
    }

    @Override
    public void onCardClicked(DayCard card) {
        fragmentInterface.onCardClicked(card);
    }

}
