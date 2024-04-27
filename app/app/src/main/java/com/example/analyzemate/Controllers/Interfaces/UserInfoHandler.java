package com.example.analyzemate.Controllers.Interfaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.analyzemate.Models.Constants;
import com.example.analyzemate.Models.ExistingUser;
import com.example.analyzemate.Models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class UserInfoHandler {

    static OkHttpClient client = new OkHttpClient();

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
    static float balance;

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
                        balance = (float) jsonResponse.getDouble("balance");


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

    private static void GetCurrentUser(Context context, UserInfoCallback callback) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        // формирование запроса
        Request request = new Request.Builder()
                .url(serverUrl + "users/me")
                .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                .build();

        Log.d("Request", "Request headers: " + request.header("Authorization"));

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

                    try {
                        JSONObject jsonResponse = new JSONObject(responseBodyString);

                        // формирование ответа
                        Integer id = jsonResponse.getInt("id");
                        String email = jsonResponse.getString("email");
                        boolean is_active = Boolean.parseBoolean(jsonResponse.getString("is_active"));
                        boolean is_superuser = Boolean.parseBoolean(jsonResponse.getString("is_superuser"));
                        boolean is_verified = Boolean.parseBoolean(jsonResponse.getString("is_verified"));
                        Float balance = (float) jsonResponse.getDouble("balance");
                        String patronymic = jsonResponse.getString("patronymic");
                        String name = jsonResponse.getString("name");
                        String surname = jsonResponse.getString("surname");
                        String birthdate = jsonResponse.getString("birthdate");
                        String config = jsonResponse.getString("config");

                        ExistingUser user_from_server = new ExistingUser(id, email, is_active, is_superuser, is_verified,
                                balance, patronymic, name, surname, birthdate, config);

                        callback.onSuccess(user_from_server);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    public static void UpdateUserBalance(Context context, Float user_balance) throws JSONException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        GetCurrentUser(context, new UserInfoCallback() {
            @Override
            public void onSuccess(ExistingUser existingUser) throws JSONException {
                JSONObject jsonBody = new JSONObject();

                jsonBody.put("id", existingUser.id);
                jsonBody.put("email", existingUser.email);
                jsonBody.put("is_active", existingUser.is_active);
                jsonBody.put("is_superuser", existingUser.is_superuser);
                jsonBody.put("is_verified", existingUser.is_verified);
                jsonBody.put("balance", user_balance);
                jsonBody.put("patronymic", existingUser.patronymic);
                jsonBody.put("name", existingUser.name);
                jsonBody.put("surname", existingUser.surname);
                jsonBody.put("birthdate", existingUser.birthdate);
                jsonBody.put("config", new JSONObject());

                RequestBody requestBody = RequestBody.create(String.valueOf(jsonBody),
                        MediaType.parse("application/json"));

                // формирование запроса
                Request request = new Request.Builder()
                        .url(serverUrl + "users/me")
                        .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                        .patch(requestBody)
                        .build();

                Log.d("Request", "Sending request to: " + serverUrl + "users/me");
                Log.d("Request", "Request headers: " + request.header("Authorization"));
                Log.d("Request", "Request body: " + jsonBody.toString());

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
                                balance = (float) jsonResponse.getDouble("balance");
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
        });
    }
}
