package com.henagle.www.kanban;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.henagle.www.kanban.model.Deck;
import com.henagle.www.kanban.model.Page;

/**
 * Created by rebeccahe on 5/20/18.
 *
 * Instances of this class are fragments representing a single card list.
 */

public class DeckFragment extends Fragment {

    private RecyclerView deckRecyclerView;
    private DeckAdapter deckAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Deck deck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_deck, container, false);

        // Retrieve deck data for this fragment
        Bundle args = getArguments();
        Page page = Page.values()[args.getInt(DeckPagerAdapter.CURRENT_PAGE_ARG)];
        deck = MainActivity.decks.get(page);

        deckRecyclerView = rootView.findViewById(R.id.deck_recycler_view);

        // Use a layout manager
        layoutManager = new LinearLayoutManager(getContext());
        deckRecyclerView.setLayoutManager(layoutManager);

        // Specify an adapter
        deckAdapter = new DeckAdapter(deck);
        deckRecyclerView.setAdapter(deckAdapter);

        // ItemTouchHelper
        ItemTouchHelper.Callback callback = new CardTouchHelperCallback(deckAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(deckRecyclerView);

        return rootView;
    }

}