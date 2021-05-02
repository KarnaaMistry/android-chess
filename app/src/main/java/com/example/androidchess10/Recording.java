package com.example.androidchess10;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.Locale;

public class Recording implements Serializable {

    private static final long serialVersionUID = 5027741360377460058L;

    private static final String TAG = "myActivity";
    private String name;
    private List<Board> states;
    private Outcome outcome;
    private Date date;

    public enum Outcome {
        DRAW,
        WHITE_WIN_MATE,
        WHITE_WIN_RES,
        BLACK_WIN_MATE,
        BLACK_WIN_RES
    }


    public Recording(String name, Deque<Board> inputStates, Outcome o) {
        this.outcome = o;
        List<Board> st = new ArrayList<Board>();
        while (!inputStates.isEmpty()) {
            //dequeue
            st.add(MainActivity.backupBoard(inputStates.removeLast()));
        }
        this.states = st;
        this.name = name;
        this.date = new Date();
    }

    public String getName() {
        return this.name;
    }

    public List<Board> getStates() {
        return this.states;
    }

    public Outcome getOutcome() {
        return this.outcome;
    }

    public Date getDate() { return this.date; }


    public static void writeRecording(Recording rec, Context context) throws FileNotFoundException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        File path = context.getFilesDir();
        String filename = rec.getName() + "_" + sdf.format(rec.getDate()) + ".dat";
        File myRec = new File(path, filename);
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(myRec));
        oos.writeObject(rec);

        oos.close();
    }

    public static Recording readRecording(String filename, Context context) throws IOException, ClassNotFoundException {
        File path = context.getFilesDir();
        File myRec = new File(path, filename);
        Log.d(TAG,"inRead: " + myRec.getAbsolutePath());
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(myRec.getPath()));

        Recording rec = (Recording)ois.readObject();

        Log.d(TAG,"AFTER READOBJECT, RECNAME " + rec.toString());

        ois.close();
        return rec;
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        return this.name + " - " + this.outcome + ", " + this.states.size() + " states, " + sdf.format(this.date);
    }




}
