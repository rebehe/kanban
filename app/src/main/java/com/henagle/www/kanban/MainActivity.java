package com.henagle.www.kanban;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.henagle.www.kanban.model.Card;
import com.henagle.www.kanban.model.CardSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private Map<Tab,List<Card>> tabLists;
    private Map<Tab, CardListAdapter> adapters;
    private Tab currentTab = Tab.TODO;

    private AlertDialog addCardDialog;

    public enum Tab {
        TODO,
        DOING,
        DONE
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_todo:
                    list.setAdapter(adapters.get(Tab.TODO));
                    currentTab = Tab.TODO;
                    return true;
                case R.id.navigation_doing:
                    list.setAdapter(adapters.get(Tab.DOING));
                    currentTab = Tab.DOING;
                    return true;
                case R.id.navigation_done:
                    list.setAdapter(adapters.get(Tab.DONE));
                    currentTab = Tab.DONE;
                    return true;
            }
            return false;
        }
    };

    /**
     * This is to handle the delete button being clicked on an item
     * @param v
     */
    private void deleteClickHandler(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up navigation tabs
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set up app bar
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        // Set up Lists and Adapters
        tabLists = new HashMap<>();
        adapters = new HashMap<>();

        list = (ListView) findViewById(R.id.listOCards);

        createAddCardDialog();
    }

    @Override
    protected void onPause() {
        // Write all lists to files on pause for later recall
        CardSerializer.writeListToFile(this.getApplicationContext(), CardSerializer.TODO_PATH, tabLists.get(Tab.TODO));
        CardSerializer.writeListToFile(this.getApplicationContext(), CardSerializer.DOING_PATH, tabLists.get(Tab.DOING));
        CardSerializer.writeListToFile(this.getApplicationContext(), CardSerializer.DONE_PATH, tabLists.get(Tab.DONE));
        super.onPause();
    }

    @Override
    protected void onResume() {
        // Read all lists from separate files
        List<Card> resumeTodoList = CardSerializer.readListFromFile(this.getApplicationContext(), CardSerializer.TODO_PATH);
        List<Card> resumeDoingList = CardSerializer.readListFromFile(this.getApplicationContext(), CardSerializer.DOING_PATH);
        List<Card> resumeDoneList = CardSerializer.readListFromFile(this.getApplicationContext(), CardSerializer.DONE_PATH);

        // Place lists in the map for later use
        tabLists.put(Tab.TODO, resumeTodoList);
        tabLists.put(Tab.DOING, resumeDoingList);
        tabLists.put(Tab.DONE, resumeDoneList);

        // Set up adapters for resume
        adapters.put(Tab.TODO, new CardListAdapter(this, tabLists.get(Tab.TODO)));
        adapters.put(Tab.DOING, new CardListAdapter(this, tabLists.get(Tab.DOING)));
        adapters.put(Tab.DONE, new CardListAdapter(this, tabLists.get(Tab.DONE)));

        // Default to todo list
        list.setAdapter(adapters.get(Tab.TODO));
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
                tabLists.get(currentTab).add(new Card(newCardText));
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

    /*
    Custom adapter for card list
     */
    private class CardListAdapter extends BaseAdapter {

        private Context context;
        private List<Card> cards;

        public CardListAdapter(Context context, List<Card> cards) {
            this.context = context;
            this.cards = cards;
        }

        @Override
        public int getCount() {
            return cards.size();
        }

        @Override
        public Object getItem(int position) {
            return cards.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // inflate the layout for each list row
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.view_card_layout, parent, false);
            }

            Card currentCard = (Card) getItem(position);

            // update TextView with card text
            TextView textView = (TextView) view.findViewById(R.id.card);
            textView.setText(currentCard.getText());

            return view;
        }

    }

}