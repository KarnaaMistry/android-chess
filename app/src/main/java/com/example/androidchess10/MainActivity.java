package com.example.androidchess10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private PieceAdapter pieceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Board board = new Board();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pieceAdapter = new PieceAdapter(this);
        GridView boardGrid = (GridView) findViewById(R.id.board);
        boardGrid.setAdapter(pieceAdapter);

        int pos[] = new int[2];
        for(int i = 63; i >= 0; i--) {
            if(15 < i && i < 48) continue;
            pos = translate(63-i);
            pieceAdapter.mThumbIds[i] = board.positions[pos[0]][pos[1]].getDrawable();
        }

        boardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            boolean firstClick = true;
            int pos1[] = new int[2];
            int pos2[] = new int[2];
            ImageView firstView;
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageView imageView = (ImageView) v;
                if(firstClick){
                    firstView = imageView;
                    pos1 = translate(position);
                    //Maybe add some indication here a piece is selcted
                }else{
                    pos2 = translate(position);

                    //if valid move, change piece positions
                    if(true){
                        Resources r = getResources();
                        Drawable[] layers = new Drawable[2];

                        //Set the first layer to the square the piece is on
                        if (pos2[0] % 2 == 1) {
                            if (pos2[1] % 2 == 0) {layers[0] = r.getDrawable(R.drawable.ic_black);}
                            else {layers[0] = r.getDrawable(R.drawable.ic_white);}
                        } else {
                            if (pos2[1] % 2 == 1) {layers[0] = r.getDrawable(R.drawable.ic_black);}
                            else {layers[0] = r.getDrawable(R.drawable.ic_white);}
                        }

                        //Set second layer to the piece itself and draw it
                        layers[1] = r.getDrawable(R.drawable.ic_bishop_black);
                        LayerDrawable layerDrawable = new LayerDrawable(layers);
                        imageView.setImageDrawable(layerDrawable);

                        //Clear the piece from its last position
                        if (pos1[0] % 2 == 1) {
                            if (pos1[1] % 2 == 0) {layers[0] = r.getDrawable(R.drawable.ic_black);}
                            else {layers[0] = r.getDrawable(R.drawable.ic_white);}
                        } else {
                            if (pos1[1] % 2 == 1) {layers[0] = r.getDrawable(R.drawable.ic_black);}
                            else {layers[0] = r.getDrawable(R.drawable.ic_white);}
                        }
                        firstView.setImageDrawable(layers[0]);
                    }
                    firstClick = false;
                }
            }
        });
    }

    public static int[] translate(int position){
        int column = position%8;
        //int row = (((position - column)/8)-8)*-1;
        int row = (position - column)/8;
        int out[] = {row, column};
        return out;
    }
}