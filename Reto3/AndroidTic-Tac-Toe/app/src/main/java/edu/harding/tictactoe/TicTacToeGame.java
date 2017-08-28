package edu.harding.tictactoe;

/* TicTacToeConsole.java
 * By Frank McCown (Harding University)
 *
 * This is a tic-tac-toe game that runs in the console window.  The human
 * is X and the computer is O.
 */

import java.util.Random;

public class TicTacToeGame {

    private final int BOARD_SIZE = 3;

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';
    //                               R  D  RD  RU
    private static final int DR[] = {0, 1, 1, -1};
    private static final int DC[] = {1, 0, 1, 1};

    private Random rand;
    private int leftMoves;

    private char[][] board;

    public TicTacToeGame() {
        // Seed the random number generator
        rand = new Random();

        board = new char[BOARD_SIZE][BOARD_SIZE];
        clearBoard();
    }

    /** Clear the board of all X's and O's by setting all spots to OPEN_SPOT. */
    public void clearBoard() {
        leftMoves = BOARD_SIZE * BOARD_SIZE;
        for(int i = 0; i < BOARD_SIZE; i++)
            for(int j = 0; j < BOARD_SIZE; j++)
                board[i][j] = OPEN_SPOT;
    }
    /** Set the given player at the given location on the game board.
     * The location must be available, or the board will not be changed.
     *
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    public void setMove(char player, int location) {
        leftMoves--;
        int row = location / BOARD_SIZE, col = location % BOARD_SIZE;
        board[row][col] = player;
    }
    /** Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */

    private boolean checkComputerWins(int row, int col) {
        board[row][col] = COMPUTER_PLAYER;
        boolean result = false;
        if(checkForWinner() == 3) result = true;
        board[row][col] = OPEN_SPOT;
        return result;
    }

    private boolean checkHumanWins(int row, int col) {
        board[row][col] = HUMAN_PLAYER;
        boolean result = false;
        if(checkForWinner() == 2) result = true;
        board[row][col] = OPEN_SPOT;
        return result;
    }

    private int getLocation(int row, int col) {
        return row * BOARD_SIZE + col;
    }

    public int getComputerMove() {
        // checking if computer can win
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j] == OPEN_SPOT) {
                    if(checkComputerWins(i, j))
                        return getLocation(i, j);
                }
            }
        }
        // checking if human can win
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j] == OPEN_SPOT) {
                    if(checkHumanWins(i, j))
                        return getLocation(i, j);
                }
            }
        }
        int row, col;
        do{
            row = rand.nextInt(BOARD_SIZE);
            col = rand.nextInt(BOARD_SIZE);
        } while(board[row][col] != OPEN_SPOT);

        return getLocation(row, col);
    }
    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     * */

    private boolean check(int r, int c) {
        return r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE;
    }

    public int checkForWinner() {
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j] == OPEN_SPOT) continue;
                for(int dir = 0; dir < 4; dir++) {
                    int counter = 0;
                    int nr = i, nc  = j;
                    for(int k = 0; k < BOARD_SIZE && check(nr, nc); k++) {
                        if(board[i][j] == board[nr][nc])
                            counter++;
                        nr += DR[dir];
                        nc += DC[dir];
                    }
                    if(counter == BOARD_SIZE) {
                        return ( board[i][j] == HUMAN_PLAYER ? 2 : 3 );
                    }
                }
            }
        }
        if(leftMoves == 0) return 1; // i'ts a tie
        return 0; // still playing
    }



}