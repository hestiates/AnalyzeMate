package com.example.analyzemate.Controllers.Interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.analyzemate.Models.Constants;
import com.example.analyzemate.Models.User;
import com.example.analyzemate.Views.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

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

public class FireBaseHandler {
    public static void sendRegistrationToken(Context context, String registrationToken) {
        OkHttpClient client = new OkHttpClient();

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;

        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder()
                .url(serverUrl + "notification/subscribe/?registration_token=" + registrationToken)
                .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Получение HTTP-ответа (response)
                //
                int t = 0;
            }
        });
    }

    public static void getPushTest(Context context) {
        OkHttpClient client = new OkHttpClient();

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String serverUrl = Constants.SERVER_URL;


        Request request = new Request.Builder()
                .url(serverUrl + "send_test_notify/")
                .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Получение HTTP-ответа (response)
                //
            }
        });
    }
}
