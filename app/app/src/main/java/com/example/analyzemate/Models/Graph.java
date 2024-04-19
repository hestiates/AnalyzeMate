package com.example.analyzemate.Models;

import androidx.annotation.NonNull;

import com.example.analyzemate.Controllers.StockPaper.ParseJsonToStockPaper;
import com.github.mikephil.charting.charts.CombinedChart;

import java.io.IOException;

/**
 * Класс графа с графом для рисования, таймфремом, данным о бумаге
 */
public class Graph {
    public CombinedChart combinedChart;
    String ticker;
    public EnumTimeframe timeframe;
    public StockPaper stockPaper;
    public EnumIndicator indicator = null;


    public Graph(CombinedChart combinedChart, EnumTimeframe timeframe, EnumIndicator indicator, String ticker) {
        this.combinedChart = combinedChart;
        this.timeframe = timeframe;
        this.ticker = ticker;

        if (indicator != null) {
            this.indicator = indicator;
        }

        // Парсинг json и получение данных о бумаге
        stockPaper = getStockPaper();
    }

    /**
     * Получение данных о бумаге
     * @return stockPaper данные о бумаге
     */
    @NonNull
    private StockPaper getStockPaper() {
        StockPaper stockPaper;

        try {
            // Получаем информацию о бумаге
            stockPaper = ParseJsonToStockPaper.GetStockPaperFromServer(ticker, timeframe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stockPaper;
    }
}