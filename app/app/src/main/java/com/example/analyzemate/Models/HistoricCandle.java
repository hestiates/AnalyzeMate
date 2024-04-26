package com.example.analyzemate.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoricCandle {
    @JsonProperty("open")
    public float open;

    @JsonProperty("close")
    public float close;

    @JsonProperty("highest")
    public float highest;

    @JsonProperty("lowest")
    public float lowest;

    @JsonProperty("volume")
    public int volume;

    @JsonProperty("ticker")
    public String ticker;

    @JsonProperty("timeframe")
    public int intTimeframe;

    @JsonProperty("timestamp")
    public long timestamp;


    public HistoricCandle(float open, float close, float highest, float lowest, int volume,
                          String ticker, int intTimeframe, long timestamp) {
        this.open = open;
        this.close = close;
        this.highest = highest;
        this.lowest = lowest;
        this.volume = volume;
        this.ticker = ticker;
        this.intTimeframe = intTimeframe;
        this.timestamp = timestamp;
    }
}