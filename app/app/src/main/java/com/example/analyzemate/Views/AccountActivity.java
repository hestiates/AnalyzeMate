package com.example.analyzemate.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.analyzemate.Controllers.Interfaces.UserInfoCallback;
import com.example.analyzemate.Models.ExistingUser;
import com.example.analyzemate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.analyzemate.Controllers.Interfaces.UserInfoHandler;

import org.json.JSONException;

import java.io.IOException;

public class AccountActivity extends AppCompatActivity {
    TextView textView_surname;
    TextView textView_name;
    TextView textView_patronymic;
    TextView textView_birthdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        textView_name = findViewById(R.id.textView_name_ed);
        textView_surname = findViewById(R.id.textView_surname_ed);
        textView_patronymic = findViewById(R.id.textView_patronymic_ed);
        textView_birthdate = findViewById(R.id.textView_birthdate_ed);

        UserInfoHandler.GetCurrentUser(this, new UserInfoCallback() {
            @Override
            public void onSuccess(ExistingUser existingUser) throws IOException, JSONException {
                textView_surname.setText(String.valueOf(existingUser.surname));
                textView_name.setText(String.valueOf(existingUser.name));
                textView_patronymic.setText(String.valueOf(existingUser.patronymic));
                textView_birthdate.setText(String.valueOf(existingUser.birthdate));
            }
        });

        /*
         * Настройка навигационной панели
         * Задание начального экрана, добавление путей перехода
         */
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_account);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int item_id = item.getItemId();
            if (item_id == R.id.bottom_home){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);
                finish();
                return true;
            } else if (item_id == R.id.bottom_search){
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);
                finish();
                return true;
            } else if (item_id == R.id.bottom_stock){
                startActivity(new Intent(getApplicationContext(), StockActivity.class));
                overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);
                finish();
                return true;
            } else if (item_id == R.id.bottom_notification){
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);
                finish();
                return true;
            } else if (item_id == R.id.bottom_account){
                return true;
            }
            return false;
        });
        // -----------------------
    }
}