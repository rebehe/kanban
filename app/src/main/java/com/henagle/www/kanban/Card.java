package com.henagle.www.kanban;

/**
 * Created by rebeccahe on 5/19/18.
 */

public class Card {

    private String text;

    public Card(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
