package com.example.analyzemate.Controllers.StockPaper;

import com.example.analyzemate.Models.Candle;
import com.example.analyzemate.Models.StockPaper;
import com.example.analyzemate.Models.EnumTimeframe;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Класс форматтер для создания значений оси X
 * Берутся значения времени для каждой свечи и ставятся в соответствии
 */
public class IntervalDayXAxisValueFormatter extends ValueFormatter {
    private SimpleDateFormat dateFormat;
    private final List<Candle> candles;
    private EnumTimeframe timeframe;


    public IntervalDayXAxisValueFormatter(StockPaper stockCandleData) {
        timeframe = EnumTimeframe.CANDLE_INTERVAL_DAY;
        dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());

        // Устанавливаем timeframe и dateFormat
        SetTimeframeAndDateFormat(stockCandleData);
        
        this.candles = stockCandleData.getCandles();
    }

    /**
     * Задани timeframe и dateformat
     * @param stockPaper данные о бумаге
     */
    public void SetTimeframeAndDateFormat(StockPaper stockPaper) {
        // Получение timeframe. В случае ошибки return
        try {
            timeframe = EnumTimeframe.valueOf(stockPaper.getTimeframe());
        }
        catch (Exception e) {
            return;
        }

        // Задание dateformat
        if (timeframe == EnumTimeframe.CANDLE_INTERVAL_4_HOUR ||
                timeframe == EnumTimeframe.CANDLE_INTERVAL_DAY) {
                dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        }
        else {
            dateFormat = new SimpleDateFormat("dd.HH:mm", Locale.getDefault());
        }
    }

    /**
     * Форматтер для создания оси X.
     * Значения времени в миллисекундах
     * @param value float to be formatted
     * @return dateFormat.format(date)
     */
    @Override
    public String getFormattedValue(float value) {

//        long milliseconds = (long) value * 24 * 60 * 60 * 1000;
        Date date;
        try {
            if ((value < 0) || (value >= candles.size())){
                return "Х";
            }
            date = new Date(candles.get((int) value).getTime());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return dateFormat.format(date);
    }
}
