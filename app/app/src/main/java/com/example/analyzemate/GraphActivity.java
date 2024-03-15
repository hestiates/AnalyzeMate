package com.example.analyzemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import com.example.analyzemate.Candle.Candle;
import com.example.analyzemate.Candle.IntervalDayXAxisValueFormatter;
import com.example.analyzemate.Candle.ParseJsonToStockCandleData;
import com.example.analyzemate.Candle.StockCandleData;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Инициализация граф
        CandleStickChart candleStickChart = findViewById(R.id.candleStick);
        // Описание какое-то внизу справа
        candleStickChart.getDescription().setText("GFG");

        // Парсинг json
        StockCandleData stockCandleData = getStockCandleData("path");

        // Задание свеч для графика
        List<CandleEntry> entries = getCandleEntries(stockCandleData);

        // Ось X, задание значений времени
        SetAxis(candleStickChart, stockCandleData);

        // Создание датасета из массива свечей entries
        CandleData data = getCandleData(entries, stockCandleData.getTicker());

        // Задание данных и отрисовка графика
        candleStickChart.setData(data);
        candleStickChart.invalidate();
    }

    /**
     * Задание CandleDataSet
     * @param entries - массив свечей для графика
     * @param ticker - тикер бумаги
     * @return CandleDataSet
     */
    @NonNull
    private static CandleData getCandleData(List<CandleEntry> entries, String ticker) {
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

    /**
     * Задание осей X и Y
     * @param candleStickChart - график свечей
     * @param stockCandleData - данные о бумаге
     */
    private static void SetAxis(CandleStickChart candleStickChart, StockCandleData stockCandleData) {
        XAxis xAxis = candleStickChart.getXAxis();

        // Форматирование оси X
        xAxis.setValueFormatter(new IntervalDayXAxisValueFormatter(stockCandleData));

//        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);

        YAxis yAxisLeft = candleStickChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
    }

    /**
     * Получение массива свечей для построения графика
     * @param stockCandleData - объект данных о бумаге
     * @return List<CandleEntry> - массив свечей для построения графика
     */
    @NonNull
    private static List<CandleEntry> getCandleEntries(StockCandleData stockCandleData) {
        List<CandleEntry> entries = new ArrayList<>();
        List<Candle> candles = stockCandleData.getCandles();

        for (int index = 0; index < stockCandleData.getCandles().size(); index++) {
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
     * Получение данных о бумаге из json по пути path
     * @param path - путь к файлу
     * @return StockCandleData - объект бумаги с данными свеч
     */
    @NonNull
    private static StockCandleData getStockCandleData(String path) {
        StockCandleData stockCandleData;
        // Передаем json в формате String и получаем информацию о бумаге
        try {
            // Получаем информацию о бумаге
            stockCandleData = ParseJsonToStockCandleData.JsonToCandleData(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stockCandleData;
    }
}