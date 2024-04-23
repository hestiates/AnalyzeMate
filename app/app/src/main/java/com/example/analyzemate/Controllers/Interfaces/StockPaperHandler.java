package com.example.analyzemate.Controllers.Interfaces;

import com.example.analyzemate.Models.Constants;
import com.example.analyzemate.Models.EnumTimeframe;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.SharedPreferences;
import android.content.Context;

import androidx.annotation.NonNull;


public class StockPaperHandler {
    /**
     * Метод получения данных о бумаге по тикеру и таймфрейму с сервера
     *
     * @param ticker Тикер бумаги
     * @param timeframe Таймфрейм
     * @return json в строке
     */

    static String json;

    public static String GetJSONStockPaperFromServer(Context context, String ticker, EnumTimeframe timeframe) throws IOException {
        String ticketJson = "[\"" + ticker + "\"]";
        RequestBody body = RequestBody.create(ticketJson, MediaType.parse("application/json"));

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        Request request = new Request.Builder()
                .url(serverUrl + "securities/?include_historic_candles=true")
                .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
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

                    JSONArray jsonResponse = null;
                    try {
                        jsonResponse = new JSONArray(responseBodyString);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    json = jsonResponse.toString();
                }
            }
        });
        return json;
    }
}
