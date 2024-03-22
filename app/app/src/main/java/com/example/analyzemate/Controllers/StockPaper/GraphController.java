package com.example.analyzemate.Controllers.StockPaper;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.analyzemate.Models.Candle;
import com.example.analyzemate.Models.EnumTimeframe;
import com.example.analyzemate.Models.StockPaper;
import com.example.analyzemate.R;
import com.example.analyzemate.Views.GraphActivity;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GraphController {

    private static EnumTimeframe timeframe = EnumTimeframe.CANDLE_INTERVAL_DAY;

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
     * Получение данных о бумаге из json по пути path
     * @param timeframe - таймфрейм, выбранный пользователем
     * @return StockCandleData - объект бумаги с данными свеч
     */
    @NonNull
    public static StockPaper getStockPaper(EnumTimeframe timeframe) {
        StockPaper stockPaper;
        // Передаем json в формате String и получаем информацию о бумаге
        try {
            // Получаем информацию о бумаге
            stockPaper = ParseJsonToStockPaper.GetStockPaperFromServer(timeframe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stockPaper;
    }

    /**
     * Обработка и задание спинера с таймфреймами
     * @param adapter
     * @param spinner
     * @return
     */
    public static EnumTimeframe SetAndListenSpinnerTimeframe(ArrayAdapter<String> adapter,
                                                             Spinner spinner,
                                                             CandleStickChart candleStickChart) {
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемента spinner
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);


        // Обработка его значений
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);

                // Передаем timeframe
                if (Objects.equals(item, "М30")) {
                    timeframe = EnumTimeframe.CANDLE_INTERVAL_30_MIN;
                    // CANDLE_INTERVAL_30_MIN
                }
                else if (Objects.equals(item, "Ч1")) {
                    timeframe = EnumTimeframe.CANDLE_INTERVAL_HOUR;
                    // CANDLE_INTERVAL_HOUR
                }
                else if (Objects.equals(item, "Ч4")) {
                    timeframe = EnumTimeframe.CANDLE_INTERVAL_4_HOUR;
                    // CANDLE_INTERVAL_4_HOUR
                }
                else if (Objects.equals(item, "Д1")) {
                    timeframe = EnumTimeframe.CANDLE_INTERVAL_DAY;
                    // CANDLE_INTERVAL_DAY
                }
                else {
                    timeframe = EnumTimeframe.CANDLE_INTERVAL_DAY;
                }

                // UpdateGraph
                GraphActivity.SetGraph(timeframe, candleStickChart);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        return timeframe;
    }
}
