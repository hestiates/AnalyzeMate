package com.example.analyzemate.Controllers.Interfaces;

import com.example.analyzemate.Models.EnumTimeframe;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

public class StockPaperHandler {
    /**
     * Метод получения данных о бумаге по тикеру и таймфрейму с сервера
     *
     * @param ticker Тикер бумаги
     * @param timeframe Таймфрейм
     * @return json в строке
     */
    static OkHttpClient client = new OkHttpClient();

    private static Request CreateNewRequest(String ticker) {
        String ticketJson = "[\"" + ticker + "\"]";
        RequestBody body = RequestBody.create(ticketJson, MediaType.parse("application/json"));

        String token = "BIGGEST SECRET";

        return new Request.Builder()
                .url("http://localhost:8000/securities/?include_historic_candles=true")
                .addHeader("Authorization", "Bearer " + token)  // Добавляем заголовок с токеном
                .post(body)
                .build();
    }

    public static String GetJSONStockPaperFromServer(String ticker, EnumTimeframe timeframe) {
        String json = null;

        Request request = StockPaperHandler.CreateNewRequest(ticker);

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " + response.code() + " " + response.message());
            }
            json = response.body().string();
        } catch (IOException e) {
            System.out.println("Ошибка подключения: " + e);
        }

        return json;
    }
}
