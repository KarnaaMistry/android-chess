package com.example.androidchess10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private PieceAdapter pieceAdapter;

    final int highlight_color = Color.argb(88, 0, 255, 255);

    public static boolean justDidUndo = false;

    public static boolean white_turn = true;
    public static boolean gameover = false;
    public static int enpassant = 2;
    public static int enpassant_b = 2;
    //public static int enpassant_x = 2;
    public static int[] enpassloc = new int[] {-1, -1, -1, -1, -1, -1};
    public static int[] enpassloc_b = new int[] {-1, -1, -1, -1, -1, -1};
    //public static int[] enpassloc_x = new int[] {-1, -1, -1, -1, -1, -1};
    public static boolean enpasscap = false;
    public static int[] enpasstarg = new int[] {-1, -1};

    public static int[] ini = {-1, -1};
    public static int[] end = {-1, -1};
    public ImageView selectedPiece;

    public static Board currBoard;
    public static Deque<Board> gameStack;

    public static Deque<Integer> enpassantStack;
    public static Deque<int[]> enpasslocStack;

    public static String recordingName;

    public enum Promotype {
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK
    }

    public static Promotype blackPref;
    public static Promotype whitePref;

    EditText recName;

    public static List<Recording> recordingList;

    //public static String THEBIGSTRING = "";

    private static final String TAG = "MyActivity";

    //public static boolean promotiontime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Context context = getApplicationContext();

        recordingList = loadRecordings(context);



        //recordingName = "";
        //recordingList = new ArrayList<Recording>();
        whitePref = Promotype.QUEEN;
        blackPref = Promotype.QUEEN;



        white_turn = true;
        enpassant = 2;
        enpassant_b = 2;
        enpassloc = new int[] {-1, -1, -1, -1, -1, -1};
        enpassloc_b = new int[] {-1, -1, -1, -1, -1, -1};
        enpasscap = false;
        enpasstarg = new int[] {-1, -1};
        ini = new int[]{-1, -1};
        end = new int[]{-1, -1};

        currBoard = new Board();
        gameover = false;
        Resources r = getResources();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pieceAdapter = new PieceAdapter(this);
        GridView boardGrid = (GridView) findViewById(R.id.board);
        boardGrid.setAdapter(pieceAdapter);

        gameStack = new ArrayDeque<Board>();
        enpassantStack = new ArrayDeque<Integer>();
        enpasslocStack = new ArrayDeque<int[]>();

        Board initialBoardCopy = new Board();
        gameStack.addFirst(initialBoardCopy);
        int k = enpassant;
        enpassantStack.addFirst(k);
        enpasslocStack.addFirst(new int[]{-1,-1,-1,-1,-1,-1});

        drawBoard(currBoard, boardGrid);

        TextView title = (TextView) findViewById(R.id.titletext);

        //title.setText("numrecs: " + recordingList.size());

        updatePromoteIcon(MainActivity.this, true);
        updatePromoteIcon(MainActivity.this, false);

        title.setText(R.string.Whitet);

        boardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Context context = getApplicationContext();


                if (gameover) { return; }
                ImageView selectedImView = (ImageView) v;
                if (selectedPiece == null) {   //player has not selected any of their own pieces
                    ini = translate(position);
                    int r = ini[0]; int c = ini[1];
                    if (currBoard.positions[r][c].getColor().equals("white") && white_turn) {
                        //if (firstPiece != null) { firstPiece.setColorFilter(null); }
                        selectedPiece = selectedImView;
                        selectedImView.setColorFilter(highlight_color, PorterDuff.Mode.SRC_ATOP);
                        //title.setText(r + " " + c);
                    }
                    if (currBoard.positions[r][c].getColor().equals("black") && !white_turn) {
                        //if (firstPiece != null) { firstPiece.setColorFilter(null); }
                        selectedPiece = selectedImView;
                        selectedImView.setColorFilter(highlight_color, PorterDuff.Mode.SRC_ATOP);
                        //title.setText(r + " " + c);
                    }
                    return;

                } else {  //player has selected their piece previously

                    end = translate(position);
                    int r1 = ini[0]; int c1 = ini[1];
                    int r2 = end[0]; int c2 = end[1];
                    if (currBoard.positions[r2][c2].getColor().equals("white") && white_turn) {
                        selectedPiece.setColorFilter(null);
                        selectedPiece = selectedImView;
                        selectedImView.setColorFilter(highlight_color, PorterDuff.Mode.SRC_ATOP);
                        ini[0] = r2; ini[1] = c2;
                        end[0] = -1; end[1] = -1;
                        //title.setText(r2 + " " + c2);
                        return;
                    }
                    if (currBoard.positions[r2][c2].getColor().equals("black") && !white_turn) {
                        selectedPiece.setColorFilter(null);
                        selectedPiece = selectedImView;
                        selectedImView.setColorFilter(highlight_color, PorterDuff.Mode.SRC_ATOP);
                        ini[0] = r2; ini[1] = c2;
                        end[0] = -1; end[1] = -1;
                        //title.setText(r2 + " " + c2);
                        return;
                    }


                    end = translate(position);
                   // title.setText("(" + r1 + "," + c1 + ")->(" + r2 + "," + c2 + ")");

                    Piece pc = currBoard.positions[r1][c1];

                    if (!pc.legalMove(currBoard, end)) {
                      //  title.setText("ooh, bad try -->(" + r2 + "," + c2 +")");
                        return;
                    }

                    Board dBackup = backupBoard(currBoard);
                    enpassant_b = enpassant;
                    for (int h = 0; h < 6; h++) { enpassloc_b[h] = enpassloc[h]; }


                    pc.move(currBoard, end);
                    if (enpasscap) {
                        Pawn.enpassCapture(currBoard, enpasstarg[0], enpasstarg[1]);
                        enpasstarg[0] = -1;
                        enpasstarg[1] = -1;
                        enpasscap = false;
                    }

                    if (isInCheck(currBoard, white_turn)) {
                        currBoard = dBackup;
                        enpassant = enpassant_b;
                        for (int h = 0; h < 6; h++) { enpassloc[h] = enpassloc_b[h]; }
                       // title.setText("o uput uslf inchk!");
                        return;
                    }

                    if (pc.getName().charAt(1) == 'p') {

                        if ((white_turn && pc.getCoords()[0] == 7) || (!white_turn && pc.getCoords()[0] == 0)) {  //PROMOTION CHECKER

                            Pawn p = (Pawn) pc;
                            if (white_turn) {

                                if (whitePref == Promotype.ROOK) {
                                    p.promote(currBoard, 'R');
                                } else if (whitePref == Promotype.KNIGHT) {
                                    p.promote(currBoard, 'N');
                                } else if (whitePref == Promotype.BISHOP) {
                                    p.promote(currBoard, 'B');
                                } else {
                                    p.promote(currBoard, 'Q');
                                }

                            } else {

                                if (blackPref == Promotype.ROOK) {
                                    p.promote(currBoard, 'R');
                                } else if (blackPref == Promotype.KNIGHT) {
                                    p.promote(currBoard, 'N');
                                } else if (blackPref == Promotype.BISHOP) {
                                    p.promote(currBoard, 'B');
                                } else {
                                    p.promote(currBoard, 'Q');
                                }
                            }

                        }

                    }

                    //At this point, the move has been successful, and we update our game, and the recording, as well.
                    //We also update or reset all relevant values.

                    //stack updates
                    enpassant++;
                    int enp = enpassant;
                    enpassantStack.addFirst(enp);    //store the most recent enpassant int
                    int[] arr = new int[6];
                    for (int h = 0; h < 6; h++) { arr[h] = enpassloc[h]; }
                    enpasslocStack.addFirst(arr);     //store the most recent enpassant array
                    Board ibackup = backupBoard(currBoard);
                    gameStack.addFirst(ibackup);



                    selectedPiece.setColorFilter(null);
                    selectedPiece = null;

                    white_turn = !white_turn;
                    drawBoard(currBoard, boardGrid);

                    if (white_turn) {
                        title.setText(R.string.Whitet);
                    } else {
                        title.setText(R.string.Blackt);
                    }

                    justDidUndo = false;

                    //title.setText("-D:"+enpassloc[0]+"|"+enpassloc[1]+"|"+enpassloc[2]+"|"+enpassloc[3]+"|"+enpassloc[4]+"|"+enpassloc[5]);
                    //title.setText("enps: " + enpassant);

                    if (isInCheck(currBoard, white_turn)) {
                        if (!canEscapeCheck(currBoard, white_turn)) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            if (!white_turn) {
                                builder.setTitle("Checkmate, White wins! ");
                                builder.setIcon(R.drawable.ic_king_white);
                            } else {
                                builder.setTitle("Checkmate, Black wins! ");
                                builder.setIcon(R.drawable.ic_king_black);
                            }
                            builder.setMessage("\nSave the game recording?\n\n\nGame title:");
                            recName = new EditText(MainActivity.this);


                            builder.setView(recName);
                            recName.setHint("     type here");
                            recName.setHintTextColor(Color.rgb(213, 213, 213));

                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String name = recName.getText().toString();
                                    recordingName = name;

                                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);

                                    if (name.length() < 1) {
                                        Context context = getApplicationContext();
                                        CharSequence text1 = "Empty title, auto-generated one";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text1, duration);
                                        toast.show();

                                        name = "chessRecording";
                                    }

                                    Recording rec = null;
                                    if (!white_turn) {
                                        rec = new Recording(name, gameStack, Recording.Outcome.WHITE_WIN_MATE);
                                    } else {
                                        rec = new Recording(name, gameStack, Recording.Outcome.BLACK_WIN_MATE);
                                    }

                                    //String myDate = sdf.format(rec.getDate());

                                    //dialog.dismiss();

                                    //Log.d(TAG,"new recording bt to be added = " + rec.toString());

                                    recordingList.add(rec);

                                    saveRecordings(getApplicationContext());
                                    Context context = getApplicationContext();
                                    CharSequence text1 = "Recording saved";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text1, duration);
                                    toast.show();

                                    beginAnew();

                                }
                            });
                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    beginAnew();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                public void onCancel(DialogInterface dialog) {
                                    beginAnew();
                                }
                            });

                            dialog.setCanceledOnTouchOutside(true);
                            dialog.show();
                            gameover = true;

                        }
                        if (!gameover) {
                            if (white_turn) {
                                title.setText(R.string.checkW);
                            } else {
                                title.setText(R.string.checkB);
                            }
                        }

                    }

                }

            }

        });


    }

    public void beginAnew() {
        //this.recreate();
        Context context = getApplicationContext();
        recordingList = loadRecordings(context);



        //recordingName = "";
        //recordingList = new ArrayList<Recording>();
        justDidUndo = false;
        white_turn = true;
        whitePref = Promotype.QUEEN;
        blackPref = Promotype.QUEEN;
        enpassant = 2;
        enpassant_b = 2;
        enpassloc = new int[] {-1, -1, -1, -1, -1, -1};
        enpassloc_b = new int[] {-1, -1, -1, -1, -1, -1};
        enpasscap = false;
        enpasstarg = new int[] {-1, -1};
        ini = new int[]{-1, -1};
        end = new int[]{-1, -1};

        currBoard = new Board();
        gameover = false;
        Resources r = getResources();
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        pieceAdapter = new PieceAdapter(this);
        GridView boardGrid = (GridView) findViewById(R.id.board);
        boardGrid.setAdapter(pieceAdapter);

        gameStack = new ArrayDeque<Board>();
        enpassantStack = new ArrayDeque<Integer>();
        enpasslocStack = new ArrayDeque<int[]>();

        Board initialBoardCopy = new Board();
        gameStack.addFirst(initialBoardCopy);
        int k = enpassant;
        enpassantStack.addFirst(k);
        enpasslocStack.addFirst(new int[]{-1,-1,-1,-1,-1,-1});

        drawBoard(currBoard, boardGrid);
        updatePromoteIcon(MainActivity.this, true);
        updatePromoteIcon(MainActivity.this, false);

        TextView title = (TextView) findViewById(R.id.titletext);
        title.setText(R.string.Whitet);

        //.setText("NNumrecs: " + recordingList.size());

        //Log.d(TAG, "began anew");

    }

    public static int[] translate(int position){
        int column = position%8;
        //int row = (((position - column)/8)-8)*-1;
        int row = 7 - (position - column)/8;
        int[] out = {row, column};
        return out;
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


    public void undoMove(View v) {
        if (gameover || justDidUndo) { return; }
        if (gameStack.isEmpty() || gameStack.size() < 2) { return; }
        if (selectedPiece != null) { selectedPiece.setColorFilter(null); }
        selectedPiece = null;

        //pop(), peek()
        gameStack.removeFirst();
        Board prev = gameStack.peekFirst();
        enpassantStack.removeFirst();
        int enpassPrev = enpassantStack.peekFirst();
        //enpasslocStack.removeFirst();
        int[] enlocPrev = enpasslocStack.peekFirst();
        ////for (int h = 0; h < 6; h++) { enlocPrev[h] = [h]; }

        white_turn = !white_turn;


        int k = enpassPrev;
        enpassant = k;
        for (int h = 0; h < 6; h++) { enpassloc[h] = enlocPrev[h]; }
        currBoard = backupBoard(prev);

        GridView boardGrid = (GridView) findViewById(R.id.board);
        drawBoard(currBoard, boardGrid);


        TextView title = (TextView) findViewById(R.id.titletext);

        if (white_turn) {
            title.setText(R.string.Whitet);
        } else {
            title.setText(R.string.Blackt);
        }
        //title.setText("-F:" + enpassloc[0]+"|"+enpassloc[1]+"|"+enpassloc[2]+"|"+enpassloc[3]+"|"+enpassloc[4]+"|"+enpassloc[5]);
        //.setText("enps: " + enpassant);

        justDidUndo = true;

    }

    public void choosePromotion(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose your preferred promotion type:");
        if (white_turn) {
            builder.setIcon(R.drawable.ic_pawn_white);
        } else {
            builder.setIcon(R.drawable.ic_pawn_black);
        }


        builder.setItems(new CharSequence[]{"Queen", "Bishop", "Knight", "Rook"},  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {

                if (position == 3) {
                    if (white_turn) {
                        whitePref = Promotype.ROOK;
                    } else {
                        blackPref = Promotype.ROOK;
                    }
                    updatePromoteIcon(MainActivity.this, white_turn);
                } else if (position == 2) {
                    if (white_turn) {
                        whitePref = Promotype.KNIGHT;
                    } else {
                        blackPref = Promotype.KNIGHT;
                    }
                    updatePromoteIcon(MainActivity.this, white_turn);
                } else if (position == 1) {
                    if (white_turn) {
                        whitePref = Promotype.BISHOP;
                    } else {
                        blackPref = Promotype.BISHOP;
                    }
                    updatePromoteIcon(MainActivity.this, white_turn);
                } else {
                    if (white_turn) {
                        whitePref = Promotype.QUEEN;
                    } else {
                        blackPref = Promotype.QUEEN;
                    }
                    updatePromoteIcon(MainActivity.this, white_turn);
                }
            }
        });
        builder.create().show();

    }

    public static void updatePromoteIcon(MainActivity m, boolean b) {

        if (gameover) { return; }
        ImageView img;

        if (b) {
            img = (ImageView) m.findViewById(R.id.imageViewW);

            if (whitePref == Promotype.ROOK) {
                img.setImageResource(R.drawable.ic_rook_white);
            } else if (whitePref == Promotype.KNIGHT) {
                img.setImageResource(R.drawable.ic_knight_white);
            } else if (whitePref == Promotype.BISHOP) {
                img.setImageResource(R.drawable.ic_bishop_white);
            } else {
                img.setImageResource(R.drawable.ic_queen_white);
            }



        } else {
            img = (ImageView) m.findViewById(R.id.imageViewB);

            if (blackPref == Promotype.ROOK) {
                img.setImageResource(R.drawable.ic_rook_black);
            } else if (blackPref == Promotype.KNIGHT) {
                img.setImageResource(R.drawable.ic_knight_black);
            } else if (blackPref == Promotype.BISHOP) {
                img.setImageResource(R.drawable.ic_bishop_black);
            } else {
                img.setImageResource(R.drawable.ic_queen_black);
            }
        }




    }


    public void resign(View v) {

        if (gameover) { return; }
        gameover = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (!white_turn) {
            builder.setTitle("White wins!  (Black resigned)");
            builder.setIcon(R.drawable.ic_king_white);
        } else {
            builder.setTitle("Black wins!  (White resigned)");
            builder.setIcon(R.drawable.ic_king_black);
        }
        builder.setMessage("\nSave the game recording?\n\n\nGame title:");
        recName = new EditText(this);


        builder.setView(recName);
        recName.setHint("     type here");
        recName.setHintTextColor(Color.rgb(213, 213, 213));

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String name = recName.getText().toString();
                recordingName = name;

                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);

                if (name.length() < 1) {
                    Context context = getApplicationContext();
                    CharSequence text1 = "Empty title, auto-generated one";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text1, duration);
                    toast.show();

                    name = "chessRecording";
                }

                Recording rec = null;
                if (!white_turn) {
                    rec = new Recording(name, gameStack, Recording.Outcome.WHITE_WIN_RES);
                } else {
                    rec = new Recording(name, gameStack, Recording.Outcome.BLACK_WIN_RES);
                }

                //String myDate = sdf.format(rec.getDate());

                //dialog.dismiss();

                //Log.d(TAG,"new recording bt to be added = " + rec.toString());

                recordingList.add(rec);

                saveRecordings(getApplicationContext());

                Context context = getApplicationContext();
                CharSequence text1 = "Recording saved";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text1, duration);
                toast.show();

                beginAnew();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                beginAnew();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                beginAnew();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


    }





    public void draw(View v) {

        if (gameover) { return; }
        gameover = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Draw!");
        builder.setMessage("\nSave the game recording?\n\n\nGame title:");
        recName = new EditText(this);


        builder.setView(recName);
        builder.setIcon(R.drawable.ic_king_gray);
        recName.setHint("     type here");
        recName.setHintTextColor(Color.rgb(213, 213, 213));

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String name = recName.getText().toString();
                recordingName = name;

                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);

                if (name.length() < 1) {
                    Context context = getApplicationContext();
                    CharSequence text1 = "Empty title, auto-generated one";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text1, duration);
                    toast.show();

                    name = "chessRecording";
                }



                Recording rec = new Recording(name, gameStack, Recording.Outcome.DRAW);

                //String myDate = sdf.format(rec.getDate());

                //dialog.dismiss();

                //Log.d(TAG,"new recording bt to be added = " + rec.toString());

                recordingList.add(rec);

                saveRecordings(getApplicationContext());

                Context context = getApplicationContext();
                CharSequence text1 = "Recording saved";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text1, duration);
                toast.show();

                beginAnew();



            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                beginAnew();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                beginAnew();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }


    public void toReplay(View v) {

        Intent intent = new Intent(MainActivity.this, ReplayListActivity.class);
        beginAnew();
        startActivity(intent);


    }




    public static List<Recording> loadRecordings(Context context) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        List<Recording> out = new ArrayList<Recording>();

        List<String> dataFiles = new ArrayList<String>();
        File folder = new File(String.valueOf(context.getFilesDir()));
        File[] list = null;
        list = folder.listFiles();
        if (list != null) {
            for (File file : list) {
                if (file.isFile()) {
                    dataFiles.add(file.getName());
                }
            }
        }

        for (String str : dataFiles) {
           // Log.d(TAG,"datafilename : " + str);
            Recording u = null;
            try {
                u = Recording.readRecording(str, context);
            } catch (ClassNotFoundException | IOException e) {
               // Log.d(TAG,"IO Exception in loadRecordings(readrecordings)");
               // Log.d(TAG,e.getMessage());
            }
            out.add(u);
        }

        //Log.d(TAG,"finished loadRec");

        return out;
    }

    public static void saveRecordings(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        List<Recording> recs = new ArrayList<Recording>(recordingList);

        List<String> filenames = new ArrayList<String>();
        //(TAG,"recording size: -h " + recs.size());
        //Log.d(TAG,"second? " + recs.get(1).toString());
        for (Recording u : recs) {
            if (u == null) {
               // Log.d(TAG,"hello null world");
            }
            //Log.d(TAG,u.toString());
            //Log.d(TAG,"u = " + u.getName());
            filenames.add(u.getName() + "_" + sdf.format(u.getDate()) + ".dat");
        }
        File folder = context.getFilesDir();
        File[] list = null;
        list = folder.listFiles();

        //Log.d(TAG,"folder list size: " + list.length);


        if (list != null) {
            for (File file : list) {
                if (!filenames.contains(file.getName())) {
                    file.delete();
                }
            }
        }
        for (Recording u : recs) {
            try {
                Recording.writeRecording(u, context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Log.d(TAG,"finished saveRec");
    }






    public void AIMove(View v) {
        if (gameover) { return; }
        String color;
        if (white_turn) { color = "white"; }
        else { color = "black"; }

        Board chessboard = backupBoard(currBoard);
        enpassant_b = enpassant;
        for (int h = 0; h < 6; h++) { enpassloc_b[h] = enpassloc[h]; }

        List<Integer[]> pieces = new ArrayList<Integer[]>();


        for (int r0 = 0; r0 < 8; r0++) {
            for (int c0 = 0; c0 < 8; c0++) {

                if (chessboard.positions[r0][c0].getColor().equals(color)) {
                    pieces.add(new Integer[]{r0,c0});
                }

            }
        }

        Collections.shuffle(pieces);

        for (Integer[] k : pieces) {
            int r = k[0];
            int c = k[1];

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    //Board theBackup = backupBoard(currBoard);
                    if (color.equals("white")) {
                        end[0] = i;
                        end[1] = j;
                    } else {
                        end[0] = 8-1-i;
                        end[1] = 8-1-j;
                    }

                    Piece pc = currBoard.positions[r][c];  //Candidate piece that may do a successful move.
                    if (!pc.legalMove(currBoard, end)) {
                        continue;
                    }

                    Board dBackup = backupBoard(currBoard);
                    enpassant_b = enpassant;
                    for (int h = 0; h < 6; h++) {
                        enpassloc_b[h] = enpassloc[h];
                    }


                    pc.move(currBoard, end);
                    if (enpasscap) {
                        Pawn.enpassCapture(currBoard, enpasstarg[0], enpasstarg[1]);
                        enpasstarg[0] = -1;
                        enpasstarg[1] = -1;
                        enpasscap = false;
                    }

                    if (isInCheck(currBoard, white_turn)) {
                        currBoard = dBackup;
                        enpassant = enpassant_b;
                        for (int h = 0; h < 6; h++) {
                            enpassloc[h] = enpassloc_b[h];
                        }
                        continue;
                    }


                    if (pc.getName().charAt(1) == 'p') {    //PROMOTION CHECKER
                        if ((white_turn && pc.getCoords()[0] == 7) || (!white_turn && pc.getCoords()[0] == 0)) {

                            Pawn p = (Pawn) pc;
                            if (white_turn) {

                                if (whitePref == Promotype.ROOK) {
                                    p.promote(currBoard, 'R');
                                } else if (whitePref == Promotype.KNIGHT) {
                                    p.promote(currBoard, 'N');
                                } else if (whitePref == Promotype.BISHOP) {
                                    p.promote(currBoard, 'B');
                                } else {
                                    p.promote(currBoard, 'Q');
                                }

                            } else {

                                if (blackPref == Promotype.ROOK) {
                                    p.promote(currBoard, 'R');
                                } else if (blackPref == Promotype.KNIGHT) {
                                    p.promote(currBoard, 'N');
                                } else if (blackPref == Promotype.BISHOP) {
                                    p.promote(currBoard, 'B');
                                } else {
                                    p.promote(currBoard, 'Q');
                                }
                            }

                        }
                    }



                    //At this point, the move has been successful, and we update our game, and the recording, as well.
                    //We also update or reset all relevant values.

                    //stack updates
                    enpassant++;
                    int enp = enpassant;
                    enpassantStack.addFirst(enp);    //store the most recent enpassant int
                    int[] arr = new int[6];
                    for (int h = 0; h < 6; h++) {
                        arr[h] = enpassloc[h];
                    }
                    enpasslocStack.addFirst(arr);     //store the most recent enpassant array
                    Board ibackup = backupBoard(currBoard);
                    gameStack.addFirst(ibackup);


                    if (selectedPiece != null) {
                        selectedPiece.setColorFilter(null);
                    }
                    selectedPiece = null;

                    white_turn = !white_turn;
                    TextView title = (TextView) findViewById(R.id.titletext);
                    if (white_turn) {
                        title.setText(R.string.Whitet);
                    } else {
                        title.setText(R.string.Blackt);
                    }

                    GridView boardGrid = (GridView) findViewById(R.id.board);
                    drawBoard(currBoard, boardGrid);


                    //title.setText("suc AI, e:(" + r + "," + c + ")->(" + end[0] + "," + end[1]);

                    if (isInCheck(currBoard, white_turn)) {
                        if (!canEscapeCheck(currBoard, white_turn)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            if (!white_turn) {
                                builder.setTitle("Checkmate, White wins! ");
                                builder.setIcon(R.drawable.ic_king_white);
                            } else {
                                builder.setTitle("Checkmate, Black wins! ");
                                builder.setIcon(R.drawable.ic_king_black);
                            }
                            builder.setMessage("\nSave the game recording?\n\n\nGame title:");
                            recName = new EditText(MainActivity.this);


                            builder.setView(recName);
                            recName.setHint("     type here");
                            recName.setHintTextColor(Color.rgb(213, 213, 213));

                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String name = recName.getText().toString();
                                    recordingName = name;

                                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);

                                    if (name.length() < 1) {
                                        Context context = getApplicationContext();
                                        CharSequence text1 = "Empty title, auto-generated one";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text1, duration);
                                        toast.show();

                                        name = "chessRecording";
                                    }

                                    Recording rec = null;
                                    if (!white_turn) {
                                        rec = new Recording(name, gameStack, Recording.Outcome.WHITE_WIN_MATE);
                                    } else {
                                        rec = new Recording(name, gameStack, Recording.Outcome.BLACK_WIN_MATE);
                                    }

                                    //String myDate = sdf.format(rec.getDate());

                                    //dialog.dismiss();

                                    //Log.d(TAG,"new recording bt to be added = " + rec.toString());

                                    recordingList.add(rec);

                                    saveRecordings(getApplicationContext());
                                    Context context = getApplicationContext();
                                    CharSequence text1 = "Recording saved";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text1, duration);
                                    toast.show();

                                    beginAnew();

                                }
                            });
                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    beginAnew();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                public void onCancel(DialogInterface dialog) {
                                    beginAnew();
                                }
                            });

                            dialog.setCanceledOnTouchOutside(true);
                            dialog.show();
                            gameover = true;
                        }
                        if (!gameover) {
                            if (white_turn) {
                                title.setText(R.string.checkW);
                            } else {
                                title.setText(R.string.checkB);
                            }
                        }


                    }
                    justDidUndo = false;
                    return;

                }
            }


        }



    }




    public static boolean canEscapeCheck(Board cboard, boolean white_turn) {

        String color;
        if (white_turn) { color = "white"; }
        else { color = "black"; }

        Board chessboard = backupBoard(cboard);
        enpassant_b = enpassant;
        for (int h = 0; h < 6; h++) { enpassloc_b[h] = enpassloc[h]; }


        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                if (!chessboard.positions[r][c].getColor().equals(color)) { continue; }

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {

                        Piece hero = chessboard.positions[r][c];  //Candidate piece that may get the player out of check
                        if (hero.legalMove(chessboard, new int[] {i,j})) {


                            Board backup_board = backupBoard(chessboard);

                            enpassant_b = enpassant;
                            for (int h = 0; h < 6; h++) { enpassloc_b[h] = enpassloc[h]; }

                            hero.move(chessboard, new int[] {i,j});
                            if (enpasscap) {
                                Pawn.enpassCapture(chessboard, enpasstarg[0], enpasstarg[1]);
                                enpasstarg[0] = -1;
                                enpasstarg[1] = -1;
                                enpasscap = false;
                            }


                            if (!isInCheck(chessboard, white_turn)) {

                                chessboard = backup_board;
                                enpassant = enpassant_b;
                                for (int h = 0; h < 6; h++) { enpassloc[h] = enpassloc_b[h]; }

                                return true;
                            }
                            chessboard = backup_board;
                            enpassant = enpassant_b;
                            for (int h = 0; h < 6; h++) { enpassloc[h] = enpassloc_b[h]; }
                        }

                    }
                }

            }
        }



        return false;

    }

    /**
     * Returns whether or not the indicated player is currently in check by finding the king's position
     * on the board and seeing if that coordinate is being threatened by an enemy piece.
     *
     * @param chessboard 	current board storing all piece information
     * @param white_turn 	<code>true</code> if it is white's turn; <code>false</code> if it is black's turn.
     * @return 				<code>false</code> if the player indicated by <code>white_turn</code>
     *  					is in check; <code>true</code> otherwise.
     * @see Board
     */
    public static boolean isInCheck(Board chessboard, boolean white_turn) {

        String kingname = "";
        String opponent = "";
        Piece[][] board = chessboard.positions;
        if (white_turn) {
            kingname = "wK";
            opponent = "black";
        }
        else {
            kingname = "bK";
            opponent = "white";
        }
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c].getName().equals(kingname)) {
                    if (board[r][c].inDanger(chessboard, opponent)) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }
        }

        return false;

    }

    /**
     * Saves the current state of the given board and returns it, creating a duplicate.
     *
     * @param boardA		board to be duplicated
     * @return				<code>Board</code> instance with the same pieces and positions as the given board
     * @see Board
     */
    public static Board backupBoard(Board boardA) {

        Board boardB = new Board(8);
        //boardB.ID = (int) (Math.random() * 40000.0);
        Piece[][] posA = boardA.positions;
        Piece[][] posB = boardB.positions;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = posA[r][c];
                if (p instanceof Pawn) { posB[r][c] = new Pawn(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}, ((Pawn)p).getFirstMoveFlag()); }
                else if (p instanceof Rook) { posB[r][c] = new Rook(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}, ((Rook)p).getHasMoved()); }
                else if (p instanceof Knight) { posB[r][c] = new Knight(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}); }
                else if (p instanceof Bishop) { posB[r][c] = new Bishop(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}); }
                else if (p instanceof Queen) { posB[r][c] = new Queen(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}); }
                else if (p instanceof King) { posB[r][c] = new King(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}, ((King)p).getHasMoved());  }
                else if (p instanceof Blank) { posB[r][c] = new Blank(new int[] {p.getCoords()[0], p.getCoords()[1]}); }
                else { }
            }
        }

        return boardB;

    }

    protected void onRecreate(Bundle savedInstanceState) {
        Log.d(TAG,"did an onRecreate");
        saveRecordings(getApplicationContext());
    }

    protected void onDestroy(Bundle savedInstanceState) {
        Log.d(TAG,"did an onRecreate");
        saveRecordings(getApplicationContext());
    }

}