package com.example.lr4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class FindPairActivity extends AppCompatActivity {

    private int[] images = {
            R.drawable.tile_game_mushroom_1,
            R.drawable.tile_game_mushroom_2,
            R.drawable.tile_game_mushroom_3,
            R.drawable.tile_game_mushroom_4,
            R.drawable.tile_game_mushroom_5,
            R.drawable.tile_game_mushroom_6,
            R.drawable.tile_game_mushroom_7,
            R.drawable.tile_game_mushroom_8,
            R.drawable.tile_game_mushroom_9
    };
    private ImageView firstCard, secondCard;
    private int firstCardImageId, secondCardImageId;
    private int cardsMatched = 0;
    private int gridSize = 4;
    private boolean isGameFinished = false; // Флаг для отслеживания окончания игры
    private int totalCards = gridSize * gridSize;
    private int maxAttempts = totalCards / 2 + 3;
    private boolean isComparing = false;
    private int attempts = 0; // Ограничение на количество попыток

    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pair);

        gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setRowCount(gridSize);
        gridLayout.setColumnCount(gridSize);

        int[] randomizedImages = new int[totalCards];
        for (int i = 0; i < totalCards / 2; i++) {
            randomizedImages[i] = images[i];
            randomizedImages[i + totalCards / 2] = images[i];
        }
        shuffleArray(randomizedImages);

        int cardSize = getResources().getDisplayMetrics().widthPixels / gridSize;

        for (int i = 0; i < totalCards; i++) {
            ImageView card = new ImageView(this);
            card.setImageResource(R.drawable.card_back);
            card.setTag("unmatched:" + randomizedImages[i]);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = cardSize;
            params.height = cardSize;
            card.setLayoutParams(params);
            card.setScaleType(ImageView.ScaleType.FIT_XY);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClicked((ImageView) v);
                }
            });
            gridLayout.addView(card);
        }
    }

    private void onCardClicked(ImageView card) {
        if (isComparing || isGameFinished) {
            return; // Не принимаем клики во время сравнения
        }

        String tag = card.getTag().toString();
        String[] tagParts = tag.split(":");
        String state = tagParts[0];
        int clickedImageId = Integer.parseInt(tagParts[1]);

        if (state.equals("unmatched")) {
            card.setImageResource(clickedImageId);
            card.setTag("flipped:" + clickedImageId);

            if (firstCard == null) {
                firstCard = card;
                firstCardImageId = clickedImageId;
            } else {
                secondCard = card;
                secondCardImageId = clickedImageId;

                if (firstCardImageId == secondCardImageId) {
                    firstCard.setTag("matched:" + clickedImageId);
                    secondCard.setTag("matched:" + clickedImageId);
                    firstCard = null;
                    secondCard = null;
                    cardsMatched += 2;

                    if (cardsMatched == totalCards) {
                        gameWon();
                    }
                } else {
                    isComparing = true; // Блокируем нажатия
                    attempts++; // Увеличиваем количество попыток
                    if (attempts >= maxAttempts) {
                        gameLost();
                    }

                    // Задержка перед сбросом
                    card.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (firstCard != null && secondCard != null) {
                                firstCard.setImageResource(R.drawable.card_back);
                                secondCard.setImageResource(R.drawable.card_back);
                                firstCard.setTag("unmatched:" + firstCardImageId);
                                secondCard.setTag("unmatched:" + secondCardImageId);
                                firstCard = null;
                                secondCard = null;
                                isComparing = false; // Разблокируем нажатия
                            }
                        }
                    }, 1000);
                }
            }
        }
    }

    private void gameWon() {
        isGameFinished = true; // Устанавливаем флаг окончания игры
        int matchedPairs = cardsMatched / 2; // Рассчитываем количество угаданных пар
        String message = "Вы выиграли! Количество угаданных пар: " + matchedPairs;

        // Создаем диалоговое окно
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false) // Блокируем возможность закрытия окна без выбора
                .setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Обработка закрытия диалогового окна
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void gameLost() {
        isGameFinished = true; // Устанавливаем флаг окончания игры
        int matchedPairs = cardsMatched / 2; // Рассчитываем количество угаданных пар
        String message = "Вы проиграли! Попытки закончились. Количество угаданных пар: "+ matchedPairs;

        // Создаем диалоговое окно
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false) // Блокируем возможность закрытия окна без выбора
                .setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Обработка закрытия диалогового окна
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = (int) (Math.random() * (i + 1));
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
