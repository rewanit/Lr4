package com.example.lr4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TicTacToeView extends View {

    public static final int GRID_SIZE = 3;
    public static final int EMPTY = 0;
    public static final int X_PLAYER = 1;
    public static final int O_PLAYER = 2;

    public int[][] board;
    public int currentPlayer;

    public Paint linePaint;
    public Paint xPaint;
    public Paint oPaint;
    public Paint wPaint;

    public TicTacToeView(Context context) {
        super(context);
        init();
    }

    public TicTacToeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private OnMoveListener onMoveListener;

    public interface OnMoveListener {
        void onMove(int row, int col);
    }

    public void setOnMoveListener(OnMoveListener listener) {
        this.onMoveListener = listener;
    }


    private void init() {
        board = new int[GRID_SIZE][GRID_SIZE];
        currentPlayer = X_PLAYER;

        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);

        xPaint = new Paint();
        xPaint.setColor(Color.RED);
        xPaint.setStrokeWidth(10);

        oPaint = new Paint();
        oPaint.setColor(Color.BLUE);
        oPaint.setStrokeWidth(10);
        oPaint.setStyle(Paint.Style.STROKE);

        wPaint = new Paint();
        wPaint.setColor(Color.BLACK);
        wPaint.setStrokeWidth(15);
        wPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);
        drawXsAndOs(canvas);
        if (hasWinLine) {
            // Зачеркнуть линию победы
            canvas.drawLine(
                    winLineStartX, winLineStartY, winLineEndX, winLineEndY, wPaint
            );
        }
    }

    private void drawXsAndOs(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float cellWidth = width / (float) GRID_SIZE;
        float cellHeight = height / (float) GRID_SIZE;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int cellValue = board[row][col];
                float centerX = col * cellWidth + cellWidth / 2;
                float centerY = row * cellHeight + cellHeight / 2;

                if (cellValue == X_PLAYER) {
                    canvas.drawLine(
                            centerX - cellWidth / 3,
                            centerY - cellHeight / 3,
                            centerX + cellWidth / 3,
                            centerY + cellHeight / 3,
                            xPaint
                    );
                    canvas.drawLine(
                            centerX + cellWidth / 3,
                            centerY - cellHeight / 3,
                            centerX - cellWidth / 3,
                            centerY + cellHeight / 3,
                            xPaint
                    );
                } else if (cellValue == O_PLAYER) {
                    float radius = Math.min(cellWidth, cellHeight) / 3;
                    canvas.drawCircle(centerX, centerY, radius, oPaint);
                }
            }
        }
    }

    private void drawBoard(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float cellWidth = width / (float) GRID_SIZE;
        float cellHeight = height / (float) GRID_SIZE;

        for (int i = 1; i < GRID_SIZE; i++) {
            float x = cellWidth * i;
            float y = cellHeight * i;

            canvas.drawLine(x, 0, x, height, linePaint);
            canvas.drawLine(0, y, width, y, linePaint);
        }
    }

    private int winLineStartX, winLineStartY, winLineEndX, winLineEndY;
    private boolean hasWinLine = false;

    // ...

    public void setWinLine(int startX, int startY, int endX, int endY) {
        winLineStartX = startX;
        winLineStartY = startY;
        winLineEndX = endX;
        winLineEndY = endY;
        hasWinLine = true;
        invalidate(); // Перерисовать поле с зачеркнутой линией
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int row = (int) (event.getY() / getHeight() * GRID_SIZE);
            int col = (int) (event.getX() / getWidth() * GRID_SIZE);

            if (isValidMove(row, col)) {

                if (onMoveListener != null) {
                    onMoveListener.onMove(row, col);
                }
            }
        }
        return true;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int player) {
        currentPlayer = player;
    }

    public void resetBoard() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                board[row][col] = EMPTY;
            }
        }
        currentPlayer = X_PLAYER;
        invalidate();
    }
    public float getCellWidth() {
        return getWidth() / (float) GRID_SIZE;
    }

    // Метод для получения высоты ячейки
    public float getCellHeight() {
        return getHeight() / (float) GRID_SIZE;
    }
    private boolean isValidMove(int row, int col) {
        return (row >= 0 && row < GRID_SIZE &&
                col >= 0 && col < GRID_SIZE &&
                board[row][col] == EMPTY);
    }
}
