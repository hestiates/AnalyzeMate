package com.example.analyzemate.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzemate.Controllers.Adapters.RecyclerViewAdapter;
import com.example.analyzemate.Controllers.Interfaces.OnBalanceUpdateListener;
import com.example.analyzemate.Controllers.Interfaces.StockPaperHandler;
import com.example.analyzemate.Controllers.Interfaces.UserInfoHandler;
import com.example.analyzemate.Models.EnumTimeframe;
import com.example.analyzemate.Models.ExistingUser;
import com.example.analyzemate.Models.State;
import com.example.analyzemate.R;
import com.example.analyzemate.Views.Autorization.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnBalanceUpdateListener {
    ArrayList<State> states = new ArrayList<State>();
    Integer curr_briefcase = 1;
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
        textView_balance.setOnClickListener(view -> CreateDialogView());

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
         * Настройка списка, задание адаптера
         */
        setInitialData();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHome);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, states);
        recyclerView.setAdapter(adapter);

        // TODO Доделать добавление портфеля
        /*
        ImageView bt_add = findViewById(R.id.bt_add);
        LinearLayout layout = findViewById(R.id.layout);
        bt_add.setOnClickListener(view -> {
            curr_briefcase++;
            if (MAX_BRIEFCASE <= curr_briefcase) {
                bt_add.setVisibility(View.GONE);
            }
            LinearLayout briefcaseLayout = new LinearLayout(view.getContext());
            briefcaseLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(
                    UiAdapter.dpToPx(view.getContext(), 20),
                    UiAdapter.dpToPx(view.getContext(), 10),
                    UiAdapter.dpToPx(view.getContext(), 20),
                    UiAdapter.dpToPx(view.getContext(), 10));
            briefcaseLayout.setLayoutParams(layoutParams);
            TransitionManager.beginDelayedTransition(briefcaseLayout, new AutoTransition());

            TextView briefcaseTextView = new TextView(view.getContext());
            briefcaseTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            briefcaseTextView.setText(R.string.briefcase);
            briefcaseTextView.setTextSize(22);
            briefcaseTextView.setTextColor(Color.BLACK);
            briefcaseLayout.addView(briefcaseTextView);

            Button historyButton = new Button(view.getContext());
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
        });*/
    }

    private void CreateDialogView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите баланс");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("Принять", (dialogInterface, i) -> {
            float d = (float) Double.parseDouble(input.getText().toString());

            textView_balance.setText(String.valueOf(d));

            try {
                UserInfoHandler.UpdateUserBalance(MainActivity.this, d);
            } catch (JSONException e) {
                throw new RuntimeException(e);
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

    /**
     * Загглушка. Создает список бумаг и иконок
     */
    private void setInitialData() {
        states.add(new State("SBER","Gasprompt", R.drawable.baseline_gas_meter_24, "112.1", "8"));
        states.add(new State("SBER","RosCosmostars", R.drawable.baseline_home_24, "5.2", "131"));
    }
}