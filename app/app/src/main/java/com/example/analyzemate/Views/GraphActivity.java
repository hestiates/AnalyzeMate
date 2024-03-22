package com.example.analyzemate.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.analyzemate.Controllers.StockPaper.IntervalDayXAxisValueFormatter;
import com.example.analyzemate.Controllers.StockPaper.GraphController;
import com.example.analyzemate.Models.EnumTimeframe;
import com.example.analyzemate.Models.StockPaper;
import com.example.analyzemate.R;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private final String[] timeframes = { "М30", "Ч1", "Ч4", "1Д"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        // Инициализация граф
        CandleStickChart candleStickChart = findViewById(R.id.candleStick);

        // Обработка spinner таймфрейма
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, timeframes);

        // Получение таймфрейма и от спиннера

        EnumTimeframe timeframe = GraphController.SetAndListenSpinnerTimeframe(adapter, spinner,
                candleStickChart);


        ///////// РАБОТА С ГРАФОМ
        // Инициализация граф
        SetGraph(timeframe, candleStickChart);
    }

    /**
     * Создание и обоновление графа
     * @param timeframe
     */
    public static void SetGraph(EnumTimeframe timeframe, CandleStickChart candleStickChart ) {
        // Описание какое-то внизу справа
        candleStickChart.getDescription().setText("GFG");

        // Парсинг json
        StockPaper stockPaper = GraphController.getStockPaper(timeframe);

        // Задание свеч для графика
        List<CandleEntry> entries = GraphController.getCandleEntries(stockPaper);

        // Ось X, задание значений времени
        SetAxis(candleStickChart, stockPaper);

        // Создание датасета из массива свечей entries
        CandleData data = getCandleData(entries, stockPaper.getTicker());

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
    private static void SetAxis(CandleStickChart candleStickChart, StockPaper stockCandleData) {
        XAxis xAxis = candleStickChart.getXAxis();

        // Форматирование оси X
        IntervalDayXAxisValueFormatter formatter = new IntervalDayXAxisValueFormatter(stockCandleData);

        // Установка форматтера
        xAxis.setValueFormatter(formatter);

//        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);

        YAxis yAxisLeft = candleStickChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
    }
}