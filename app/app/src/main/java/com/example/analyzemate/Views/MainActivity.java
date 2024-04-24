package com.example.analyzemate.Views;
import com.example.analyzemate.Controllers.Interfaces.OnBalanceUpdateListener;
import com.example.analyzemate.Controllers.Interfaces.UserInfoHandler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzemate.Controllers.Adapters.RecyclerViewAdapter;
import com.example.analyzemate.Models.State;
import com.example.analyzemate.R;
import com.example.analyzemate.Views.Autorization.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnBalanceUpdateListener {
    TextView textView_balance;
    @Override
    public void onBalanceUpdated(double balance) {
        // Обновляем UI с полученным балансом
        textView_balance.setText(String.valueOf(balance));
    }

    ArrayList<State> states = new ArrayList<State>();
    public static final String APP_PREFERENCES = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textView_balance = findViewById(R.id.textView_balance);

        // Устанавливаем себя в качестве слушателя обновления баланса
        UserInfoHandler.setBalanceUpdateListener(this);
        // Получаем баланс с сервера
        UserInfoHandler.getBalanceFromServer(MainActivity.this);

        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        // Проверка токена авторизации
        CheckAuthorizationToken(preferences);

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
     * Проверка авторизации пользователя
     * Запускает LoginActivity, если пользователь не авторизован
     * @param preferences переменная с переменными из памяти телефона
     */
    private void CheckAuthorizationToken(SharedPreferences preferences) {
        if (preferences.contains("token")) {
            //  затычка, удаляет пользователя из памяти телефона
//            SharedPreferences.Editor editor = preferences.edit();
//             editor.clear();
//             editor.apply();
//             Toast.makeText(MainActivity.this, "check", Toast.LENGTH_SHORT).show();
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