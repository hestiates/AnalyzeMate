package com.example.analyzemate.Controllers.Interfaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.analyzemate.Models.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TransactionHistoryHandler {

    static OkHttpClient client = new OkHttpClient();

    /**
     * Метод для установки слушателя обновления данных о бумаге
     */
    private static TransactionHistoryCallback transactionHistoryCallback;
    /**
     * Позволяет активити установить себя в качестве слушателя баланса
     */
    public static void setTransactionHistoryCallback(TransactionHistoryCallback callback) {
        transactionHistoryCallback = callback;
    }

    /**
     * Метод получения списка ценных бумаг
     * @param context - контекст (активити), для получения токена из sharedPreferences
     */
    public static void GetListHistoryTransactionsInPortfolio(Context context, Integer portfolio_id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        Request request = new Request.Builder()
                .url(serverUrl + "portfolio/transaction/" + portfolio_id.toString() + "/")
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
                        if (transactionHistoryCallback != null) {
                            transactionHistoryCallback.onTransactionHistoryReceived(jsonResponse);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
