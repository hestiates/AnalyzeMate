package com.example.analyzemate.Models;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


/**
 * Данные о бумаге
 */
public class StockPaper {
    @JsonProperty("figi")
    private String figi;

    @JsonProperty("TICKER")
    private String ticker;

    @JsonProperty("timeframe")
    private String timeframe;

    @JsonProperty("history")
    private List<List<Object>> history;

    private List<Candle> candles;

    public StockPaper() {
    }

    public StockPaper(String figi, String ticker, String timeframe, List<List<Object>> history) {
        this.figi = figi;
        this.ticker = ticker;
        this.timeframe = timeframe;
        this.history = history;
    }

    public String getFigi() {
        return figi;
    }

    public String getTicker() {
        return ticker;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public List<List<Object>> getHistory() {
        return history;
    }

    public List<Candle> getCandles() {
        return candles;
    }

    /**
     * Получение данных из history в формате Candle
     * @return List<Candle>
     */
    public List<Candle> getCandlesFromHistory() {
        List<Candle> candles = new ArrayList<>();
        for (int i = 1; i < history.size(); i++) {
            List<Object> row = history.get(i);
            if (row.size() == 6) {
                Long time = (Long) row.get(0);
                float open = ((Number) row.get(1)).floatValue();
                float close = ((Number) row.get(2)).floatValue();
                float high = ((Number) row.get(3)).floatValue();
                float low = ((Number) row.get(4)).floatValue();
                int volume = ((Number) row.get(5)).intValue();
                candles.add(new Candle(time, open, close, high, low, volume));
            }
        }

        return candles;
    }

    public void setCandles() {
        this.candles = this.getCandlesFromHistory();
    }
}