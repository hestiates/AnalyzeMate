package com.example.analyzemate.Views.Autorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.analyzemate.Models.User;
import com.example.analyzemate.R;
import com.example.analyzemate.Controllers.Interfaces.AutorizationHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Листенер для кнопки
        Button bt_register = findViewById(R.id.bt_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSuccessRegisterActivity();
            }
        });
    }

    /**
     * После нажатия кнопки создает пользователя из введенных данных.
     * Проверяет корректность введенных данных.
     * Отправляет запрос к серверу на регистрацию пользователя.
     * Перенаправляет на RegisterActivity
     */

    public void goToSuccessRegisterActivity() {
        User user = GetUserFromEditData();

        // TODO Запрос к серверу
        AutorizationHandler.CheckStartup();

        // TODO Проверка данных ответа сервера

        // Если успешно
        Intent intent = new Intent(this, SuccessRegisterActivity.class);
        startActivity(intent);
    }


    private User GetUserFromEditData() {
        // Получения данные введенных
        EditText ed_email = findViewById(R.id.edit_email);
        EditText ed_surname = findViewById(R.id.et_surname);
        EditText ed_name = findViewById(R.id.et_name);
        EditText ed_patronymic = findViewById(R.id.et_patronymic);
        EditText ed_Date = findViewById(R.id.editTextDate);
        EditText ed_password = findViewById(R.id.et_password);

        String email =  ed_email.getText().toString();
        String surname =  ed_surname.getText().toString();
        String name =  ed_name.getText().toString();
        String patronymic =  ed_patronymic.getText().toString();
        String date = ed_Date.getText().toString(); // TODO преобразование в формат Date
        String password = ed_password.getText().toString();

        ////// Тестовое для преобразования даты
        String dtStart = "2010-10-15T09:27:37Z";
        Date date_test = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            date_test = format.parse(dtStart);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Создание пользователя
        User user = new User(email, surname, name, patronymic, date_test, password);

        return  user;
    }
}