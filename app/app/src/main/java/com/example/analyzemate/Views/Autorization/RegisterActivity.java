package com.example.analyzemate.Views.Autorization;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    TextView tvLogin;
    EditText ed_email, ed_surname, ed_name, ed_patronymic, ed_password;
    TextView et_data;
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
        ed_email = findViewById(R.id.et_email);
        ed_surname = findViewById(R.id.et_surname);
        ed_name = findViewById(R.id.et_name);
        ed_patronymic = findViewById(R.id.et_patronymic);
        et_data = findViewById(R.id.et_data);
        ed_password = findViewById(R.id.et_password);

        // При нажатие на изменение даты, отображается диалоговое окно выбора даты
        et_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String month_str = String.valueOf(month + 1);
                                String day_str = String.valueOf(day);
                                if (9 >= month) {
                                    month_str = "0" + month_str;
                                }
                                if (9 >= day) {
                                    day_str = "0" + day_str;
                                }
                                et_data.setText(day_str + "." + month_str + "." + year);
                            }
                        },
                        year, month, day);
                dialog.show();
            }
        });

        // Листенер для кнопки
        Button bt_register = findViewById(R.id.bt_register);
        bt_register.setOnClickListener(v -> {
            if (ValidateFields()) {
                goToSuccessRegisterActivity();
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

        // Задание текста перехода на страницу входа
        tvLogin.setText(register);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
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
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
    };

    /**
     * После нажатия кнопки создает пользователя из введенных данных.
     * Отправляет запрос к серверу на регистрацию пользователя.
     * Перенаправляет на RegisterActivity
     */
    public void goToSuccessRegisterActivity() {
        User user = GetUserFromEditData();

        // Запрос к серверу
        AutorizationHandler.RegisterUser(user, this);
    }

    /**
     * Получение и создание User из введенных данных
     * @return User
     */
    private User GetUserFromEditData() {
        // Получения данные введенных
        String email =  ed_email.getText().toString();
        String surname =  ed_surname.getText().toString();
        String name =  ed_name.getText().toString();
        String patronymic =  ed_patronymic.getText().toString();
        String date_str = et_data.getText().toString(); // TODO преобразование в формат Date
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

    /**
     * Метод валидации полей входа.
     * Метод проверяет пустоту полей.
     * @return true, если валидация пройдена, в ином случае false
     */
    private boolean ValidateFields() {
        String email =  ed_email.getText().toString();
        String name = ed_name.getText().toString();
        String surname =  ed_surname.getText().toString();
        String password = ed_password.getText().toString();
        String data = et_data.getText().toString();
        boolean isEmpty = false;

        if (email.isEmpty()) {
            ed_email.setError("Поле должно быть заполнено");
            isEmpty = true;
        }
        if (name.isEmpty()) {
            ed_name.setError("Поле должно быть заполнено");
            isEmpty = true;
        }
        if (surname.isEmpty()) {
            ed_surname.setError("Поле должно быть заполнено");
            isEmpty = true;
        }
        if (data.isEmpty()) {
            et_data.setError("Поле должно быть заполнено");
            isEmpty = true;
        }
        if (password.isEmpty()) {
            ed_password.setError("Поле должно быть заполнено");
            isEmpty = true;
        }
        return !isEmpty;
    }
}