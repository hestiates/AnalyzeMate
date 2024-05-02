package com.example.analyzemate.Views.PaperFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.analyzemate.Controllers.Interfaces.StockPaperCallback;
import com.example.analyzemate.Controllers.Interfaces.StockPaperHandler;

import androidx.fragment.app.Fragment;

import com.example.analyzemate.Controllers.Interfaces.StockPaperToUICallback;
import com.example.analyzemate.Models.StockPaperToUI;
import com.example.analyzemate.R;

public class OverviewFragment extends Fragment {
    TextView textView_description;
    TextView textView_price;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        textView_description = view.findViewById(R.id.textView_description);
        textView_price = view.findViewById(R.id.textView_cost);

        assert getArguments() != null;
        String description_text = getArguments().getString("ticker");

        StockPaperHandler.GetStockPaperFromServer(view.getContext(), description_text, new StockPaperToUICallback() {
            @Override
            public void StockPaperToUIReceived(final StockPaperToUI stockPaperToUI) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String name = stockPaperToUI.name;
                        String cost = String.valueOf(stockPaperToUI.price);

                        textView_description.setText(name);
                        textView_price.setText(cost);
                    }
                });
            }
        });

        return view;
    }
}