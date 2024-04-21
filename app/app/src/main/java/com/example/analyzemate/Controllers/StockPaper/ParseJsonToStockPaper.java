package com.example.analyzemate.Controllers.StockPaper;

import android.content.Context;

import com.example.analyzemate.Controllers.Interfaces.StockPaperHandler;
import com.example.analyzemate.Models.EnumTimeframe;
import com.example.analyzemate.Models.StockPaper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Класс для парсинга из json в StockCandleData
 */
public class ParseJsonToStockPaper {
    /**
     * Парсинг из json в StockPaper
     * @param json - строка json
     * @return StockPaper
     * @throws IOException - ошибка чтения
     */
    public static StockPaper JsonToStockPaper(String json) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        StockPaper stockPaper = objectMapper.readValue(json, StockPaper.class);

        return stockPaper;
    }

    /**
     * Запрос серверу по таймфрейму, обработка и возврат stockPaper
     * @param timeframe - таймфрейм, заданный пользователем
     * @return StockPaper - данные о бумаге
     * @throws IOException ошибка чтения
     */
    public static StockPaper GetStockPaperFromServer(Context context, String ticker, EnumTimeframe timeframe) throws IOException {
        // Запрос к серверу по таймфрейму и тикеру
        String json = StockPaperHandler.GetJSONStockPaperFromServer(context, ticker, timeframe);

        // Создаем объект класса StockPaper - данные о бумаге
        StockPaper stockPaper = JsonToStockPaper(json);
        stockPaper.setCandles(); // Задаем свечи

        return stockPaper;
    }
}
