package com.example.analyzemate.Controllers.StockPaper;

import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.example.analyzemate.Models.Candle;
import com.example.analyzemate.Models.StockPaper;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import java.util.ArrayList;
import java.util.List;


public class GraphController {
    /**
     * Получение массива свечей для построения графика
     * @param stockPaper - объект данных о бумаге
     * @return List<CandleEntry> - массив свечей для построения графика
     */
    @NonNull
    public static List<CandleEntry> getCandleEntries(StockPaper stockPaper) {
        List<CandleEntry> entries = new ArrayList<>();
        List<Candle> candles = stockPaper.getCandles();

        for (int index = 0; index < stockPaper.getCandles().size(); index++) {
            Candle candle = candles.get(index);

            // Если открытие и закрытие равно, то свеча по идее белая, но пока сделаем зеленой
            if (candle.getOpen() == candle.getClose()){
                candle = new Candle(candle.getTime(), candle.getOpen(),
                        candle.getClose() + candle.getClose() / 10000,
                        candle.getHigh(), candle.getLow(), candle.getVolume());
            }
            entries.add(new CandleEntry(
                    index,
                    candle.getHigh(),
                    candle.getLow(),
                    candle.getOpen(),
                    candle.getClose()));
        }
        return entries;
    }

    /**
     * Задание CandleDataSet
     * @param entries - массив свечей для графика
     * @param ticker - тикер бумаги
     * @return CandleDataSet
     */
    @NonNull
    public static CandleData getCandleData(List<CandleEntry> entries, String ticker) {
        CandleDataSet dataSet = new CandleDataSet(entries, ticker);

        dataSet.setDrawIcons(false);
        dataSet.setIncreasingColor(Color.GREEN);  // Color for up (green) candlesticks
        dataSet.setIncreasingPaintStyle(Paint.Style.FILL); // Set the paint style to Fill for green candlesticks
        dataSet.setDecreasingColor(Color.RED);    // Color for down (red) candlesticks
        dataSet.setShadowColorSameAsCandle(true); // Using the same color for shadows as the candlesticks
        dataSet.setDrawValues(false);             // Hiding the values on the chart if not needed

        // Создание CandleData
        CandleData data = new CandleData(dataSet);
        return data;
    }
}
