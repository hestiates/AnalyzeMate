package com.example.analyzemate.Models;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


/**
 * Данные о бумаге
 */
public class StockPaper {

    @JsonProperty("ticker")
    private String ticker;

    private String timeframe;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("historic_candles")
    private List<HistoricCandle> historicCandles;

    private List<Candle> candles;


    public StockPaper() {
    }

    public StockPaper(String ticker, String timeframe, List<HistoricCandle> historicCandles) {
        this.ticker = ticker;
        this.timeframe = timeframe;
        this.historicCandles = historicCandles;
    }

    public String getTicker() {
        return ticker;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public List<HistoricCandle> getHistoricCandles() {
        return historicCandles;
    }

    public List<Candle> getCandles() {
        return candles;
    }

    /**
     * Получение данных из historic_cnadles в формате Candle
     * Установка таймфрейма в переменную
     * @return List<Candle>
     */
    public List<Candle> getCandlesFromHistory() {
        // Таймфрейм задаем
        int intTimeframe = historicCandles.get(0).intTimeframe;
        if (intTimeframe == 9) {
            this.timeframe = "CANDLE_INTERVAL_30_MIN";
        }
        else if (intTimeframe == 4) {
            timeframe = "CANDLE_INTERVAL_HOUR";
        }
        else if (intTimeframe == 11) {
            timeframe = "CANDLE_INTERVAL_4_HOUR";
        }
        else if (intTimeframe == 5) {
            timeframe = "CANDLE_INTERVAL_DAY";
        }

        List<Candle> candles = new ArrayList<>();

        for (int i = 0; i < historicCandles.size(); i++) {
            HistoricCandle historicCandle = historicCandles.get(i);
            if (historicCandle != null) {
                Long time = historicCandle.timestamp;
                float open = historicCandle.open;
                float close = historicCandle.close;
                float high = historicCandle.highest;
                float low = historicCandle.lowest;
                int volume = historicCandle.volume;
                candles.add(new Candle(time, open, close, high, low, volume));
            }
        }

        return candles;
    }

    public void setCandles() {
        this.candles = this.getCandlesFromHistory();
    }
}