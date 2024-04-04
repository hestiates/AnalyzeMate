package com.example.analyzemate.Views.Autorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.analyzemate.Controllers.Interfaces.AutorizationHandler;
import com.example.analyzemate.Models.User;
import com.example.analyzemate.R;
import com.example.analyzemate.Views.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToMainActivity(View view) {
        User user = GetUserFromEditData();

        // TODO Запрос к серверу
        AutorizationHandler.AuthenticationUser(user, this);

        // TODO Проверка данных ответа сервера

        // Если успешно
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("key", "authorization");
//        startActivity(intent);
    }

    private User GetUserFromEditData() {
        // Получения данные введенных
        EditText ed_email = findViewById(R.id.edit_email);
        EditText ed_password = findViewById(R.id.et_password);

        String email =  ed_email.getText().toString();
        String password = ed_password.getText().toString();

        // Создание пользователя
        User user = new User(email, "", "", "", new Date(), password);

        return  user;
    }
}