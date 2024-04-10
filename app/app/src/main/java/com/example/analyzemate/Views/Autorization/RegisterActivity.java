package com.example.analyzemate.Views.Autorization;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.analyzemate.Controllers.Interfaces.AutorizationHandler;
import com.example.analyzemate.Models.User;
import com.example.analyzemate.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    TextView tvLogin;
    EditText ed_email, ed_surname, ed_name, ed_patronymic, ed_Date, ed_password;

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
        // Получение полей формы
        tvLogin = findViewById(R.id.str_login);
        ed_email = findViewById(R.id.edit_email);
        ed_surname = findViewById(R.id.et_surname);
        ed_name = findViewById(R.id.et_name);
        ed_patronymic = findViewById(R.id.et_patronymic);
        ed_Date = findViewById(R.id.editTextDate);
        ed_password = findViewById(R.id.et_password);

        // Листенер для кнопки
        Button bt_register = findViewById(R.id.bt_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Загушка.
                startActivity(new Intent(RegisterActivity.this, SuccessRegisterActivity.class));
                // goToSuccessRegisterActivity();
            }
        });

        // Создание текста с ссылкой на логин
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                finish();
            }
        };
        SpannableString register = new SpannableString("Войти");
        register.setSpan(clickableSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Задание текста
        tvLogin.setText(register);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());

        /*
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
         */
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

    // TODO Изменить после настройки страницы
    /*
    private boolean CheckFields() {
        String email =  etEmail.getText().toString();
        String password = etPassword.getText().toString();
        boolean isEmpty = true;

        if (email.isEmpty()) {
            etEmail.setError("Поле должно быть заполнено");
            isEmpty = false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Поле должно быть заполнено");
            isEmpty = false;
        }

        return isEmpty;
    }*/
}