package com.example.analyzemate.Controllers.Interfaces;
import com.example.analyzemate.Models.Constants;
import com.example.analyzemate.Models.ExistingUser;
import com.example.analyzemate.Models.State;
import com.example.analyzemate.Models.StockPaperToUI;
import com.example.analyzemate.Models.User;
import com.example.analyzemate.Models.Portfolio;
import com.example.analyzemate.Controllers.Interfaces.PortfolioCallback;
import com.example.analyzemate.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PortfolioHandler {
    static OkHttpClient client = new OkHttpClient();

    public static void MakeTransaction(Context context, String transactionType, Integer volume, String security, Integer idPortfolio) throws JSONException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        // постройка тела запроса
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("volume", volume);
        jsonBody.put("security", security);
        jsonBody.put("portfolio", idPortfolio);

        RequestBody requestBody = RequestBody.create(String.valueOf(jsonBody),
                MediaType.parse("application/json"));

        // формирование запроса
        Request request = new Request.Builder()
                .url(serverUrl + "portfolio/transaction/?transaction_type=" + transactionType)
                .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                .patch(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                throw new RuntimeException(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }
                    Toast.makeText(context, "Ценная бумага успешно куплена", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void getUsersPortfolios(Context context, PortfolioCallback callback) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        // формирование запроса
        Request request = new Request.Builder()
                .url(serverUrl + "portfolio/?include_securities=true")
                .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                throw new RuntimeException(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    // обработка ошибки
                    if (!response.isSuccessful()) {
                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }

                    assert responseBody != null;
                    String responseBodyString = responseBody.string();
                    ArrayList<Portfolio> portfolioList = new ArrayList<>();
                    // получение баланса из блока ответа сервера
                    try {
                        JSONArray jsonArray = new JSONArray(responseBodyString);

                        // проходимся по каждому портфелю
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonResponse = (JSONObject) jsonArray.get(i);

                            Integer id = jsonResponse.getInt("id");
                            Float balance = (float) jsonResponse.getDouble("balance");
                            Integer ownerId = jsonResponse.getInt("owner");

                            Portfolio portfolio = new Portfolio(id, balance, ownerId);

                            JSONArray securitiesArray = jsonResponse.getJSONArray("securities");

                            // и по каждой бумаге в портфеле
                            for (int j = 0; j < securitiesArray.length(); j++) {
                                JSONObject tmp = (JSONObject) securitiesArray.get(j);

                                String ticker = tmp.getString("ticker");
                                String name = tmp.getString("name");
                                String cost = String.valueOf(tmp.getDouble("price"));
                                String volume = String.valueOf(tmp.getInt("volume"));
                                String delta_price = String.valueOf(tmp.getDouble("delta_price"));

                                State paper = new State(ticker, name, R.drawable.baseline_home_24, cost, delta_price);

                                portfolio.AddToPortfolio(paper);
                            }
                            portfolioList.add(portfolio);
                        }
                        callback.PortfolioReceived(portfolioList);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
