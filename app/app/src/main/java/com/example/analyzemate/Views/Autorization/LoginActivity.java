package com.example.analyzemate.Views.Autorization;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
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

import com.example.analyzemate.R;
import com.example.analyzemate.Views.MainActivity;

public class LoginActivity extends AppCompatActivity {

    Button bEnter;
    EditText etEmail, etPassword;
    TextView tvRegister;

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
        // Получение полей формы
        etEmail = findViewById(R.id.edit_email);
        etPassword = findViewById(R.id.edit_password);
        tvRegister = findViewById(R.id.str_register);
        bEnter = findViewById(R.id.bt_register);

        // Создание текста с ссылкой на регистрацию
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        };
        SpannableString register = new SpannableString("Зарегистрироваться");
        register.setSpan(clickableSpan, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Задание текста
        tvRegister.setText(register);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());

        // Обработка кнопки входа
        bEnter.setOnClickListener(this::goToMainActivity);
    }

    // TODO Изменить способ проверки пользователя
    private void goToMainActivity(View view) {
        // User user = GetUserFromEditData();
        if (ValidateFields()) {
            // TODO Запрос к серверу
            // AutorizationHandler.AuthenticationUser(user, this);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("key", "authorization");
            startActivity(intent);
        }

        // TODO Проверка данных ответа сервера
    }

    /*
    private User GetUserFromEditData() {
        // Создание пользователя
        User user = new User(email, "", "", "", new Date(), password);

        return  user;
    }
     */

    /**
     * Метод валидации полей входа.
     * Метод проверяет пустоту полей.
     * @return true, если валидация пройдена, в ином случае false
     */
    private boolean ValidateFields() {
        String email =  etEmail.getText().toString();
        String password = etPassword.getText().toString();
        boolean isEmpty = false;

        if (email.isEmpty()) {
            etEmail.setError("Поле должно быть заполнено");
            isEmpty = true;
        }

        if (password.isEmpty()) {
            etPassword.setError("Поле должно быть заполнено");
            isEmpty = true;
        }

        return !isEmpty;
    }

}