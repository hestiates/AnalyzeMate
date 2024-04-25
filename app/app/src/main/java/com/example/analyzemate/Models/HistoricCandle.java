package com.example.analyzemate.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoricCandle {
    @JsonProperty("open")
    public double open;

    @JsonProperty("close")
    public double close;

    @JsonProperty("highest")
    public double highest;

    @JsonProperty("lowest")
    public double lowest;

    @JsonProperty("volume")
    public long volume;

    @JsonProperty("ticker")
    public String ticker;

    @JsonProperty("timeframe")
    public int intTimeframe;

    @JsonProperty("timestamp")
    public long timestamp;


    public HistoricCandle(double open, double close, double highest, double lowest, long volume,
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