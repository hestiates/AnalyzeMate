package com.example.analyzemate.Views.Autorization;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.analyzemate.Models.User;
import com.example.analyzemate.R;
import com.example.analyzemate.Controllers.Interfaces.AutorizationHandler;
import com.example.analyzemate.Views.MainActivity;

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

        // Задание обработчика клина на "Уже есть аккаунт"
        TextView textViewAlreadyHaveAccount = findViewById(R.id.text_already_have_account);
        Intent intentLogin = new Intent(this, LoginActivity.class);

        // Установить обработчик клика на TextView
        textViewAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Перенаправление на другую активность (например, SignUpActivity)
                startActivity(intentLogin);
            }
        });
    }
    // textWatcher is for watching any changes in editText
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // this function is called before text is edited
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // this function is called when text is edited
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                Intent intent = new Intent(RegisterActivity.class.newInstance(), SuccessRegisterActivity.class);
                startActivity(intent);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
    };



    /**
     * После нажатия кнопки создает пользователя из введенных данных.
     * Проверяет корректность введенных данных.
     * Отправляет запрос к серверу на регистрацию пользователя.
     * Перенаправляет на RegisterActivity
     */

    public void goToSuccessRegisterActivity() {
        User user = GetUserFromEditData();

        // Запрос к серверу
        AutorizationHandler.RegisterUser(user, this);
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
        String date_str = ed_Date.getText().toString(); // TODO преобразование в формат Date
        String password = ed_password.getText().toString();

        Date date = null;

        // Форматтер для преобразования даты из строки
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd.MM.yyyy");

        // Форматтер для преобразования даты в строку в формате "2010-10-15T09:27:37.000"
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        String formattedDate = null;
        try {
            // Преобразование строки с датой в объект Date
            Date date_d = inputFormat.parse(date_str);

            // Преобразование объекта Date в строку с нужным форматом
            formattedDate = outputFormat.format(date_d);

            // Вывод отформатированной даты
            System.out.println(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Преобразование строки в date
        try {
            date = outputFormat.parse(formattedDate);
//            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Создание пользователя
        User user = new User(email, surname, name, patronymic, date, password);

        return  user;
    }
}