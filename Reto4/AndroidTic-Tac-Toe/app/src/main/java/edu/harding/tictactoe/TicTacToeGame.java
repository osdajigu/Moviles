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

    public enum DifficultyLevel{Easy, Harder, Expert};

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';
    //                               R  D  RD  RU
    private static final int DR[] = {0, 1, 1, -1};
    private static final int DC[] = {1, 0, 1, 1};

    private Random rand;
    private int leftMoves;
    private DifficultyLevel difficultyLevel = DifficultyLevel.Expert;

    private char[][] board;

    public TicTacToeGame() {
        // Seed the random number generator
        rand = new Random();

        board = new char[BOARD_SIZE][BOARD_SIZE];
        clearBoard();
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel newDifficulty) {
        difficultyLevel = newDifficulty;
    }

    public void clearBoard() {
        leftMoves = BOARD_SIZE * BOARD_SIZE;
        for(int i = 0; i < BOARD_SIZE; i++)
            for(int j = 0; j < BOARD_SIZE; j++)
                board[i][j] = OPEN_SPOT;
    }

    public void setMove(char player, int location) {
        leftMoves--;
        int row = location / BOARD_SIZE, col = location % BOARD_SIZE;
        board[row][col] = player;
    }

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

    public int getWinningMove() {
        // checking if computer can win
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j] == OPEN_SPOT) {
                    if(checkComputerWins(i, j))
                        return getLocation(i, j);
                }
            }
        }
        return -1;
    }

    public int getBlockingMove() {
        // checking if human can win
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j] == OPEN_SPOT) {
                    if(checkHumanWins(i, j))
                        return getLocation(i, j);
                }
            }
        }
        return -1;
    }

    public int getRandomMove() {
        int row, col;
        do{
            row = rand.nextInt(BOARD_SIZE);
            col = rand.nextInt(BOARD_SIZE);
        } while(board[row][col] != OPEN_SPOT);

        return getLocation(row, col);
    }


    public int getComputerMove() {
        if(difficultyLevel == DifficultyLevel.Easy)
            return getRandomMove();
        if(difficultyLevel == DifficultyLevel.Harder) {
            int move = getWinningMove();
            if(move == -1) move = getRandomMove();
            return move;
        }
        /// expert
        int move = getWinningMove();
        if(move == -1) move = getBlockingMove();
        if(move == -1) move = getRandomMove();
        return move;
    }


    private boolean check(int r, int c) {
        return r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE;
    }
    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     * */
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