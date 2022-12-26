package ru.startandroid.develop.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

//головне актівіті
public class MainActivity extends AppCompatActivity {
    //Об'єкт для ініціалізації бази даних
    TeachersDatabase myDB;
    //Списки для завантаження даних з бази
    ArrayList<String> _id, user_name, user_position, user_subjects;
    //Адаптер для відображення зчитаних даних на екрані
    AdapterForDatabase customAdapter;
    //Для відображення даних
    RecyclerView recyclerView;
    //Для відображення пустої бази даних
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.noData);
        //ініціалізація бази даних
        myDB = new TeachersDatabase(MainActivity.this);
        //ініціалізація списків, в які будуть завантажені дані з бази даних
        _id = new ArrayList<>();
        user_name = new ArrayList<>();
        user_position = new ArrayList<>();
        user_subjects = new ArrayList<>();

        //виклик методу, для завантаження даних у списки
        storeDataInArrays();

        //ініціалізація адаптера
        customAdapter = new AdapterForDatabase(MainActivity.this, this, _id, user_name, user_position, user_subjects);
        //встановлюємо адаптер
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    //для оновлення вмісту екрану
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }
    //метод для завантаження даних з бази в списки
    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                _id.add(cursor.getString(0));
                user_name.add(cursor.getString(1));
                user_position.add(cursor.getString(2));
                user_subjects.add(cursor.getString(3));
            }
        }
    }
    //перехід на актівіті в якому будемо додавати дані до бази
    public void addData(View view){
        Intent intentTeacher = new Intent(this, AddingTeacher.class);
        startActivity(intentTeacher);
    }
    //метод для кнопки видалення усіх користувачів з бази
    public void deleteAllUsers(View view) {
        TeachersDatabase myDB = new TeachersDatabase(MainActivity.this);
        myDB.deleteAllData();
        recreate();
    }
}
