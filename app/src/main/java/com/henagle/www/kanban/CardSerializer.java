package com.henagle.www.kanban;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is meant to serialize and deserialize a card
 * @author dnagle
 *
 */
public class CardSerializer {
    public static String TODO_PATH = "todo.txt";
    public static String DOING_PATH = "doing.txt";
    public static String DONE_PATH = "done.txt";

    public static List<Card> readListFromFile(Context context, String path) {

        List<Card> returnList = new ArrayList<>();

        try {
            FileInputStream inputStream = context.openFileInput(path);

            if ( inputStream != null ) {
                ObjectInputStream list = new ObjectInputStream(inputStream);
                Object readList = list.readObject();
                if (readList instanceof List) {
                    for (Object o : (List)readList) {
                        if (o instanceof Card) {
                            returnList.add((Card)o);
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

        return returnList;
    }

    public static void writeListToFile(Context context, String path, List<Card> cardList) {
        try {
            FileOutputStream fileOut = context.openFileOutput(path, context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(cardList);
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
