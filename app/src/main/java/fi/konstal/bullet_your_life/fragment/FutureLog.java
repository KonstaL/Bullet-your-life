package fi.konstal.bullet_your_life.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.data.DayCard;



public class FutureLog extends Fragment implements FragmentInterface{
    FragmentInterface fragmentInterface;


    public FutureLog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FutureLog.
     */
    // TODO: Rename and change types and number of parameters
    public static FutureLog newInstance(String param1, String param2) {
        FutureLog fragment = new FutureLog();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /*    if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_future_log, container, false);
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

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.collapsing_toolbar_items);
        CollapsingToolbarLayout mCollapsingToolbarLayout = getActivity().findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_notes);
        fab.setOnClickListener((e)-> {



            Snackbar s = Snackbar.make(getView(), "hello", Snackbar.LENGTH_SHORT);
            ImageView v = getActivity().findViewById(R.id.imageView);
            ViewCompat.setTranslationZ(v, -1);
            s.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);

                    //regardless of the dismiss event, put imageview back to top
                    ViewCompat.setTranslationZ(v, 10);
                    Calendar c = Calendar.getInstance();
                    Date date = new Date();
                    c.setTime(date);
                    Log.d("test", date.toString());
                }

                @Override
                public void onShown(Snackbar transientBottomBar) {
                    super.onShown(transientBottomBar);
                }
            });

            s.show();

        });

    }


    @Override
    public void onCardClicked(DayCard card) {
        fragmentInterface.onCardClicked(card);
    }

}
