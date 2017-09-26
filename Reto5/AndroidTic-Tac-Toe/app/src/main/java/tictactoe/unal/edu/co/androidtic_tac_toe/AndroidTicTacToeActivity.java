package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.harding.tictactoe.BoardView;
import edu.harding.tictactoe.TicTacToeGame;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT = 2;

    private TicTacToeGame gameStatus;
    private Button boardButtons[];
    private TextView infoTextView;
    private boolean gameOver;

    private int humanWins, computerWins, ties;
    private TextView humanTextView, computerTextView, tiesTextView;

    private BoardView boardView;

    private boolean humanStarts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_tic_tac_toe);

        gameStatus = new TicTacToeGame();

        boardView = (BoardView) findViewById(R.id.board);
        boardView.setGame(gameStatus);


        boardButtons = new Button[9];

        /*
        boardButtons[0] = (Button) findViewById(R.id.one);
        boardButtons[1] = (Button) findViewById(R.id.two);
        boardButtons[2] = (Button) findViewById(R.id.three);
        boardButtons[3] = (Button) findViewById(R.id.four);
        boardButtons[4] = (Button) findViewById(R.id.five);
        boardButtons[5] = (Button) findViewById(R.id.six);
        boardButtons[6] = (Button) findViewById(R.id.seven);
        boardButtons[7] = (Button) findViewById(R.id.eight);
        boardButtons[8] = (Button) findViewById(R.id.nine);
        */
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
            case R.id.about:
                showDialog(DIALOG_ABOUT);
                return true;
        }
        return false;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_DIFFICULTY_ID:

                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.
                int selected = 0; // easy by default
                if(gameStatus.getDifficultyLevel() == TicTacToeGame.DifficultyLevel.Harder)
                    selected = 1;
                if(gameStatus.getDifficultyLevel() == TicTacToeGame.DifficultyLevel.Expert)
                    selected = 2;

                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog

                                // TODO: Set the diff level of mGame based on which item was selected.
                                if(item == 0) gameStatus.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                if(item == 1) gameStatus.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                if(item == 2) gameStatus.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);

                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();

                break;
            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog

                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AndroidTicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();

                break;
            case DIALOG_ABOUT:
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();
        }


        return dialog;
    }


    private void updateStatistics() {
        humanTextView.setText( humanWins + "" );
        tiesTextView.setText( ties + "" );
        computerTextView.setText( computerWins + "" );
    }


    private void startNewGame() {
        gameStatus.clearBoard();
        boardView.invalidate();   // Redraw the board

        gameOver = false;
        // reset to initial values
        for(int i = 0; i < boardButtons.length; i++) {
            //boardButtons[i].setText("");
            //boardButtons[i].setEnabled(true);
            //boardButtons[i].setOnClickListener(new ButtonClickListener(i));
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

    /*

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
    */


}