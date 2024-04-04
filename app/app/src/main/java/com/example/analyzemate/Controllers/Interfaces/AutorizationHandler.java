package com.example.analyzemate.Controllers.Interfaces;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.analyzemate.Models.User;
import com.example.analyzemate.Views.Autorization.LoginActivity;
import com.example.analyzemate.Views.Autorization.SuccessRegisterActivity;
import com.example.analyzemate.Views.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AutorizationHandler {

    static String serverUrl = "http://192.168.3.234:8000/";

    public static void CheckStartup() {

        OkHttpClient client = new OkHttpClient();

        // HTTP-запрос (request)
        Request request = new Request.Builder() // TODO не работает
                .url(serverUrl + "check_startup/")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Получение HTTP-ответа (response)
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }

                    // пример получения всех заголовков ответа
                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        // вывод заголовков
                        System.out.println(responseHeaders.name(i) + ": "
                                + responseHeaders.value(i));
                    }
                    // вывод тела ответа
                    System.out.println(responseBody.string());

                    // TODO проверка

                }
            }
        });
    }

    /**
     * Регистрация пользователя, запрос к серверу
     * @param user - данные о пользователя
     */
    public static void RegisterUser(User user, Context context) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String jsonRequest;
        // Создание текста запроса
        JSONObject json = new JSONObject();
        try {
            json.put("email", user.login);
            json.put( "password", user.password);
            json.put( "is_active", true);
            json.put( "is_superuser", false);
            json.put( "is_verified", false);
            json.put( "balance", user.balance);
            json.put( "patronymic", user.patronymic);
            json.put( "name", user.name);
            json.put( "surname", user.surname);

            // Форматирование даты в соответствии с требованиями JSON
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            String formattedDate = dateFormat.format(user.date);
            json.put("birthdate", formattedDate);

            json.put("config", new JSONObject()); // Пустой JSON объект для "config"

            // Преобразование JSONObject в строку
            jsonRequest = json.toString();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonRequest, JSON);
        Request.Builder requestBuilder = new Request.Builder().url(serverUrl + "auth/register/").post(body);
        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Получение HTTP-ответа (response)
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        // Обработка ответа
                        String responseBodyString = responseBody.string();
                        JSONObject jsonResponse = new JSONObject(responseBodyString);
                        String detail = jsonResponse.getString("detail");

                        // Проверка, что пользователь не существует
                        if (detail.equals("REGISTER_USER_ALREADY_EXISTS")) {
                            // Показ сообщения в главном потоке UI интерфейса
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Такой пользователь уже существует", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else { // Другие ошибки
                            // Показ сообщения в главном потоке UI интерфейса
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Проверьте данные", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }

                    // Если все успешно
                    // Показ сообщения в главном потоке UI интерфейса
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Успешная регистрация", Toast.LENGTH_SHORT).show();
                            // Переходим в другое активити
                            Intent intent = new Intent(context, SuccessRegisterActivity.class);
                            context.startActivity(intent);
                        }
                    });

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    /**
     * Аутентификация пользователя, запрос к серверу
     *
     * @param user - данные пользователя, необходимы user.login и user.password
     */
    public static void AuthenticationUser(User user, Context context) {
        OkHttpClient client = new OkHttpClient();

        // Формирование запроса в формате x-www-form-urlencoded
        RequestBody formBody = new FormBody.Builder()
                .add("username", user.login)
                .add("password", user.password)
                .build();

        Request request = new Request.Builder()
                .url(serverUrl + "auth/login/")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Получение HTTP-ответа (response)
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        // Показ сообщения в главном потоке UI интерфейса
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Неверный пароль или логин", Toast.LENGTH_SHORT).show();
                            }
                        });

                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }

                    String responseBodyString = response.body().string();

                    // Разбор JSON-ответа для получения токена
                    JSONObject jsonResponse = new JSONObject(responseBodyString);

                    String token = jsonResponse.getString("access_token");

                    // Сохранение токена в SharedPreferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.apply();

                    // Показ сообщения в главном потоке UI интерфейса
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Успешная аутентификация", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                    });

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
