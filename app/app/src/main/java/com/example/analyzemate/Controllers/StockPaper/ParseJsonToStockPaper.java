package com.example.analyzemate.Controllers.StockPaper;

import com.example.analyzemate.Models.StockPaper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Класс для парсинга из json в StockPaper
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
        stockPaper.setCandles();
        return stockPaper;
    }
}
