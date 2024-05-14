package com.example.analyzemate.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzemate.Controllers.Adapters.RecyclerViewAdapter;
import com.example.analyzemate.Controllers.Interfaces.TransactionHistoryCallback;
import com.example.analyzemate.Controllers.Interfaces.TransactionHistoryHandler;
import com.example.analyzemate.Models.State;
import com.example.analyzemate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StockActivity extends AppCompatActivity implements TransactionHistoryCallback {

    ArrayList<State> states = new ArrayList<State>();


    @Override
    public void onTransactionHistoryReceived(JSONArray jsonArray) throws JSONException, IOException, ParseException {
        setTransactionList(jsonArray);
        if (states.isEmpty()) {
            return;
        }

        runOnUiThread(() -> {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, states);
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stock);
        /*
         * Настройка навигационной панели
         * Задание начального экрана, добавление путей перехода
         */
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_stock);

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

        // Устанавливаем себя в качестве слушателя получения транзакций
        TransactionHistoryHandler.setTransactionHistoryCallback(this);

        // Запрос к серверу на получение данных транзакций
        TransactionHistoryHandler.GetListHistoryTransactionsInPortfolio(this, 1);
        TransactionHistoryHandler.GetListHistoryTransactionsInPortfolio(this, 2);
        TransactionHistoryHandler.GetListHistoryTransactionsInPortfolio(this, 3);

    }

    private void setTransactionList(JSONArray jsonArray) throws JSONException, IOException, ParseException {
        // Переводим json в StockPaper и добавляем в список states
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Integer volume = jsonObject.getInt("volume");
            float price = (float) jsonObject.getDouble("price");
            String security = jsonObject.getString("security");
            Integer portfolio = jsonObject.getInt("portfolio") + 1;
            String type = jsonObject.getString("type");

            SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
            SimpleDateFormat outputFormatter = new SimpleDateFormat("MM-dd HH:mm");

            String time = jsonObject.getString("timestamp");

            // Преобразование строки в Date
            Date date = inputFormatter.parse(time);

            // Преобразование Date в строку с нужным форматом
            String outputDateString = outputFormatter.format(date);


            String text = "Портфель: " + portfolio.toString() + " Кол-во: " + volume.toString() + " Дата: " + outputDateString;

            states.add(new State(
                    security,
                    text,
                    R.drawable.baseline_notifications_none_24,
                    Float.toString(price),
                    type));
        }
    }
}