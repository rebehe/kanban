package com.henagle.www.kanban;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.henagle.www.kanban.model.Deck;

/**
 * Created by rebeccahe on 5/21/18.
 *
 * This custom adapter is used to pass Deck data into a recycler view
 */
public class DeckAdapter extends RecyclerView.Adapter implements CardTouchHelperCallback.CardTouchHelperAdapter {

    private Deck deck;

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

        DeckAdapter.CardViewHolder cardViewHolder = new DeckAdapter.CardViewHolder(cardLayout);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Replace the contents of the view with the Card at this position in the Deck
        DeckAdapter.CardViewHolder cardViewHolder = (DeckAdapter.CardViewHolder) holder;
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
