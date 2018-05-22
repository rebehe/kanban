package com.henagle.www.kanban;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    /**
     * This is to handle the delete button being clicked on a Card
     * @param view view of Card to be deleted
     */
    protected void deleteClickHandler(View view) {
//        int index = deckRecyclerView.getPositionForView(view);
//        if (deck.size() > index) {
//            deck.remove(index);
//        }
        deckAdapter.notifyDataSetChanged();
    }

    /*
    This custom adapter is used to pass Deck data into a list view of Cards
     */
    private class DeckAdapter extends RecyclerView.Adapter implements CardTouchHelperCallback.CardTouchHelperAdapter {

        private Deck deck;

        // Provides a reference to the views for each data item
        // Provide access to all views for a data item in a view holder
        public class CardViewHolder extends RecyclerView.ViewHolder {

            public RelativeLayout cardLayout;
            // TODO: Move Card TextView and X button to separate layout files

            public CardViewHolder(RelativeLayout cardLayout) {
                super(cardLayout);
                this.cardLayout = cardLayout;
            }
        }

        public DeckAdapter(Deck deck) {
            this.deck = deck;
        }

        // --- Override methods from RecyclerView.Adapter ---

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout cardLayout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_card_layout, parent, false);

            CardViewHolder cardViewHolder = new CardViewHolder(cardLayout);
            return cardViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // Get Card from Deck at this position
            // Replace the contents of the view with that Card's text
            CardViewHolder cardViewHolder = (CardViewHolder) holder;
            TextView cardTextView = (TextView) cardViewHolder.cardLayout.getChildAt(0);
            cardTextView.setText(deck.get(position).getText());
        }

        @Override
        public int getItemCount() {
            return deck.size();
        }

        // --- Override methods from CardTouchHelperAdapter ---

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    deck.swap(i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    deck.swap(i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            deck.remove(position);
            notifyItemRemoved(position);
        }

    }

}