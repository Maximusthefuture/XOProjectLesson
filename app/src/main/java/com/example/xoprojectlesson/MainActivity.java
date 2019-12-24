package com.example.xoprojectlesson;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements XOCustomVIew.WinListener {
    private static final String TAG =  "MainActivity";

    private TicTacToeField ticTacToeField;
    private XOCustomVIew mXOCustomVIew;
    private TextView circleWin;
    private TextView xWin;
    private View result;
    private Button restart;
    private int circleScore = 0;
    private int xScore = 0;
    private TextView winnerText;


//    GestureDetector;
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {
        mXOCustomVIew = findViewById(R.id.xo_custom_view);
        circleWin = findViewById(R.id.circle_scores);
        xWin = findViewById(R.id.x_scores);
        restart = findViewById(R.id.button_restart);
        result = findViewById(R.id.restart_layout);
        winnerText = findViewById(R.id.winner_text_view);

        restart.setOnClickListener(v -> {
            restart.setVisibility(View.INVISIBLE);
            mXOCustomVIew.restart();
            winnerText.setVisibility(View.INVISIBLE);
        });
        mXOCustomVIew.setOnWinListener(this);

    }


    public void showScores(TicTacToeField.Figure scoreSide) {


        result.setVisibility(View.VISIBLE);

        switch (scoreSide) {
            case CIRCLE:
                winnerText.setText(R.string.circle_text);
                break;
            case CROSS:
                winnerText.setText(R.string.cross_text);
                break;
            case NONE:
                winnerText.setText(R.string.draw_figure_text);
                break;


        }
        showScores();
    }

    private void showScores() {
        circleWin.setText(Integer.toString(circleScore));
        xWin.setText(Integer.toString(xScore));
    }


    @Override
    public void onWin(TicTacToeField.Figure win) {
        if (win == TicTacToeField.Figure.CIRCLE) {
            circleScore++;
        } else {
            xScore++;
        }

        showScores(win);
    }
}
