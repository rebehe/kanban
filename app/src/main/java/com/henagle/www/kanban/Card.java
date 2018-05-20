package com.henagle.www.kanban;

import java.io.Serializable;

/**
 * Created by rebeccahe on 5/19/18.
 */

public class Card implements Serializable {

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
