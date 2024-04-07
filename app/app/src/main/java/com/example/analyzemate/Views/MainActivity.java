package com.example.analyzemate.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzemate.Controllers.Interfaces.RecyclerViewAdapter;
import com.example.analyzemate.Models.State;
import com.example.analyzemate.R;
import com.example.analyzemate.Views.Autorization.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    ArrayList<State> states = new ArrayList<State>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Тест страницы ценной бумаги
        // startActivity(new Intent(getApplicationContext(), PaperActivity.class));

        Bundle extras = getIntent().getExtras();
        SharedPreferences mPreferences = getSharedPreferences("user", MODE_PRIVATE);

        if (extras != null) { // Если Активити передало параметры
            RememberUser(extras, mPreferences);
        }

        // Проверка авторизации пользователя
        CheckAuthorization(mPreferences);

        /*
        * Настройка навигационной панели
        * Задание начального экрана, добавление путей перехода
        */
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int item_id = item.getItemId();
            if (item_id == R.id.bottom_home){
                return true;
            } else if (item_id == R.id.bottom_search){
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                finish();
                return true;
            } else if (item_id == R.id.bottom_stock){
                startActivity(new Intent(getApplicationContext(), StockActivity.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                finish();
                return true;
            } else if (item_id == R.id.bottom_notification){
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                finish();
                return true;
            } else if (item_id == R.id.bottom_account){
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                finish();
                return true;
            }
            return false;
        });
        /*
         * Настройка списка, задание адаптера
         */
        setInitialData();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHome);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, states);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Запоминаем пользователя в памяти телефона.
     Сохраняет в памяти телефона флаг remember.
     * @param extras переменные, переданные из другого активити
     * @param mPreferences переменная с переменными из памяти телефона
     */
    private void RememberUser(Bundle extras, SharedPreferences mPreferences) {
        String value = extras.getString("key"); // Полученный параметр

        // Если перенапревлены из LoginActivity
        if (Objects.equals(value, "authorization")) {
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("token", "true");
            editor.apply();
            // TODO Заглушка. Заменить на временное хранение
            Toast.makeText(MainActivity.this, "Remember", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Проверка авторизации пользователя
     * Запускает LoginActivity, если пользователь не авторизован
     * @param mPreferences переменная с переменными из памяти телефона
     */
    private void CheckAuthorization(SharedPreferences mPreferences) {
        if (mPreferences.contains("token")) {
            // TODO затычка, удаляет пользователя из памяти телефона
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.clear();
            editor.apply();
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    /**
     * Загглушка. Создает список бумаг и иконок
     */
    private void setInitialData() {
        states.add(new State("Gasprompt", R.drawable.baseline_gas_meter_24, "112.1", "8"));
        states.add(new State("RosCosmostars", R.drawable.baseline_home_24, "5.2", "131"));
    }
}