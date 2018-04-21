package fi.konstal.bullet_your_life.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fi.konstal.bullet_your_life.App;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.EditCardActivity;
import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;
import fi.konstal.bullet_your_life.data.NoteCard;
import fi.konstal.bullet_your_life.daycard_recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.daycard_recycler_view.RecyclerItemClickListener;
import fi.konstal.bullet_your_life.notes_recycler_view.NoteCardViewAdapter;
import fi.konstal.bullet_your_life.view_models.NotesViewModel;


public class FutureLog extends Fragment implements FragmentInterface {
    private static final String TAG = "FutureLog";
    FragmentInterface fragmentInterface;
    NoteCardViewAdapter adapter;
    NotesViewModel viewModel;

    @Inject
    CardRepository cardRepository;

    @BindView(R.id.notecards_recycler_view)
    RecyclerView recyclerView;
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

        //Setup Dagger2
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_future_log, container, false);
        ButterKnife.bind(this, v); // bind ButterKnife to this fragment
        return v;
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

        viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        viewModel.init(cardRepository);
        viewModel.getNoteCards().observe(this, cardList -> {

            if (cardList != null) {
                Log.i(TAG, "OBSERVER DATA RECEIVED");

                if (recyclerView.getAdapter() == null) {
                    adapter = new NoteCardViewAdapter(getContext());
                    recyclerView.setAdapter(adapter);
                    adapter.setCardList(cardList);
                } else {
                    adapter.updateCardList(cardList);
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
                        intent.putExtra("type", "NoteCard");
                        intent.putExtra("id", adapter.getCardList().get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        PopupMenu popupMenu = new PopupMenu(getContext(), view);
                        popupMenu.getMenuInflater().inflate(R.menu.collapsing_toolbar_items, popupMenu.getMenu());
                        popupMenu.show();
                    }
                })
        );

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.collapsing_toolbar_items);
        CollapsingToolbarLayout mCollapsingToolbarLayout = getActivity().findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_notes);
        fab.setOnClickListener((e) -> {


            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            View dialogView = getLayoutInflater().inflate(R.layout.partial_notecard_dialog, null);
            builder.setView(dialogView);

            builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    TextView tv = dialogView.findViewById(R.id.NoteCard_new_title);
                    cardRepository.addNoteCards(new NoteCard(tv.getText().toString()));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();


         /*   Snackbar s = Snackbar.make(getView(), "hello", Snackbar.LENGTH_SHORT);
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

            s.show();*/

        });

    }


    @Override
    public void onCardClicked(DayCard card) {
        fragmentInterface.onCardClicked(card);
    }

}
