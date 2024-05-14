package com.example.analyzemate.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzemate.Controllers.Adapters.RecyclerViewAdapter;
import com.example.analyzemate.Controllers.Adapters.UiAdapter;
import com.example.analyzemate.Controllers.Interfaces.AlertDialogListener;
import com.example.analyzemate.Controllers.Interfaces.EditPortfolioCallback;
import com.example.analyzemate.Controllers.Interfaces.FireBaseHandler;
import com.example.analyzemate.Controllers.Interfaces.OnBalanceUpdateListener;
import com.example.analyzemate.Controllers.Interfaces.PortfolioCallback;
import com.example.analyzemate.Controllers.Interfaces.PortfolioHandler;
import com.example.analyzemate.Controllers.Interfaces.UserInfoHandler;
import com.example.analyzemate.Models.ExistingUser;
import com.example.analyzemate.Models.Portfolio;
import com.example.analyzemate.Models.State;
import com.example.analyzemate.R;
import com.example.analyzemate.Views.Autorization.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnBalanceUpdateListener {
    ArrayList<State> states = new ArrayList<State>();
    private Integer curr_briefcase = 1;
    private static final int MAX_BRIEFCASE = 3;
    public static final String APP_PREFERENCES = "user";
    TextView textView_balance;
    @Override
    public void onBalanceUpdated(float balance) {
        // Обновляем UI с полученным балансом
        textView_balance.setText(String.valueOf(balance));
    }

    @Override
    public void existingUserReceived(ExistingUser existingUser) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textView_balance = findViewById(R.id.textView_balance);
        ImageView bt_add = findViewById(R.id.bt_add);
        LinearLayout layout = findViewById(R.id.layout);

        //// Отправка регистрационного токена на сервер для FCM push
        Task<String> stringTask = FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            // Обработка ошибки
                            return;
                        }

                        // Полученный токен
                        String token = task.getResult();

                        // Отправьте этот токен на ваш сервер FastAPI
                        FireBaseHandler.sendRegistrationToken(getApplicationContext(), token);
                    }
                });

        // Устанавливаем себя в качестве слушателя обновления баланса
        UserInfoHandler.setBalanceUpdateListener(this);
        // Получаем баланс с сервера
        UserInfoHandler.getBalanceFromServer(MainActivity.this);

        Bundle extras = getIntent().getExtras();
        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        // Проверка токена авторизации
        CheckAuthorizationToken(preferences);

        if (extras != null) { // Если Активити передало параметры
            RememberUser(extras, preferences);
        }

        // Проверка авторизации пользователя
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
         * Настройка списка ценных бумаг портфеля, задание адаптера
         */
        PortfolioHandler.getUsersPortfolios(this, new PortfolioCallback() {
            @Override
            public void PortfolioReceived(ArrayList<Portfolio> portfolioList) {
                if (portfolioList.toArray().length >= 3) {
                    curr_briefcase = 3;
                    bt_add.setVisibility(View.GONE);
                }
                for (int i = 0; i < portfolioList.toArray().length; i++) {
                    Portfolio portfolio = portfolioList.get(i);
                    int portfolioID = i + 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CreateBriefcase(MainActivity.this, layout, portfolioID);
                            RecyclerView recyclerView = findViewById(portfolioID);
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this, portfolio.securities);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });

        // Добавление портфелей
        bt_add.setOnClickListener(view -> {
            CreateDialogView(this, new AlertDialogListener() {
                @Override
                public void OnPositiveReceived(float balance) {
                    // Обработка введенного баланса
                    final double portfolio_balance = balance;

                    runOnUiThread(() -> {
                        curr_briefcase++;
                        if (MAX_BRIEFCASE <= curr_briefcase) {
                            bt_add.setVisibility(View.GONE);
                        }
                    });
                    try {
                        PortfolioHandler.AddNewPortfolio(MainActivity.this, portfolio_balance, new EditPortfolioCallback() {
                            @Override
                            public void EditPortfolioSuccess() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Портфель создан", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    runOnUiThread(() -> recreate());
                }
            });
        });
    }

    /*
        Метод для создания новых портфелей.
        Создает новый портфель с пустым списком ценных бумаг.
     */
    private void CreateBriefcase(Context context, LinearLayout layout, int portfolioID) {
        LinearLayout briefcaseLayout = new LinearLayout(context);
        briefcaseLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(
                UiAdapter.dpToPx(context, 20),
                UiAdapter.dpToPx(context, 10),
                UiAdapter.dpToPx(context, 20),
                UiAdapter.dpToPx(context, 10));
        briefcaseLayout.setLayoutParams(layoutParams);

        TextView briefcaseTextView = new TextView(context);
        briefcaseTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        briefcaseTextView.setText(R.string.briefcase);
        briefcaseTextView.setTextSize(22);
        briefcaseTextView.setTextColor(Color.BLACK);
        briefcaseLayout.addView(briefcaseTextView);

        Button historyButton = new Button(context);
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        historyButton.setLayoutParams(layoutParams);
        historyButton.setText(R.string.history);
        historyButton.setTextSize(15);
        historyButton.setBackgroundColor(Color.TRANSPARENT);
        historyButton.setTextColor(Color.BLACK);
        historyButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.baseline_history_24,0);
        briefcaseLayout.addView(historyButton);
        layout.addView(briefcaseLayout);

        RecyclerView recyclerView = new RecyclerView(layout.getContext());
        recyclerView.setId(portfolioID);
        recyclerView.setLayoutManager(new LinearLayoutManager(briefcaseLayout.getContext()));
        layout.addView(recyclerView);
    }

    private void CreateDialogView(Activity activity, AlertDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Введите баланс портфеля");
        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("Принять", (dialogInterface, i) -> {
            String inputText = input.getText().toString();
            if (!inputText.isEmpty()) {
                try {
                    float balance = Float.parseFloat(inputText);
                    listener.OnPositiveReceived(balance);
                } catch (NumberFormatException e) {
                    input.setError("Некорректное значение");
                }
            } else {
                input.setError("Поле не может быть пустым");
            }
        });
        builder.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }

    /**
     * Запоминаем пользователя в памяти телефона.
     Сохраняет в памяти телефона флаг remember.
     * @param extras переменные, переданные из другого активити
     * @param preferences переменная с переменными из памяти телефона
     */
    private void RememberUser(Bundle extras, SharedPreferences preferences) {
        String value = extras.getString("key"); // Полученный параметр
        // Если перенапревлены из LoginActivity
        if (Objects.equals(value, "authorization")) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("token", "true");
            editor.apply();
            // TODO Заглушка. Заменить на временное хранение
            Toast.makeText(MainActivity.this, "Remember", Toast.LENGTH_SHORT).show();
        }
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
}