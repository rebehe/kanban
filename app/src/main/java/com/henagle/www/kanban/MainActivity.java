package com.henagle.www.kanban;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.henagle.www.kanban.model.Card;
import com.henagle.www.kanban.model.DeckSerializer;
import com.henagle.www.kanban.model.Deck;
import com.henagle.www.kanban.model.Page;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Represents a map of Page -> Deck entries
    // Source of truth for all card data
    protected static Map<Page, Deck> decks;

    private ViewPager viewPager;
    private DeckPagerAdapter viewPagerAdapter;
    private AlertDialog addCardDialog;

    /**
     * Handle card deletion by delegating to the current fragment
     * @param view view of Card to be deleted
     */
    public void deleteClickHandler(View view) {
        DeckFragment currentFragment = (DeckFragment) viewPagerAdapter.getItem(viewPager.getCurrentItem());
        currentFragment.deleteClickHandler(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up app bar
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        // Instantiate decks
        decks = new HashMap<>();
        for (Page page : Page.values()) {
            decks.put(page, new Deck());
        }

        // View pager for managing different decks
        viewPager = findViewById(R.id.view_pager);
        viewPagerAdapter = new DeckPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        createAddCardDialog();
    }

    @Override
    protected void onPause() {
        // Write all lists to files on pause for later recall
        DeckSerializer.writeDeckToFile(this.getApplicationContext(), DeckSerializer.TODO_PATH, decks.get(Page.TODO));
        DeckSerializer.writeDeckToFile(this.getApplicationContext(), DeckSerializer.DOING_PATH, decks.get(Page.DOING));
        DeckSerializer.writeDeckToFile(this.getApplicationContext(), DeckSerializer.DONE_PATH, decks.get(Page.DONE));
        super.onPause();
    }

    @Override
    protected void onResume() {
        // Read all lists from separate files
        Deck resumeTodoDeck = DeckSerializer.readDeckFromFile(this.getApplicationContext(), DeckSerializer.TODO_PATH);
        Deck resumeDoingDeck = DeckSerializer.readDeckFromFile(this.getApplicationContext(), DeckSerializer.DOING_PATH);
        Deck resumeDoneDeck = DeckSerializer.readDeckFromFile(this.getApplicationContext(), DeckSerializer.DONE_PATH);

        // Place lists in the map for later use
        decks.put(Page.TODO, resumeTodoDeck);
        decks.put(Page.DOING, resumeDoingDeck);
        decks.put(Page.DONE, resumeDoneDeck);

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, which adds the plus icon to the action bar
        getMenuInflater().inflate(R.menu.app_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_card) {
            addCardDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAddCardDialog() {
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(layoutParams);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(input);

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String newCardText = input.getText().toString();
                getCurrentDeck().add(new Card(newCardText));
                viewPagerAdapter.notifyDataSetChanged();
                input.getText().clear();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        addCardDialog = builder.create();
    }

    private Deck getCurrentDeck() {
        Page currentPage = Page.values()[viewPager.getCurrentItem()];
        return decks.get(currentPage);
    }

}