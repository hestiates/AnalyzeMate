package com.example.analyzemate.Controllers.Interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.analyzemate.Models.Constants;
import com.example.analyzemate.Models.StockPaperToUI;
import com.example.analyzemate.Views.Autorization.SuccessRegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TrackHandler {

    static OkHttpClient client = new OkHttpClient();


    public static void SetTrackStockPaper(Context context, String ticker) {
        String ticketJson = "[\"" + ticker + "\"]";

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        Request request = new Request.Builder()
                .url(serverUrl + "notification/subscribe/" + ticker)
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

                    // Показ сообщения в главном потоке UI интерфейса
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Отслеживание начато", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public static void UnSetTrackStockPaper(Context context, String ticker) {
        String ticketJson = "[\"" + ticker + "\"]";

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        Request request = new Request.Builder()
                .url(serverUrl + "notification/unsubscribe/" + ticker)
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

                    // Показ сообщения в главном потоке UI интерфейса
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Отслеживание закончато", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}

