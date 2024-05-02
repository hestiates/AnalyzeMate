package com.example.analyzemate.Controllers.Interfaces;

import com.example.analyzemate.Models.Constants;
import com.example.analyzemate.Models.EnumTimeframe;
import com.example.analyzemate.Models.StockPaperToUI;
import com.example.analyzemate.Controllers.Interfaces.StockPaperToUICallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;


public class StockPaperHandler {
    /**
     * Метод получения данных о бумаге по тикеру и таймфрейму с сервера
     *
     * @param ticker Тикер бумаги
     * @param timeframe Таймфрейм
     * @return json в строке
     */

    static OkHttpClient client = new OkHttpClient();

    /**
     * Метод для установки слушателя обновления данных о бумаге
     */
    private static StockPaperCallback stockPaperCallback;
    /**
     * Позволяет активити установить себя в качестве слушателя баланса
     */
    public static void setStockPaperCallback(StockPaperCallback callback) {
        stockPaperCallback = callback;
    }

    static String json;

    public static void GetStockPaperFromServer(Context context, String ticker, StockPaperToUICallback callback) {
        String ticketJson = "[\"" + ticker + "\"]";
        RequestBody body = RequestBody.create(ticketJson, MediaType.parse("application/json"));

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        Request request = new Request.Builder()
                .url(serverUrl + "securities/?include_historic_candles=false&")
                .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                .post(body)
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

                    assert responseBody != null;
                    String responseBodyString = responseBody.string();

                    try {
                        JSONArray jsonResponse = new JSONArray(responseBodyString);
                        JSONObject j = (JSONObject) jsonResponse.get(0);

                        String ticker = j.getString("ticker");
                        String name = j.getString("name");
                        Double price = j.getDouble("price");

                        StockPaperToUI stockPaperToUI = new StockPaperToUI(ticker, name, price);

                        callback.StockPaperToUIReceived(stockPaperToUI);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
    }

    /**
     * Метод получения данных о ценной бумаге с свечами по тикеру и таймфрейму.
     * @param context - контекст (активити), для получения токена из sharedPreferences
     * @param ticker - тикер бумаги
     * @param timeframe - таймфрейм
     */
    public static void GetJSONStockPaperFromServer(Context context, String ticker,
                                                     EnumTimeframe timeframe) {
        String ticketJson = "[\"" + ticker + "\"]";
        RequestBody body = RequestBody.create(ticketJson, MediaType.parse("application/json"));

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        // Преобразуем таймфрейм к числу, согласно серверу
        int intTimeframe = 0;
        if (timeframe == EnumTimeframe.CANDLE_INTERVAL_30_MIN) {
            intTimeframe = 9;
        }
        else if (timeframe == EnumTimeframe.CANDLE_INTERVAL_HOUR) {
            intTimeframe = 4;
        }
        else if (timeframe == EnumTimeframe.CANDLE_INTERVAL_4_HOUR) {
            intTimeframe = 11;
        }
        else if (timeframe == EnumTimeframe.CANDLE_INTERVAL_DAY) {
            intTimeframe = 5;
        }

        Request request = new Request.Builder()
                .url(serverUrl + "securities/?include_historic_candles=true&timeframe=" +
                        intTimeframe)
                .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                .post(body)
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
                    assert responseBody != null;
                    String responseBodyString = responseBody.string();

                    try {
                        JSONArray jsonResponse = new JSONArray(responseBodyString);

                        JSONObject j = (JSONObject) jsonResponse.get(0);

                        String json = j.toString();

                        // уведомляем слушателя
                        // if нужен для проверки, что активити ждет данных о бумаге
                        if (stockPaperCallback != null) {
                            stockPaperCallback.onStockPaperReceived(json);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


    /**
     * Метод получения списка ценных бумаг
     * @param context - контекст (активити), для получения токена из sharedPreferences
     */
    public static void GetListStockPaperFromServer(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        Request request = new Request.Builder()
                .url(serverUrl + "securities")
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
                    if (!response.isSuccessful()) {
                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }
                    assert responseBody != null;
                    String responseBodyString = responseBody.string();

                    try {
                        Log.d("Response", responseBodyString);
                        JSONArray jsonResponse = new JSONArray(responseBodyString);

                        // уведомляем слушателя
                        // if нужен для проверки, что активити ждет данных о бумаге
                        if (stockPaperCallback != null) {
                            stockPaperCallback.onStockPaperListReceived(jsonResponse);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
