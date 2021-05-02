package com.example.androidchess10;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ReplayListActivity extends AppCompatActivity {

    private ListView listView;
    public static List<String> list;

    public static Recording selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();

        Resources r = getResources();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_list);

        list = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US);
        for (Recording p : MainActivity.recordingList) {
            list.add(p.getName() + "\n                         (" + sdf.format(p.getDate()) + ")");
        }
        listView = (ListView) findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

                //selected = (String) adapter.getItemAtPosition(position);

                selected = MainActivity.recordingList.get(position);
                //listView.setSelection(position);
                /*Context context = getApplicationContext();
                CharSequence text1 = "you clicked " + position;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text1, duration);
                toast.show();
                return;

                 */
            }
        });





    }



    public void back(View v) {

        Intent home = new Intent(ReplayListActivity.this, MainActivity.class);
        startActivity(home);

    }


    public void play(View v) {

        if (selected == null) {
            Context context = getApplicationContext();
            CharSequence text1 = "Select a recording";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text1, duration);
            toast.show();
            return;
        }

        Intent replayview = new Intent(ReplayListActivity.this, ReplayViewActivity.class);
        startActivity(replayview);


    }

    public void sortByName(View v) {


        MainActivity.recordingList.sort((r1, r2) -> {
            return r1.getName().compareToIgnoreCase(r2.getName());
        });

        list.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US);
        for (Recording p : MainActivity.recordingList) {
            list.add(p.getName() + "\n                         (" + sdf.format(p.getDate()) + ")");
        }
        listView = (ListView) findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        selected = null;


    }

    public void sortByName2(View v) {


        MainActivity.recordingList.sort((r1, r2) -> {
            return r2.getName().compareToIgnoreCase(r1.getName());
        });

        list.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US);
        for (Recording p : MainActivity.recordingList) {
            list.add(p.getName() + "\n                         (" + sdf.format(p.getDate()) + ")");
        }
        listView = (ListView) findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        selected = null;


    }

    public void sortByDate(View v) {

        MainActivity.recordingList.sort((r1, r2) -> {
            return r1.getDate().compareTo(r2.getDate());
        });

        list.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US);
        for (Recording p : MainActivity.recordingList) {
            list.add(p.getName() + "\n                         (" + sdf.format(p.getDate()) + ")");
        }
        listView = (ListView) findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        selected = null;

    }

    public void sortByDate2(View v) {

        MainActivity.recordingList.sort((r1, r2) -> {
            return r2.getDate().compareTo(r1.getDate());
        });

        list.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US);
        for (Recording p : MainActivity.recordingList) {
            list.add(p.getName() + "\n                         (" + sdf.format(p.getDate()) + ")");
        }
        listView = (ListView) findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        selected = null;

    }




}
