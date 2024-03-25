package com.example.analyzemate.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.analyzemate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Проверка авторизации
//        startActivity(new Intent(getApplicationContext(),GraphActivity.class));
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
        // -----------------------
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            return insets;
        });
    }
}