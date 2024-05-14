package com.example.analyzemate.Controllers.Interfaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.analyzemate.Models.Constants;
import com.example.analyzemate.Models.Portfolio;
import com.example.analyzemate.Models.State;
import com.example.analyzemate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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

    public static void AddNewPortfolio(Context context, Double balance, EditPortfolioCallback callback) throws JSONException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        // постройка тела запроса
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("balance", balance);

        RequestBody requestBody = RequestBody.create(String.valueOf(jsonBody),
                MediaType.parse("application/json"));

        // формирование запроса
        Request request = new Request.Builder()
                .url(serverUrl + "portfolio/")
                .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                .post(requestBody)
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
                    callback.EditPortfolioSuccess();
                }
            }
        });
    }

    public static void MakeTransaction(Context context, String transactionType, Integer volume, String security, Integer idPortfolio, EditPortfolioCallback callback) throws JSONException {
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
                .post(requestBody)
                .build();

        Log.d("MakeTransaction", idPortfolio.toString());
        Log.d("MakeTransaction", "Request URL: " + request.url());
        Log.d("MakeTransaction", "Request Headers: " + request.headers().toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                throw new RuntimeException(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        if (response.code() == 409) {
                            new Handler(Looper.getMainLooper()).post(() ->
                                            Toast.makeText(context, "В портфеле нет доступных бумаг для продаж", Toast.LENGTH_SHORT).show()
                                    );
                        } else if (response.code() == 400) {
                            new Handler(Looper.getMainLooper()).post(() ->
                                    Toast.makeText(context, "Недостаточно денег не балансе", Toast.LENGTH_SHORT).show()
                            );
                        }
                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }
                    callback.EditPortfolioSuccess();
                }  catch (IOException e) {
                    Log.e("MakeTransaction", "Ошибка при обработке ответа от сервера", e);
                    throw new IOException("Запрос к серверу не был успешен: " +
                            response.code() + " " + response.message());
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
