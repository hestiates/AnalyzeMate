package com.example.analyzemate.Controllers.Interfaces;

import com.example.analyzemate.Models.StockPaper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public interface StockPaperCallback {
    void onStockPaperReceived(String json) throws IOException;
    void onStockPaperListReceived(JSONArray jsonArray) throws JSONException, IOException;
}
