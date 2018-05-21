package com.henagle.www.kanban;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.henagle.www.kanban.model.Card;
import com.henagle.www.kanban.model.Deck;
import com.henagle.www.kanban.model.Page;

/**
 * Created by rebeccahe on 5/20/18.
 *
 * Instances of this class are fragments representing a single card list.
 */

public class DeckFragment extends Fragment {

    private ListView deckListView;

    private Deck deck;
    private DeckAdapter deckAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_deck, container, false);

        // Retrieve deck data for this fragment
        Bundle args = getArguments();
        Page page = Page.values()[args.getInt(DeckPagerAdapter.CURRENT_PAGE_ARG)];
        deck = MainActivity.decks.get(page);

        // Update list view with deck data
        deckListView = rootView.findViewById(R.id.deck_list_view);
        deckAdapter = new DeckAdapter(getContext(), deck);
        deckListView.setAdapter(deckAdapter);

        return rootView;
    }

    /**
     * This is to handle the delete button being clicked on a Card
     * @param view view of Card to be deleted
     */
    protected void deleteClickHandler(View view) {
        RelativeLayout viewParentRow = (RelativeLayout) view.getParent();
        int index = ((ViewGroup) viewParentRow.getParent()).indexOfChild(viewParentRow);
        if (deck.size() > index) {
            deck.remove(index);
        }
        deckAdapter.notifyDataSetChanged();
    }

    /*
    This custom adapter is used to pass Deck data into a list view of Cards
     */
    private class DeckAdapter extends BaseAdapter {

        private Context context;
        private Deck deck;

        public DeckAdapter(Context context, Deck deck) {
            this.context = context;
            this.deck = deck;
        }

        @Override
        public int getCount() {
            return deck.size();
        }

        @Override
        public Object getItem(int position) {
            return deck.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.view_card_layout, parent, false);
            }

            // Update card text
            Card card = (Card) getItem(position);
            TextView cardTextView = view.findViewById(R.id.card);
            cardTextView.setText(card.getText());

            return view;
        }

    }

}