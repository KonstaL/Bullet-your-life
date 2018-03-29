package fi.konstal.bullet_your_life.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.EditCardActivity;
import fi.konstal.bullet_your_life.data.CardDataHandler;
import fi.konstal.bullet_your_life.recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.recycler_view.RecyclerItemClickListener;
import fi.konstal.bullet_your_life.recycler_view.RecyclerViewClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * { WeeklyLog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeeklyLog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyLog extends Fragment implements FragmentInterface {
    public final static int MODIFY_CARD = 10;



    private RecyclerView recyclerView;
    private CardViewAdapter cardAdapter;
    private CardDataHandler cardDataHandler;
    private List<DayCard> cardList;

    private FragmentInterface fragmentInterface;
    private EditCardInterface editCardInterface;


    // TODO: Rename parameter arguments, choose names that match

    public WeeklyLog(CardDataHandler cardDataHandler) {
        this.cardDataHandler = cardDataHandler;
        this.cardList = cardDataHandler.getDayCardList();
    }

    public WeeklyLog() {
        this.cardList = new ArrayList<>();
    }


    public static WeeklyLog newInstance(String param1, String param2) {
        WeeklyLog fragment = new WeeklyLog();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        recyclerView =  getView().findViewById(R.id.recycler_view);
        RecyclerViewClickListener listener = (view, position) -> {
            Toast.makeText(getContext(), "Click on " + position, Toast.LENGTH_SHORT).show();
            Log.d("shit", "click tapahtu");
        };



        cardAdapter= new CardViewAdapter(getContext(), listener, cardList);
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


                cardList.get(index).setCardTasks(modifiedCard.getCardTasks());

                Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
                recyclerView.getRecycledViewPool().clear();
            } else {
                Toast.makeText(getContext(), "MOFIFY CARD NOT OK", Toast.LENGTH_SHORT).show();
            }
        }
    }





    @Override
    public void onCardClicked(DayCard card) {
        fragmentInterface.onCardClicked(card);
    }


    /*mDriveClient = Drive.getDriveClient(getApplicationContext(), googleSignInAccount);
    // Build a drive resource client.
    mDriveResourceClient =
            Drive.getDriveResourceClient(getApplicationContext(), googleSignInAccount);
    // Start camera.
    startActivityForResult(
                  new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CODE_CAPTURE_IMAGE);
*/


}
