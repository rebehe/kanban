package com.henagle.www.kanban.model;

import android.content.Context;
import android.util.Log;

import com.henagle.www.kanban.model.Card;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is meant to serialize and deserialize a card
 * @author dnagle
 *
 */
public class DeckSerializer {
    public static String TODO_PATH = "todo.txt";
    public static String DOING_PATH = "doing.txt";
    public static String DONE_PATH = "done.txt";

    public static Deck readDeckFromFile(Context context, String path) {
        Deck deck = new Deck();

        try {
            FileInputStream inputStream = context.openFileInput(path);

            if (inputStream != null) {
                ObjectInputStream list = new ObjectInputStream(inputStream);
                Object cardList = list.readObject();
                if (cardList instanceof Deck) {
                    for (Object o : (Deck) cardList) {
                        if (o instanceof Card) {
                            deck.add((Card) o);
                        }
                    }
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("deserialization", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("deserialization", "Can not read file: " + e.toString());
        } catch(ClassNotFoundException e ) {
            Log.e("deserialization", "Class not found:" + e.toString());
        }

        return deck;
    }

    public static void writeDeckToFile(Context context, String path, Deck deck) {
        try {
            FileOutputStream fileOut = context.openFileOutput(path, context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(deck);
            out.close();
            fileOut.close();
            System.out.println("\nSerialization Successful... Checkout your specified output file..\n");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
