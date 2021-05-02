package com.example.androidchess10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class ReplayViewActivity extends AppCompatActivity {

    private PieceAdapter pieceAdapter;

    public Deque<Board> playbackStack;

    public int ind;
    public int limit;

    public static Board currBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();

        Resources r = getResources();
        setContentView(R.layout.replay_view);

        pieceAdapter = new PieceAdapter(this);
        GridView boardGrid = (GridView) findViewById(R.id.board2);
        boardGrid.setAdapter(pieceAdapter);

        ind = 0;

        //playbackStack = new ArrayDeque<Board>();

        List<Board> ptr = ReplayListActivity.selected.getStates();

        //for (int i = ptr.size()-1; i >= 0; i--) {
        //    playbackStack.addFirst(MainActivity.backupBoard(ptr.get(i)));
        //}

        limit = ptr.size()-1;

        //currBoard = MainActivity.backupBoard(playbackStack.removeFirst());

        drawBoard(ptr.get(ind), boardGrid);



    }

    public void back(View v) {

        Intent replays = new Intent(ReplayViewActivity.this, ReplayListActivity.class);
        startActivity(replays);

    }

    public void home(View v) {

        Intent home = new Intent(ReplayViewActivity.this, MainActivity.class);
        startActivity(home);

    }

    public void drawBoard(Board cboard, GridView boardGrid) {

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = cboard.positions[r][c];
                int pieceDrawable = p.getDrawable();
                int k = 56 - 8*r + c;
                pieceAdapter.mThumbIds[k] = pieceDrawable;
            }
        }
        boardGrid.setAdapter(pieceAdapter);

    }

    public void nextMove(View v) {

        List<Board> ptr = ReplayListActivity.selected.getStates();
        if (ind > limit-1 && ptr.size() > 1) {
            return;
        }

        if (ptr.size() <= 1) {
            TextView textv = (TextView) findViewById(R.id.winner);

            Recording.Outcome out = ReplayListActivity.selected.getOutcome();
            if (out == Recording.Outcome.WHITE_WIN_RES) {
                textv.setText(R.string.winner1);
            } else if (out == Recording.Outcome.BLACK_WIN_RES) {
                textv.setText(R.string.winner2);
            } else if (out == Recording.Outcome.DRAW) {
                textv.setText(R.string.winner3);
            } else if (out == Recording.Outcome.WHITE_WIN_MATE) {
                textv.setText(R.string.winner4);
            } else {    //should be black wins by checkmate
                textv.setText(R.string.winner5);
            }
            ind++;
            return;
        }

        ind++;

        currBoard = MainActivity.backupBoard(ptr.get(ind));


        GridView boardGrid = (GridView) findViewById(R.id.board2);
        drawBoard(currBoard, boardGrid);

        if (ind > limit-1) {

            TextView textv = (TextView) findViewById(R.id.winner);

            Recording.Outcome out = ReplayListActivity.selected.getOutcome();
            if (out == Recording.Outcome.WHITE_WIN_RES) {
                textv.setText(R.string.winner1);
            } else if (out == Recording.Outcome.BLACK_WIN_RES) {
                textv.setText(R.string.winner2);
            } else if (out == Recording.Outcome.DRAW) {
                textv.setText(R.string.winner3);
            } else if (out == Recording.Outcome.WHITE_WIN_MATE) {
                textv.setText(R.string.winner4);
            } else {    //should be black wins by checkmate
                textv.setText(R.string.winner5);
            }

        }


    }


}



