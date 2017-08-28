package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.harding.tictactoe.TicTacToeGame;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    private TicTacToeGame gameStatus;
    private Button boardButtons[];
    private TextView infoTextView;
    private boolean gameOver;

    private int humanWins, computerWins, ties;
    private TextView humanTextView, computerTextView, tiesTextView;

    private boolean humanStarts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_tic_tac_toe);

        gameStatus = new TicTacToeGame();

        boardButtons = new Button[9];

        boardButtons[0] = (Button) findViewById(R.id.one);
        boardButtons[1] = (Button) findViewById(R.id.two);
        boardButtons[2] = (Button) findViewById(R.id.three);
        boardButtons[3] = (Button) findViewById(R.id.four);
        boardButtons[4] = (Button) findViewById(R.id.five);
        boardButtons[5] = (Button) findViewById(R.id.six);
        boardButtons[6] = (Button) findViewById(R.id.seven);
        boardButtons[7] = (Button) findViewById(R.id.eight);
        boardButtons[8] = (Button) findViewById(R.id.nine);

        infoTextView = (TextView) findViewById(R.id.information);

        humanTextView = (TextView) findViewById(R.id.count_human);
        tiesTextView = (TextView) findViewById(R.id.count_ties);
        computerTextView = (TextView) findViewById(R.id.count_computer);

        humanWins = computerWins = ties = 0;

        humanStarts = true;

        startNewGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startNewGame();
        return true;
    }

    private void updateStatistics() {
        humanTextView.setText( humanWins + "" );
        tiesTextView.setText( ties + "" );
        computerTextView.setText( computerWins + "" );
    }


    private void startNewGame() {
        gameStatus.clearBoard();

        gameOver = false;
        // reset to initial values
        for(int i = 0; i < boardButtons.length; i++) {
            boardButtons[i].setText("");
            boardButtons[i].setEnabled(true);
            boardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

        if(!humanStarts) {
            int move = gameStatus.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            infoTextView.setText(R.string.turn_human);
        } else {
            // Human goes first
            infoTextView.setText(R.string.first_human);
        }



        humanStarts = !humanStarts;
    }

    private void setMove(char player, int location) {
        gameStatus.setMove(player, location);
        boardButtons[location].setEnabled(false);
        boardButtons[location].setText(player+"");
        if(player == TicTacToeGame.HUMAN_PLAYER)
            boardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            boardButtons[location].setTextColor(Color.rgb(200, 0, 0));

    }

    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }

        @Override
        public void onClick(View v) {
            if(!gameOver && boardButtons[location].isEnabled()) {

                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                int winner = gameStatus.checkForWinner();
                if(winner == 0) {
                    infoTextView.setText(R.string.turn_computer);
                    int move = gameStatus.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = gameStatus.checkForWinner();
                }

                gameOver |= winner != 0;

                if (winner == 0)
                    infoTextView.setText(R.string.turn_human);
                else if (winner == 1) {
                    infoTextView.setText(R.string.result_tie);
                    ++ties;
                }
                else if (winner == 2) {
                    infoTextView.setText(R.string.result_human_wins);
                    ++humanWins;
                }
                else {
                    infoTextView.setText(R.string.result_computer_wins);
                    ++computerWins;
                }

                if(gameOver)
                    updateStatistics();
            }
        }
    }


}
