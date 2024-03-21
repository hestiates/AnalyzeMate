package com.example.analyzemate.Controllers.Candle;

import com.example.analyzemate.Models.Candle;
import com.example.analyzemate.Models.StockCandleData;
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
    private final SimpleDateFormat dateFormat;
    private List<Candle> candles;

    public IntervalDayXAxisValueFormatter(StockCandleData stockCandleData) {
        dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        this.candles = stockCandleData.getCandles();
    }

    /**
     * Форматте для создания оси X.
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
