package com.example.analyzemate.Models;

/**
 * Класс свеча
 */
public class Candle {
    private Long time; // Миллисекунды
    private float open;
    private float close;
    private float high;
    private float low;
    private int volume;

    public Candle(Long time, float open, float close, float high, float low, int volume) {
        this.time = time;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }

    public float getClose() {
        return close;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public float getOpen() {
        return open;
    }

    public int getVolume() {
        return volume;
    }

    public Long getTime() {
        return time;
    }
}
