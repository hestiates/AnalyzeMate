package com.example.analyzemate.Views.PaperFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.analyzemate.Controllers.StockPaper.GraphController;
import com.example.analyzemate.Controllers.StockPaper.IntervalDayXAxisValueFormatter;
import com.example.analyzemate.Models.EnumTimeframe;
import com.example.analyzemate.Models.Graph;
import com.example.analyzemate.R;
import com.example.analyzemate.Views.GraphActivity;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleEntry;

import java.util.List;
import java.util.Objects;

public class GraphFragment extends Fragment {

    private final String[] timeframes = { "М30", "Ч1", "Ч4", "1Д"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graph, container, false);
        // Инициализация граф
        CandleStickChart candleStickChart = v.findViewById(R.id.candleStick);

        // Обработка spinner таймфрейма
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, timeframes);

        // Получение таймфрейма и от спиннера
        SetAndListenSpinnerTimeframe(adapter, spinner,
                candleStickChart);

        // Inflate the layout for this fragment
        return v;
    }

    /**
     * Создание и обоновление графа, отображение на экране
     * @param graph объект графа
     */
    public static void SetGraph(Graph graph) {
        // Описание какое-то внизу справа
        graph.candleStickChart.getDescription().setText("GFG");

        // Задание свеч для графика
        List<CandleEntry> entries = GraphController.getCandleEntries(graph.stockPaper);

        // Ось X, задание значений времени
        SetAxis(graph);

        // Создание датасета из массива свечей entries
        CandleData data = GraphController.getCandleData(entries, graph.stockPaper.getTicker());

        // Задание данных и отрисовка графика
        graph.candleStickChart.setData(data);
        graph.candleStickChart.invalidate();
    }

    /**
     * Обработка и задание спинера с таймфреймами
     * @param adapter - адаптер для слушания спиннера
     * @param spinner - спиннер с таймфремами
     * @param candleStickChart - график для рисования свечного графика
     */
    private static void SetAndListenSpinnerTimeframe(ArrayAdapter<String> adapter,
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

                EnumTimeframe timeframe;

                // Передаем timeframe
                if (Objects.equals(item, "М30")) {
                    timeframe = EnumTimeframe.CANDLE_INTERVAL_30_MIN;
                }
                else if (Objects.equals(item, "Ч1")) {
                    timeframe = EnumTimeframe.CANDLE_INTERVAL_HOUR;
                }
                else if (Objects.equals(item, "Ч4")) {
                    timeframe = EnumTimeframe.CANDLE_INTERVAL_4_HOUR;
                }
                else if (Objects.equals(item, "Д1")) {
                    timeframe = EnumTimeframe.CANDLE_INTERVAL_DAY;
                }
                else {
                    timeframe = EnumTimeframe.CANDLE_INTERVAL_DAY;
                }


                // TODO Сделать передачу между активити тикера бумаги
                String ticker = "SBER";
                Graph graph = new Graph(candleStickChart, timeframe, ticker);

                // Обновление графа (динамически)
                // UpdateGraph

                // Создание графа
                GraphActivity.SetGraph(graph);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    /**
     * Задание осей X и Y
     * @param graph - объект графа
     */
    private static void SetAxis(Graph graph) {
        XAxis xAxis = graph.candleStickChart.getXAxis();

        // Форматирование оси X
        IntervalDayXAxisValueFormatter formatter = new IntervalDayXAxisValueFormatter(graph.stockPaper);

        // Установка форматтера
        xAxis.setValueFormatter(formatter);

//        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);

        YAxis yAxisLeft = graph.candleStickChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
    }
}