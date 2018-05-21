package com.henagle.www.kanban;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.henagle.www.kanban.model.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rebeccahe on 5/20/18.
 */

public class DeckPagerAdapter extends FragmentPagerAdapter {

    public static final String CURRENT_PAGE_ARG = "current_page";

    private Map<Page, DeckFragment> fragments;

    public DeckPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        // Initialize fragments
        fragments = new HashMap<>();
        for (Page page : Page.values()) {
            fragments.put(page, new DeckFragment());
        }
    }

    @Override
    public Fragment getItem(int pagePosition) {
        if (pagePosition >= 0 || pagePosition <= 2) {
            Page page = Page.values()[pagePosition];
            Fragment fragment = fragments.get(page);

            // Pass the page position into fragment so it knows which deck to load
            Bundle args = new Bundle();
            args.putInt(CURRENT_PAGE_ARG, pagePosition);
            fragment.setArguments(args);

            return fragment;
        }
        // Invalid page position
        return null;
    }

    @Override
    public int getCount() {
        return Page.values().length;
    }

    @Override
    public CharSequence getPageTitle(int pagePosition) {
        return Page.values()[pagePosition].name();
    }
}
