package com.example.omryh.tictactoe;

import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    // 0: vitas, 1: naomi, 2: empty
    int activePlayer = 0;
    boolean gameActive = true;
    boolean isWin = false;
    boolean isRematch = false;
    String currentPlayerName;
    String winner = "";
    final Handler handler = new Handler();
    private GestureDetector mDetector;

    int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};


    public void dropin(View view) {
        ImageView counter = (ImageView) view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());
        TextView modeText = findViewById(R.id.modeText);
        TextView winnerText = findViewById(R.id.winnerText);

        if (gameState[tappedCounter] == 2 && gameActive) {
            gameState[tappedCounter] = activePlayer;
            counter.setTranslationY(-1500);
            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.vitas);
                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.naomi);
                activePlayer = 0;
            }
            counter.animate().translationYBy(1500).rotation(3600).setDuration(1000);
            setCurrentPlayerName(activePlayer);
            modeText.setText("Now Playing: " + currentPlayerName);
            for (int[] winningPosition : winningPositions) {
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {
                    // won
                    gameActive = false;
                    isWin = true;
                    if (activePlayer == 1) {
                        winner = "Vitas";
                    } else {
                        winner = "Naomi";
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Button playAgainBtn = (Button) findViewById(R.id.playAgainBtn);
                            playAgainBtn.setVisibility(View.VISIBLE);
                        }
                    }, 3000);
                    winnerText.setText(winner + " has won!");
                    //Toast.makeText(this, winner + " has won", Toast.LENGTH_LONG).show();
                    modeText.setVisibility(View.INVISIBLE);
                }
            }
        }
        // No one won
        if (!isWin && hasGameEnded() == true) {
            winnerText.setText("No cat has won");
            //Toast.makeText(this, "No cat has won. You lost.", Toast.LENGTH_LONG).show();
            Button playAgainBtn = (Button) findViewById(R.id.playAgainBtn);
            playAgainBtn.setVisibility(View.VISIBLE);
            modeText.setVisibility(View.INVISIBLE);
        }
    }

    public boolean hasGameEnded() {
        int count = 0;
        for (int i = 0; i < gameState.length; i++) {
            if (gameState[i] != 2) {
                count++;
            }
        }
        if (count == gameState.length) {
            return true;
        }
        return false;
    }

    public void resetGame(View view) {
        activePlayer = 0;
        gameActive = true;
        isWin = false;
        TextView winnerText = findViewById(R.id.winnerText);
        TextView modeText = findViewById(R.id.modeText);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        Button playAgainBtn = (Button) findViewById(R.id.playAgainBtn);

        playAgainBtn.setVisibility(View.INVISIBLE);
        winnerText.setText("");
        setCurrentPlayerName(activePlayer);
        modeText.setVisibility(View.VISIBLE);
        modeText.setText("Now Playing: " + currentPlayerName);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView counter = (ImageView) gridLayout.getChildAt(i);
            counter.animate().rotation(-3600);
            counter.setImageDrawable(null);
        }

        // reset game state array
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }
    }

    public void setCurrentPlayerName(int activePlayer) {
        currentPlayerName = activePlayer == 0 ? "Vitas" : "Naomi";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView modeText = findViewById(R.id.modeText);
        modeText.setVisibility(View.VISIBLE);
        setCurrentPlayerName(activePlayer);
        modeText.setText("Now Playing: " + currentPlayerName);

        View view = findViewById(R.id.tictactoeView);
        mDetector = new GestureDetector(this, new MyGestureListener());

        view.setOnTouchListener(touchListener);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            return mDetector.onTouchEvent(event);
        }
    };

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG", "onDown: ");

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("TAG", "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("TAG", "onDoubleTap: ");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i("TAG", "onScroll: ");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("TAG", "onFling: ");
            return true;
        }
    }
}
