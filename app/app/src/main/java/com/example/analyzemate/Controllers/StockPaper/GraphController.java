package com.example.analyzemate.Controllers.StockPaper;

import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.example.analyzemate.Models.Candle;
import com.example.analyzemate.Models.StockPaper;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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

        int i = 0;
        for (int index = 0; index < stockPaper.getCandles().size(); index++) {
            i = index;
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

        // TODO Заглушка, чтобы последняя свеча отображалась не половинчатой.
        // ХЗ как это исправить, проблема только при использовании CombinedChart,
        // вместо CandleStickChart

        Candle candle = candles.get(i);
        entries.add(new CandleEntry(
                i+1,
                candle.getClose(),
                candle.getClose(),
                candle.getClose(),
                candle.getClose()));

        return entries;
    }


    /**
     * Получение массива точек EMA для построения графика
     * @param stockPaper - объект данных о бумаге
     * @return List<Entry> - массив точек для построения линейного графика
     */
    @NonNull
    public static List<Entry> getLineEntries(StockPaper stockPaper) {
        List<Entry> entries = new ArrayList<>();
        List<Candle> candles = stockPaper.getCandles();

        float EMA = 0;

        int countDays = 20; // Длина периода EMA (задается инвестором, но пофиг)
        float koef = (float) 2 / (countDays+1);


        for (int index = 0; index < stockPaper.getCandles().size(); index++) {
            Candle candle = candles.get(index);

            if (index == 0){
                EMA = candle.getClose();
                continue;
            }

            EMA = candle.getClose() * koef + EMA * (1- koef);

            entries.add(new Entry(index, EMA));
        }

        return entries;
    }


    /**
     * Задание CombinedData. Объединяет в один граф свечной и линейный (индикаторы)
     * @param candleEntries - массив свечей для свечного графика
     * @param lineEntries - массив значений для линейного графика
     * @param ticker - тикер бумаги
     * @return CombinedData
     */
    @NonNull
    public static CombinedData getCombinedData(List<CandleEntry> candleEntries,
                                               List<Entry> lineEntries, String ticker) {
        // Создание CandleData
        CandleData candleData = getCandleData(candleEntries, ticker);
        CombinedData combinedData = new CombinedData();

        combinedData.setData(candleData);

        if (lineEntries != null) {
            LineData lineData = getLineData(lineEntries, ticker);
            combinedData.setData(lineData);
        }

        return combinedData;
    }


    /**
     * Создание CandleData.
     * @param entries - массив свечей для свечного графика
     * @param ticker- тикер бумаги
     * @return CandleData
     */
    public static CandleData getCandleData(List<CandleEntry> entries, String ticker) {
        CandleDataSet dataSet = new CandleDataSet(entries, ticker);

        dataSet.setDrawIcons(false);
        dataSet.setIncreasingColor(Color.GREEN);  // Color for up (green) candlesticks
        dataSet.setIncreasingPaintStyle(Paint.Style.FILL); // Set the paint style to Fill for green candlesticks
        dataSet.setDecreasingColor(Color.RED);    // Color for down (red) candlesticks
        dataSet.setShadowColorSameAsCandle(true); // Using the same color for shadows as the candlesticks
        dataSet.setDrawValues(false);             // Hiding the values on the chart if not needed

        // Создание CandleData
        CandleData candleData = new CandleData(dataSet);

        return candleData;
    }


    /**
     * Создание LineData.
     * @param entries - массив значений для линейного графика
     * @param ticker- тикер бумаги
     * @return LineData
     */
    public static LineData getLineData(List<Entry> entries, String ticker) {
        LineDataSet dataSet = new LineDataSet(entries, ticker);

        dataSet.setDrawCircles(false);
        dataSet.setColor(Color.BLUE);

//        dataSet.setDrawIcons(false);
//        dataSet.setIncreasingColor(Color.GREEN);  // Color for up (green) candlesticks
//        dataSet.setIncreasingPaintStyle(Paint.Style.FILL); // Set the paint style to Fill for green candlesticks
//        dataSet.setDecreasingColor(Color.RED);    // Color for down (red) candlesticks
//        dataSet.setShadowColorSameAsCandle(true); // Using the same color for shadows as the candlesticks
//        dataSet.setDrawValues(false);             // Hiding the values on the chart if not needed

        // Создание CandleData
        LineData lineData = new LineData(dataSet);

        return lineData;
    }
}
