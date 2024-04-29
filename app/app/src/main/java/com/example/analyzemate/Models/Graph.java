package com.example.analyzemate.Models;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.analyzemate.Controllers.Interfaces.StockPaperCallback;
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


    public Graph(Context context, CombinedChart combinedChart, EnumTimeframe timeframe, EnumIndicator indicator, String ticker) {
        this.combinedChart = combinedChart;
        this.timeframe = timeframe;
        this.ticker = ticker;

        if (indicator != null) {
            this.indicator = indicator;
        }

        // Парсинг json и получение данных о бумаге
        getStockPaper(context);
    }

    /**
     * Получение данных о бумаге
     * @return stockPaper данные о бумаге
     */
    private void getStockPaper(Context context) {
        ParseJsonToStockPaper.GetStockPaperFromServer(context, ticker, timeframe, new StockPaperCallback() {
            @Override
            public void onStockPaperReceived(StockPaper stockPaper) {
                Graph.this.stockPaper = stockPaper;
            }
        });
    }
}