package fi.konstal.bullet_your_life.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fi.konstal.bullet_your_life.Helper;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.EditCardActivity;
import fi.konstal.bullet_your_life.recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.recycler_view.RecyclerItemClickListener;
import fi.konstal.bullet_your_life.recycler_view.RecyclerViewClickListener;
import fi.konstal.bullet_your_life.task.Task;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeeklyLog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeeklyLog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyLog extends Fragment implements FragmentInterface {
    private RecyclerView recyclerView;
    private CardViewAdapter cardAdapter;
    private List<DayCard> cardList;

    private FragmentInterface fragmentInterface;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public WeeklyLog() {
        // Required empty public constructor

        cardList = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeeklyLog.
     */
    // TODO: Rename and change types and number of parameters
    public static WeeklyLog newInstance(String param1, String param2) {
        WeeklyLog fragment = new WeeklyLog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        recyclerView =  getView().findViewById(R.id.recycler_view);
        RecyclerViewClickListener listener = (view, position) -> {
            Toast.makeText(getContext(), "Click on " + position, Toast.LENGTH_SHORT).show();
            Log.d("shit", "click tapahtu");
        };



        cardAdapter= new CardViewAdapter(getContext(), listener,cardList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardAdapter);


        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        DayCard myTest = cardList.get(position);

                        Intent intent = new Intent(getApplicationContext() , EditCardActivity.class);
                        intent.putExtra("dayCard", myTest);

                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/


        prepareCardData();

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */



    private void prepareCardData() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);

        // add 7 days of cards and examples
        for(int i = 0; i < 7; i++) {
            dt = c.getTime();
            cardList.add(new DayCard(Helper.weekdayString(getContext(), dt), dt,  new Task(getString(R.string.example_task), R.drawable.ic_task_12dp),
                    new Task(getString(R.string.example_event), R.drawable.ic_hollow_circle_16dp)));

            //move to the next day
            c.add(Calendar.DATE, 1);
        }

        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCardClicked(DayCard card) {
        fragmentInterface.onCardClicked(card);
    }
}
