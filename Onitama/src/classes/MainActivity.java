// Created by Sean Gowen. The game rules and art for Onitama are not my property and are the property of Arcane Wonders. This version of this game is not for sale or distribution and is intended solely for personal use.

package com.example.android.onitamaclone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    int[] cellViews = {R.id.zero_zero, R.id.zero_one, R.id.zero_two, R.id.zero_three, R.id.zero_four,
        R.id.one_zero, R.id.one_one, R.id.one_two, R.id.one_three, R.id.one_four,
        R.id.two_zero, R.id.two_one, R.id.two_two, R.id.two_three, R.id.two_four,
        R.id.three_zero, R.id.three_one, R.id.three_two, R.id.three_three, R.id.three_four,
        R.id.four_zero, R.id.four_one, R.id.four_two, R.id.four_three, R.id.four_four
        };

    MoveCard p1Opt1;
    MoveCard p1Opt2;
    MoveCard p2Opt1;
    MoveCard p2Opt2;
    MoveCard p1Next;
    MoveCard p2Next;
    MoveCard p1Play;
    MoveCard p2Play;

    ImageButton p1opt1Butt;
    ImageButton p1opt2Butt;
    ImageButton p2opt1Butt;
    ImageButton p2opt2Butt;

    ImageView p1NextView;
    ImageButton p1PlayView;
    ImageView p2NextView;
    ImageButton p2PlayView;

    CheckBox basegame;
    CheckBox expansion;

    Button reset1;
    Button reset2;

    int playerTurn;

    boolean win = false;

    boolean moveChosen = false;

    int[] movePlayer;

    GridCell[][] gameBoard = new GridCell[5][5];
    ArrayList<MoveCard> cardStack = new ArrayList<MoveCard>(16);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        basegame = findViewById(R.id.basegame);
        basegame.setChecked(true);
        expansion = findViewById(R.id.senseispath);


        p1opt1Butt = findViewById(R.id.p1option1);
        p1opt1Butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseMove(1);
            }
        });

        p1opt2Butt = findViewById(R.id.p1option2);
        p1opt2Butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseMove(2);
            }
        });

        p2opt1Butt = findViewById(R.id.p2option1);
        p2opt1Butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseMove(3);
            }
        });

        p2opt2Butt = findViewById(R.id.p2option2);
        p2opt2Butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseMove(4);
            }
        });

        p1NextView = findViewById(R.id.p1nextcard);
        p1PlayView = findViewById(R.id.p1playcard);
        p2NextView = findViewById(R.id.p2nextcard);
        p2PlayView = findViewById(R.id.p2playcard);

        p1PlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseMove(5);
            }
        });

        p2PlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseMove(6);
            }
        });

        reset1 = findViewById(R.id.reset_1);
        reset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( basegame.isChecked() || expansion.isChecked() ) {
                    initializeGame();
                }

            }
        });

        reset2 = findViewById(R.id.reset_2);
        reset2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( basegame.isChecked() || expansion.isChecked() ) {
                    initializeGame();
                }
            }
        });

        for(int m = 0 ; m < 5 ; m++) {
            for(int k =0; k < 5; k++){
                gameBoard[m][k] = (GridCell) findViewById(cellViews[m*5+k]);
                gameBoard[m][k].setBackgroundResource(R.drawable.blankcell);
                final int nowm = m;
                final int nowk = k;
                gameBoard[m][k].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gridPress(nowm, nowk);
                    }
                });
            }

        }

        initializeGame();

    }

    public void initializeGame() {
        cardStack.clear();
        moveChosen = false;
        movePlayer = null;
        win = false;

        if (basegame.isChecked()) {
            if (expansion.isChecked()) {
                for (int i = 0; i < 32 ; i++) {
                    cardStack.add(new MoveCard(i + 1));
                }
            } else {
                for (int i = 0; i < 16 ; i++) {
                    cardStack.add(new MoveCard(i + 1));
                }
            }
        } else {
            if (expansion.isChecked()) {
                for (int i = 16; i < 32 ; i++) {
                    cardStack.add(new MoveCard(i + 1));
                }
            }
        }

        Collections.shuffle(cardStack);

        for (int i = 0; i < 5 ; i++) {
            for (int j = 0; j < 5 ; j++){
                gameBoard[i][j].emptyCell();
            }
        }

        unselectAll();

        gameBoard[0][0].placePiece(2,GridCell.PAWN);
        gameBoard[0][1].placePiece(2,GridCell.PAWN);
        gameBoard[0][2].placePiece(2,GridCell.MASTER);
        gameBoard[0][3].placePiece(2,GridCell.PAWN);
        gameBoard[0][4].placePiece(2,GridCell.PAWN);

        gameBoard[4][0].placePiece(1,GridCell.PAWN);
        gameBoard[4][1].placePiece(1,GridCell.PAWN);
        gameBoard[4][2].placePiece(1,GridCell.MASTER);
        gameBoard[4][3].placePiece(1,GridCell.PAWN);
        gameBoard[4][4].placePiece(1,GridCell.PAWN);

        p1Opt1 = cardStack.get(0);
        p1Opt2 = cardStack.get(1);
        p2Opt1 = cardStack.get(2);
        p2Opt2 = cardStack.get(3);

        p1opt1Butt.setBackgroundResource(p1Opt1.getBG());
        p1opt2Butt.setBackgroundResource(p1Opt2.getBG());
        p2opt1Butt.setBackgroundResource(p2Opt1.getBG());
        p2opt2Butt.setBackgroundResource(p2Opt2.getBG());

        p1PlayView.setBackgroundResource(R.drawable.redcard);
        p2PlayView.setBackgroundResource(R.drawable.bluecard);
        p1NextView.setBackgroundResource(R.drawable.blank);
        p2NextView.setBackgroundResource(R.drawable.blank);


        MoveCard next = cardStack.get(4);
        if(next.whichColor() == MoveCard.BLUE) {
            p2Next = next;
            p2NextView.setBackgroundResource(p2Next.getBGinv());
            playerTurn = 2;
        }
        if(next.whichColor() == MoveCard.RED) {
            p1Next = next;
            p1NextView.setBackgroundResource(p1Next.getBGinv());
            playerTurn = 1;
        }

    }

    public void chooseMove(int button){
        if( !win ) {
            movePlayer = null;
            if(playerTurn == 1 && button == 1 ) {
                if ( p1Play == null ) {
                    unselectAll();
                    p1Play = p1Opt1;
                    p1Opt1 = null;
                    p1PlayView.setBackgroundResource(p1Play.getBG());
                    p1opt1Butt.setBackgroundResource(R.drawable.blank);
                    moveChosen = true;
                } else {
                    if (p1Opt2 == null) {
                        unselectAll();
                        p1Opt2 = p1Play;
                        p1opt2Butt.setBackgroundResource(p1Opt2.getBG());
                        p1Play = p1Opt1;
                        p1Opt1 = null;
                        p1PlayView.setBackgroundResource(p1Play.getBG());
                        p1opt1Butt.setBackgroundResource(R.drawable.blank);
                        moveChosen = true;
                    }
                }

            }
            if(playerTurn == 1 && button == 2 ) {
                if ( p1Play == null ) {
                    unselectAll();
                    p1Play = p1Opt2;
                    p1Opt2 = null;
                    p1PlayView.setBackgroundResource(p1Play.getBG());
                    p1opt2Butt.setBackgroundResource(R.drawable.blank);
                    moveChosen = true;
                } else {
                    if (p1Opt1 == null) {
                        unselectAll();
                        p1Opt1 = p1Play;
                        p1opt1Butt.setBackgroundResource(p1Opt1.getBG());
                        p1Play = p1Opt2;
                        p1Opt2 = null;
                        p1PlayView.setBackgroundResource(p1Play.getBG());
                        p1opt2Butt.setBackgroundResource(R.drawable.blank);
                        moveChosen = true;

                    }
                }

            }
            if(playerTurn == 2 && button == 3 ) {
                if ( p2Play == null ) {
                    unselectAll();
                    p2Play = p2Opt1;
                    p2Opt1 = null;
                    p2PlayView.setBackgroundResource(p2Play.getBG());
                    p2opt1Butt.setBackgroundResource(R.drawable.blank);
                    moveChosen = true;
                } else {
                    if (p2Opt2 == null) {
                        unselectAll();
                        p2Opt2 = p2Play;
                        p2opt2Butt.setBackgroundResource(p2Opt2.getBG());
                        p2Play = p2Opt1;
                        p2Opt1 = null;
                        p2PlayView.setBackgroundResource(p2Play.getBG());
                        p2opt1Butt.setBackgroundResource(R.drawable.blank);
                        moveChosen = true;
                    }
                }
            }
            if(playerTurn == 2 && button == 4 ) {
                if ( p2Play == null ) {
                    unselectAll();
                    p2Play = p2Opt2;
                    p2Opt2 = null;
                    p2PlayView.setBackgroundResource(p2Play.getBG());
                    p2opt2Butt.setBackgroundResource(R.drawable.blank);
                    moveChosen = true;
                } else {
                    if (p2Opt1 == null) {
                        unselectAll();
                        p2Opt1 = p2Play;
                        p2opt1Butt.setBackgroundResource(p2Opt1.getBG());
                        p2Play = p2Opt2;
                        p2Opt2 = null;
                        p2PlayView.setBackgroundResource(p2Play.getBG());
                        p2opt2Butt.setBackgroundResource(R.drawable.blank);
                        moveChosen = true;

                    }
                }
            }

            if (playerTurn == 1 && button == 5 && p1Play != null) {
                unselectAll();
                if (p1Opt1 == null) {
                    p1Opt1 = p1Play;
                    p1Play = null;
                    p1PlayView.setBackgroundResource(R.drawable.redcard);
                    p1opt1Butt.setBackgroundResource(p1Opt1.getBG());
                } else{
                    p1Opt2 = p1Play;
                    p1Play = null;
                    p1PlayView.setBackgroundResource(R.drawable.redcard);
                    p1opt2Butt.setBackgroundResource(p1Opt2.getBG());
                }
                moveChosen = false;
            }

            if (playerTurn == 2 && button == 6 && p2Play != null) {
                unselectAll();
                if (p2Opt1 == null) {
                    p2Opt1 = p2Play;
                    p2Play = null;
                    p2PlayView.setBackgroundResource(R.drawable.bluecard);
                    p2opt1Butt.setBackgroundResource(p2Opt1.getBG());
                } else{
                    p2Opt2 = p2Play;
                    p2Play = null;
                    p2PlayView.setBackgroundResource(R.drawable.bluecard);
                    p2opt2Butt.setBackgroundResource(p2Opt2.getBG());
                }
                moveChosen = false;
            }
        }
    }

    public void gridPress(int row, int col) {
        if (moveChosen) {
            if (playerTurn == gameBoard[row][col].getP1P2()) {
                unselectAll();
                showLegalMoves(row,col);
            }
            if (gameBoard[row][col].isSelectable()) {
                if (gameBoard[row][col].isOccupied && gameBoard[row][col].isMaster()) win = true;
                if (gameBoard[row][col].getIsPlatform() && playerTurn != gameBoard[row][col].whichPlatform && gameBoard[movePlayer[0]][movePlayer[1]].isMaster()) win = true;
                gameBoard[row][col].placePiece(playerTurn,gameBoard[movePlayer[0]][movePlayer[1]].getType());
                gameBoard[movePlayer[0]][movePlayer[1]].emptyCell();
                unselectAll();
                movePlayer = null;
                giveCards();
                moveChosen = false;
                if (win) {
                    if (playerTurn == 1) {
                        for ( int v = 0; v < 5; v++){
                            for( int m = 0; m < 5; m++){
                                if(gameBoard[v][m].getP1P2() == 1 ) gameBoard[v][m].emptyCell();
                            }
                        }
                    }
                    if (playerTurn == 2) {
                        for ( int v = 0; v < 5; v++){
                            for( int m = 0; m < 5; m++){
                                if(gameBoard[v][m].getP1P2() == 2 ) gameBoard[v][m].emptyCell();
                            }
                        }
                    }
                    p1Opt1 = null;
                    p1Opt2 = null;
                    p2Opt1 = null;
                    p2Opt2 = null;
                    p1Next = null;
                    p2Next = null;
                    p1Play = null;
                    p2Play = null;

                    p1opt1Butt.setBackgroundResource(R.drawable.blank);
                    p1opt2Butt.setBackgroundResource(R.drawable.blank);
                    p2opt1Butt.setBackgroundResource(R.drawable.blank);
                    p2opt2Butt.setBackgroundResource(R.drawable.blank);

                    p1PlayView.setBackgroundResource(R.drawable.redcard);
                    p2PlayView.setBackgroundResource(R.drawable.bluecard);
                    p1NextView.setBackgroundResource(R.drawable.blank);
                    p2NextView.setBackgroundResource(R.drawable.blank);

                }
            }
        }
    }

    public void showLegalMoves(int row, int col) {

        movePlayer = new int[]{row,col};
        MoveCard cardChosen = null;
        GridCell checkHere;
        int rowCheck = -1;
        int colCheck = -1;
        if ( playerTurn == 1 ) {
            cardChosen = p1Play;
        }
        if ( playerTurn == 2 ) {
            cardChosen = p2Play;
        }
        int[] moveRow = cardChosen.getRow();
        int[] moveCol = cardChosen.getCol();
        for (int spot = 0; spot < 4 ; spot++){
            if ( playerTurn == 1 ) {
                rowCheck = row - moveRow[spot];
                colCheck = col + moveCol[spot];
            }
            if ( playerTurn == 2 ){
                rowCheck = row + moveRow[spot];
                colCheck = col - moveCol[spot];
            }
            if ((rowCheck > -1 && rowCheck < 5) && (colCheck > -1 && colCheck < 5)) {
                checkHere = gameBoard[rowCheck][colCheck];
                if (checkHere.getP1P2() != playerTurn) {
                    checkHere.makeSelectable();
                }
            }

        }
    }

    public void unselectAll() {
        for (int i = 0; i < 5 ; i++) {
            for (int j = 0; j < 5 ; j++){
                gameBoard[i][j].unSelectable();
            }
        }
    }

    public void giveCards() {
        if (playerTurn == 1) {
            p2Next = p1Play;
            p1Play = null;
            p2NextView.setBackgroundResource(p2Next.getBGinv());
            p1PlayView.setBackgroundResource(R.drawable.redcard);
            if (p1Opt1 == null) {
                p1Opt1 = p1Next;
                p1Next = null;
                p1opt1Butt.setBackgroundResource(p1Opt1.getBG());
                p1NextView.setBackgroundResource(R.drawable.blank);
            }
            if (p1Opt2 == null) {
                p1Opt2 = p1Next;
                p1Next = null;
                p1opt2Butt.setBackgroundResource(p1Opt2.getBG());
                p1NextView.setBackgroundResource(R.drawable.blank);
            }
            playerTurn = 2;
        } else {
            p1Next = p2Play;
            p2Play = null;
            p1NextView.setBackgroundResource(p1Next.getBGinv());
            p2PlayView.setBackgroundResource(R.drawable.bluecard);
            if (p2Opt1 == null) {
                p2Opt1 = p2Next;
                p2Next = null;
                p2opt1Butt.setBackgroundResource(p2Opt1.getBG());
                p2NextView.setBackgroundResource(R.drawable.blank);
            }
            if (p2Opt2 == null) {
                p2Opt2 = p2Next;
                p2Next = null;
                p2opt2Butt.setBackgroundResource(p2Opt2.getBG());
                p2NextView.setBackgroundResource(R.drawable.blank);
            }
            playerTurn = 1;
        }

    }

}
