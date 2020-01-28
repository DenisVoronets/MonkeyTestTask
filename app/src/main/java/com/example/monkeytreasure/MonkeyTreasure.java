package com.example.monkeytreasure;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MonkeyTreasure extends AppCompatActivity {
    private myImage[] images;
    private Integer[] myCols;
    private Integer[] myRows;
    private GridView myGridView;
    private GridAdapter myGridAdapter;
    private int monkeyPlace;
    private int monkey;
    Toast toast;
    private Random random;
    private myImage stepTrapeImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monkey_treasure);
        myGridView = findViewById(R.id.field);
        toastStart(myGridView);
        myGridView.setOnItemClickListener(myGridViewOnItemClickListener);
    }

    private GridView.OnItemClickListener myGridViewOnItemClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            move(position);
        }
    };

    public void startGame() {
        generateColsRowsIds();
        createGridItems();
        createAdapter();
        monkeyPlace();
        way();
    }

    //Создаем id для строк и столбцов, что бы потом обращаться к ним.
    public void generateColsRowsIds() {
        myCols = new Integer[9];
        myRows = new Integer[9];
        for (int i = 0; i < myCols.length; i++) {
            myCols[i] = i;
        }
        for (int i = 0; i < myRows.length; i++) {
            myRows[i] = i;
        }
    }

    // За полняем GridView элементами(картинка,id - номер для обращения, индекс для передвежения обезьянки).
    public void createGridItems() {
        images = new myImage[81];
        for (int i = 0; i < images.length; i++) {
            images[i] = new myImage(R.drawable.my_background, i, 2);
        }
    }

    // Создаем адаптер для GridView.
    public void createAdapter() {
        myGridAdapter = new GridAdapter(images, this, myCols, myRows);
        myGridView.setNumColumns(myGridAdapter.getMyCols());
        myGridView.setAdapter(myGridAdapter);
    }

    //Задаем начальное положение обезьянки((кол. строк * кол. колонок) + (кол. колонок/2)), /2 - для того что бы получить средину.
//GridView это сетка похожая на матрицу и обращение к определенному элементу происходит по мат. формуле (Xi * Yg + j)
    public void monkeyPlace() {
        monkeyPlace = (myGridAdapter.getMyRows() - 1) * myGridAdapter.getMyCols() + (myGridAdapter.getMyCols() / 2);
        images[monkeyPlace] = new myImage(R.drawable.monkey, monkeyPlace, 1);
        monkey = images[monkeyPlace].getIndex();

    }

    // Метод отвечает за передвежение пользователя, закраску пройденого пути и отклонения от правила.
// Метод также создает рандомную ловушку(змею или скорпиона), если пользователь сошел с правильного пути.
    public void move(int position) {
        int randomTrape = random.nextInt(2);
        int stepIndex = images[position].getIndex();
        int stepId = images[position].getId();
        myImage stepImage = new myImage(R.drawable.monkey, stepId, 1);
        if (randomTrape == 1) {
            stepTrapeImage = new myImage(R.drawable.scorpio, stepId, 2);
        } else {
            stepTrapeImage = new myImage(R.drawable.snake, stepId, 2);
        }
        if (Math.abs(monkeyPlace - stepId) == 9 | Math.abs(monkeyPlace - stepId) == 1) {
            if (stepIndex < monkey) {
                images[stepId] = stepImage;
                images[monkeyPlace] = new myImage(R.color.colorPrimaryDark, monkeyPlace, 0);
                monkeyPlace = stepId;
            }else if(stepIndex > monkey) {
                images[stepId] = stepTrapeImage;
                myGridView.setAdapter(myGridAdapter);
                toastGameOver(myGridView);
            }else if (stepIndex == monkey) {
                images[stepId] = stepImage;
                images[monkeyPlace] = new myImage(R.color.colorPrimaryDark, monkeyPlace, 0);
                toastWin(myGridView);
            }
        } else {
            toast = Toast.makeText(getApplicationContext(),
                    "Запререщенно двигаться через одну клетку или по диагонали", Toast.LENGTH_LONG);
            toast.show();
        }
        myGridView.setAdapter(myGridAdapter);

    }
//Метод генерирует путь.На каждой второй(что бы не было соприкосновения) строке берутся два рандомных елемента и между ними прорисовываеться путь.
    public void way() {
        random = new Random();
        int gridKey = monkeyPlace;
        int randomElement;
        int max;
        int min;

        for (int a = myGridAdapter.getMyRows() - 1; a > 0; a = a - 2) {
            randomElement = myGridAdapter.getMyRow(a) * myGridAdapter.getMyCols() + random.nextInt(9);
            max = Math.max(gridKey, randomElement);
            min = Math.min(gridKey, randomElement);
            rowLine(min, max);

            if (max == randomElement) {
                columnLine(max);
                gridKey = images[max - myGridAdapter.getMyRows() * 2].getId();
            } else {
                columnLine(min);
                gridKey = images[min - myGridAdapter.getMyRows() * 2].getId();
            }
        }
        images[gridKey] = new myImage(R.drawable.bananas, gridKey, 1);
        monkeyPlace();
    }

//Прорисовка колонки.
    public void rowLine(int min, int max) {
        for (int x = min; x <= max; x++) {
            images[x] = new myImage(R.drawable.my_background, x, 0);
            //R.color.colorPrimaryDark поменять картинку, что б видеть правильный путь
        }
    }
//Прорисовка столбца.
    public void columnLine(int max) {
        //R.color.colorPrimaryDark поменять картинку, что б видеть правильный путь
        //R.drawable.my_background скрыть путь
        images[max - myGridAdapter.getMyRows()] = new myImage(R.drawable.my_background, max - myGridAdapter.getMyRows(), 0);
        images[max - myGridAdapter.getMyRows() * 2] = new myImage(R.drawable.my_background, max - myGridAdapter.getMyRows() * 2, 0);
    }

    public void toastGameOver(View view) {
        final Intent intent = new Intent(MonkeyTreasure.this, MainActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(MonkeyTreasure.this);
        builder.setTitle("Ты проиграл!")
                .setMessage("Сыграем еще раз?")
                .setIcon(stepTrapeImage.getImage())
                .setCancelable(false)
                .setNegativeButton("Главное меню", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(intent);

                    }
                })
                .setPositiveButton("Начать заново",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startGame();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void toastWin(View view) {
        final Intent intent = new Intent(MonkeyTreasure.this, MainActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(MonkeyTreasure.this);
        builder.setTitle("Поздравляю!")
                .setMessage("Ты помог мартышке найти банан! Сыграем еще раз?")
                .setCancelable(false)
                .setNegativeButton("Главное меню", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(intent);
                    }
                })
                .setPositiveButton("Начать заново",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startGame();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void toastStart(View view) {
        final Intent intent = new Intent(MonkeyTreasure.this, MainActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(MonkeyTreasure.this);
        builder.setTitle("MonkeyTreasure")
                .setMessage("Добро пожаловать в игру!" +
                        "\nЦель: накормить обезьянку и не попасть в ловушки." +
                        "\n____________________________" +
                        "\nХодить можно только в радиусе одной клетки, но не по диагонали.")
                .setCancelable(false)
                .setIcon(R.drawable.monkey_smile)
                .setPositiveButton("Я готов!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startGame();
                    }
                })
                .setNegativeButton("Вернуться назад",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(intent);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}


