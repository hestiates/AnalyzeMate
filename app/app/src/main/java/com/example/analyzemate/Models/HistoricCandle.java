package com.example.analyzemate.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoricCandle {
    private float open;
    private float close;
    private float highest;
    private float lowest;
    private int volume;
    private String ticker;
    private int timeframe;
    private long timestamp;

    @JsonCreator
    public HistoricCandle(@JsonProperty("open") float open,
                          @JsonProperty("close") float close,
                          @JsonProperty("highest") float highest,
                          @JsonProperty("lowest") float lowest,
                          @JsonProperty("volume") int volume,
                          @JsonProperty("ticker") String ticker,
                          @JsonProperty("timeframe") int timeframe,
                          @JsonProperty("timestamp") long timestamp) {
        this.open = open;
        this.close = close;
        this.highest = highest;
        this.lowest = lowest;
        this.volume = volume;
        this.ticker = ticker;
        this.timeframe = timeframe;
        this.timestamp = timestamp;
    }

    // Добавьте геттеры и setterы
    int getIntTimeframe(){
        return timeframe;
    }

    float getOpen(){
        return open;
    }

    float getClose(){
        return close;
    }

    float getHighest() {
        return  highest;
    }

    float getLowest() {
        return lowest;
    }

    long getTimestamp() {
        return timestamp;
    }

    int getVolume() {
        return volume;
    }
}
