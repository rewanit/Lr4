package com.example.lr4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TicTacToeActivity extends AppCompatActivity {

    private TicTacToeView ticTacToeView;
    private TextView gameStatusTextView;
    private Button resetButton;

    private MediaPlayer winSound;
    private MediaPlayer drawSound;
    private MediaPlayer buttonPressSound;


    private int[][] board;
    private int currentPlayer;
    private boolean gameEnded;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (winSound != null) {
            winSound.release();
        }
        if (drawSound != null) {
            drawSound.release();
        }
        if (buttonPressSound != null) {
            buttonPressSound.release();
        }


    }

    private boolean isSoundEnabled() {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        return preferences.getBoolean("sounds_enabled", true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        ticTacToeView = findViewById(R.id.ticTacToeView);
        gameStatusTextView = findViewById(R.id.gameStatusTextView);
        resetButton = findViewById(R.id.resetButton);
        winSound = MediaPlayer.create(this, R.raw.win_sound);
        drawSound = MediaPlayer.create(this, R.raw.draw_sound);
        buttonPressSound = MediaPlayer.create(this, R.raw.button_press_sound);

        initializeGame();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeGame();
                ticTacToeView.resetBoard();
                ticTacToeView.setWinLine(-1,-1,-1,-1);
                ticTacToeView.invalidate();
            }
        });

        ticTacToeView.setOnMoveListener(new TicTacToeView.OnMoveListener() {
            @Override
            public void onMove(int row, int col) {
                if (!gameEnded && isValidMove(row, col)) {
                    ticTacToeView.getBoard()[row][col] = currentPlayer;
                    board = ticTacToeView.getBoard();
                    if (isSoundEnabled())buttonPressSound.start();
                    ticTacToeView.invalidate();

                    if (checkWin(row, col, currentPlayer)) {
                        gameEnded = true;
                        gameStatusTextView.setText(currentPlayer == TicTacToeView.X_PLAYER ?
                                "X Победили!" : "O Победили!");

                        int startX, startY, endX, endY;

                        // Проверка горизонтальной линии
                        if (board[row][0] == currentPlayer &&
                                board[row][1] == currentPlayer &&
                                board[row][2] == currentPlayer) {
                            startX = 0;
                            startY = (int)(row * ticTacToeView.getCellHeight() + ticTacToeView.getCellHeight() / 2);
                            endX = ticTacToeView.getWidth();
                            endY = startY;
                        }
                        // Проверка вертикальной линии
                        else if (board[0][col] == currentPlayer &&
                                board[1][col] == currentPlayer &&
                                board[2][col] == currentPlayer) {
                            startX = (int)(col * ticTacToeView.getCellWidth() + ticTacToeView.getCellWidth() / 2);
                            startY = 0;
                            endX = startX;
                            endY = ticTacToeView.getHeight();
                        }
                        // Проверка диагональной линии (слева-направо)
                        else if (row == col &&
                                board[0][0] == currentPlayer &&
                                board[1][1] == currentPlayer &&
                                board[2][2] == currentPlayer) {
                            startX = 0;
                            startY = 0;
                            endX = ticTacToeView.getWidth();
                            endY = ticTacToeView.getHeight();
                        }
                        // Проверка диагональной линии (справа-налево)
                        else if (row + col == 2 &&
                                board[0][2] == currentPlayer &&
                                board[1][1] == currentPlayer &&
                                board[2][0] == currentPlayer) {
                            startX = ticTacToeView.getWidth();
                            startY = 0;
                            endX = 0;
                            endY = ticTacToeView.getHeight();
                        }
                        // Если не победа, то установите координаты за пределами видимой области
                        else {
                            startX = -1;
                            startY = -1;
                            endX = -1;
                            endY = -1;
                        }
                        ticTacToeView.setWinLine(startX, startY, endX, endY);
                        if (isSoundEnabled())winSound.start();
                    } else if (isBoardFull()) {
                        gameEnded = true;
                        gameStatusTextView.setText("Ничья!");
                        if (isSoundEnabled())drawSound.start();
                    } else {
                        currentPlayer = (currentPlayer == TicTacToeView.X_PLAYER) ?
                                TicTacToeView.O_PLAYER : TicTacToeView.X_PLAYER;
                        updateGameStatus();
                        ticTacToeView.setCurrentPlayer(currentPlayer);
                    }
                }
            }
        });

    }


    private void initializeGame() {
        board = new int[TicTacToeView.GRID_SIZE][TicTacToeView.GRID_SIZE];
        currentPlayer = TicTacToeView.X_PLAYER;
        gameEnded = false;

        gameStatusTextView.setText("Ходят X");

        ticTacToeView.resetBoard();
        ticTacToeView.invalidate();
    }

    private boolean isValidMove(int row, int col) {
        return (row >= 0 && row < TicTacToeView.GRID_SIZE &&
                col >= 0 && col < TicTacToeView.GRID_SIZE &&
                board[row][col] == TicTacToeView.EMPTY);
    }

    private boolean checkWin(int row, int col, int player) {
        for (int i = 0; i < TicTacToeView.GRID_SIZE; i++) {
            if (board[row][i] != player) {
                break;
            }
            if (i == TicTacToeView.GRID_SIZE - 1) {


                return true;
            }
        }

        for (int i = 0; i < TicTacToeView.GRID_SIZE; i++) {
            if (board[i][col] != player) {
                break;
            }
            if (i == TicTacToeView.GRID_SIZE - 1) {
                return true;
            }
        }

        if (row == col) {
            for (int i = 0; i < TicTacToeView.GRID_SIZE; i++) {
                if (board[i][i] != player) {
                    break;
                }
                if (i == TicTacToeView.GRID_SIZE - 1) {
                    return true;
                }
            }
        }

        if (row + col == TicTacToeView.GRID_SIZE - 1) {
            for (int i = 0; i < TicTacToeView.GRID_SIZE; i++) {
                if (board[i][TicTacToeView.GRID_SIZE - 1 - i] != player) {
                    break;
                }
                if (i == TicTacToeView.GRID_SIZE - 1) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < TicTacToeView.GRID_SIZE; row++) {
            for (int col = 0; col < TicTacToeView.GRID_SIZE; col++) {
                if (board[row][col] == TicTacToeView.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateGameStatus() {
        gameStatusTextView.setText(currentPlayer == TicTacToeView.X_PLAYER ?
                "Ходят X" : "Ходят 0");
    }
}
