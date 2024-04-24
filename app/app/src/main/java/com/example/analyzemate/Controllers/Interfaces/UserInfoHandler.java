package com.example.analyzemate.Controllers.Interfaces;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.analyzemate.Models.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UserInfoHandler {
    /**
     * Метод для установки слушателя обновления баланса
     */
    private static OnBalanceUpdateListener balanceUpdateListener;
    /**
     * Позволяет активити установить себя в качестве слушателя баланса
     */
    public static void setBalanceUpdateListener(OnBalanceUpdateListener listener) {
        balanceUpdateListener = listener;
    }
    static double balance;

    /**
     * Функция, которая делает запрос к серверу и по токену получает баланс пользователя
     *
     * @return баланс пользователя в формате int
     */
    public static void getBalanceFromServer(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        // формирование запроса
        Request request = new Request.Builder()
                .url(serverUrl + "users/me")
                .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                .build();

        OkHttpClient client = new OkHttpClient();

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
                    // получение баланса из блока ответа сервера
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBodyString);
                        balance = jsonResponse.getDouble("balance");
                        // уведомляем слушателя обновления баланса
                        // if нужен для проверки, что активити ждет обновление баланса
                        if (balanceUpdateListener != null) {
                            balanceUpdateListener.onBalanceUpdated(balance);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

}
