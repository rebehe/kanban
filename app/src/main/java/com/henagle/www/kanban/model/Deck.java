package com.henagle.www.kanban.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rebeccahe on 5/20/18.
 *
 * One deck represents a list of cards.
 */

public class Deck implements Serializable, Iterable<Card> {

    private List<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
    }

    @Override
    public Iterator<Card> iterator() {
        return deck.iterator();
    }

    public Deck(List<Card> cards) {
        this.deck = cards;
    }

    /**
     * Returns the size of the deck
     *
     * @return size of deck
     */
    public int size() {
        return deck.size();
    }

    /**
     * Retrieves the card at the specified position
     *
     * @param index
     * @return the card at the specified position
     */
    public Card get(int index) {
        return deck.get(index);
    }

    /**
     * Appends the card to the end of the deck
     *
     * @param card
     * @return boolean result of Collections.add(E)
     */
    public boolean add(Card card) {
        return deck.add(card);
    }

    /**
     * Inserts the card at the specified position
     *
     * @param index
     * @param card
     */
    public void add(int index, Card card) {
        deck.add(index, card);
    }

    /**
     * Replaces/updates the card at the specified position
     *
     * @param index
     * @param card
     * @return the card previously at this position
     */
    public Card set(int index, Card card) {
        return deck.set(index, card);
    }

    /**
     * Removes the card at the specified position
     *
     * @param index
     * @return the card that was removed
     */
    public Card remove(int index) {
        return deck.remove(index);
    }

    /**
     * Swap values at indeces i and j in the deck
     * @param i
     * @param j
     */
    public void swap(int i, int j) {
        Collections.swap(deck, i, j);
    }

}
