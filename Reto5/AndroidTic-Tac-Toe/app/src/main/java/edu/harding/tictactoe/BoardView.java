package edu.harding.tictactoe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import tictactoe.unal.edu.co.androidtic_tac_toe.R;

public class BoardView extends View {
    // Width of the board grid lines
    public static final int GRID_WIDTH = 6;

    private Bitmap humanBitmap;
    private Bitmap computerBitmap;

    private Paint paint;

    public void initialize() {
        //humanBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x_img);
        //computerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.o_img);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public BoardView(Context context) {
        super(context);
        initialize();
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private TicTacToeGame gameStatus;

    public void setGame(TicTacToeGame game) {
        gameStatus = game;
    }

    public int getBoardCellWidth() {
        return getWidth() / 3;
    }

    public int getBoardCellHeight() {
        return getHeight() / 3;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Determine the width and height of the View
        int boardWidth = getWidth();
        int boardHeight = getHeight();

        // Make thick, light gray lines
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(GRID_WIDTH);

        // Draw the two vertical board lines

        int cellWidth = boardWidth / 3;
        canvas.drawLine(cellWidth, 0, cellWidth, boardHeight, paint);
        canvas.drawLine(cellWidth * 2, 0, cellWidth * 2, boardHeight, paint);

        int cellHeight = boardHeight / 3;
        canvas.drawLine(0, cellHeight, boardWidth, cellHeight, paint);
        canvas.drawLine(0, 2*cellHeight, boardWidth, 2*cellHeight, paint);

        // Draw all the X and O images
        for (int i = 0; i < TicTacToeGame.BOARD_SIZE; i++) {
            for (int j = 0; j < TicTacToeGame.BOARD_SIZE; j++) {
                int col = i;
                int row = j;

                // Define the boundaries of a destination rectangle for the image
                int left = i*cellWidth;
                int top = j*cellHeight;
                int right = (i+1)*cellWidth;
                int bottom = (j+1)*cellHeight;

                if (gameStatus != null && gameStatus.getBoardOccupant(i) == TicTacToeGame.HUMAN_PLAYER) {
                    canvas.drawBitmap(humanBitmap,
                            null,  // src
                            new Rect(left, top, right, bottom),  // dest
                            null);
                } else if (gameStatus!= null && gameStatus.getBoardOccupant(i) == TicTacToeGame.COMPUTER_PLAYER) {
                    canvas.drawBitmap(computerBitmap,
                            null,  // src
                            new Rect(left, top, right, bottom),  // dest
                            null);
                }
            }
        }



    }
}
