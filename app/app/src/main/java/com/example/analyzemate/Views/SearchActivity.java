package com.example.analyzemate.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzemate.Controllers.Adapters.RecyclerViewAdapter;
import com.example.analyzemate.Controllers.Interfaces.StockPaperCallback;
import com.example.analyzemate.Controllers.Interfaces.StockPaperHandler;
import com.example.analyzemate.Controllers.Interfaces.StockPaperToUICallback;
import com.example.analyzemate.Controllers.StockPaper.ParseJsonToStockPaper;
import com.example.analyzemate.Models.State;
import com.example.analyzemate.Models.StockPaper;
import com.example.analyzemate.Models.StockPaperToUI;
import com.example.analyzemate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity implements StockPaperCallback {
    ArrayList<State> states = new ArrayList<State>();
    private Context mContext;

    @Override
    public void onStockPaperReceived(String json) throws IOException {

    }

    /**
     * При получении от сервера списка ценных бумаг запускает метод отрисовки
     * @param jsonArray - массив с jsonObject ценных бумаг
     */
    @Override
    public void onStockPaperListReceived(JSONArray jsonArray) throws JSONException, IOException {
        setStockPaperList(jsonArray);
        runOnUiThread(() -> {
            RecyclerView recyclerView = findViewById(R.id.recyclerViewSearch);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, states);
            recyclerView.setAdapter(adapter);
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        mContext = this;

        /*
         * Настройка навигационной панели
         * Задание начального экрана, добавление путей перехода
         */
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int item_id = item.getItemId();
            if (item_id == R.id.bottom_home){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);
                finish();
                return true;
            } else if (item_id == R.id.bottom_search){
                return true;
            } else if (item_id == R.id.bottom_stock){
                startActivity(new Intent(getApplicationContext(), StockActivity.class));
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
        // -----------------------
        /*
            Настройка списка RecyclerView, получение списка ценных бумаг и их отрисовка
        */
        // Устанавливаем себя в качестве слушателя получения данных о бумаге
        StockPaperHandler.setStockPaperCallback(this);

        // Запрос к серверу на получение данных о бумаге
        StockPaperHandler.GetListStockPaperFromServer(this);


        /*
            Прослушивание searchView
        */
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Запрос на получение ценной бумаги по тикеру (query)
                StockPaperHandler.GetSearchStockPaperFromServer(mContext, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Здесь можно обрабатывать изменение текста в реальном времени (необязательно)
                return false;
            }
        });


//        setInitialData();
//        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearch);
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, states);
//        recyclerView.setAdapter(adapter);
    }

    /**
     * Заполнение списка states бумагами
     * @param jsonArray - массив с jsonObject ценных бумаг
     * @throws JSONException
     * @throws IOException
     */
    private void setStockPaperList(JSONArray jsonArray) throws JSONException, IOException {
        states.clear();
        // Переводим json в StockPaper и добавляем в список states
        for (int i=0; i < jsonArray.length(); i++) {
            StockPaper stockPaper = ParseJsonToStockPaper.JsonToStockPaper(jsonArray.get(i).toString());
            // Todo добавить тренд
            states.add(new State(
                    stockPaper.getTicker(),
                    stockPaper.getName(),
                    R.drawable.baseline_home_24,
                    Double.toString(stockPaper.getPrice()),
                    "8"));
        }
    }

    private void handleSearchQuery(StockPaperToUI stockPaperToUI) {
        states.clear();

        if (stockPaperToUI == null) {
            return;
        }

        states.add(new State(
                stockPaperToUI.ticker,
                stockPaperToUI.name,
                R.drawable.baseline_home_24,
                Double.toString(stockPaperToUI.price),
                "8"));
    }

    /**
     * Загглушка. Создает список бумаг и иконок
     */
    private void setInitialData() {
        states.add(new State("test1","Gasprompt", R.drawable.baseline_gas_meter_24, "112.1", "8"));
        states.add(new State("test2","HeheLAND", R.drawable.baseline_home_24, "112.1", "18"));
        states.add(new State("test3","Gasproympt", R.drawable.baseline_gas_meter_24, "100412.0", "0.3"));
    }
}

