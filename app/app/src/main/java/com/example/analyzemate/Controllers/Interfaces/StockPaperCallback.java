package com.example.analyzemate.Controllers.Interfaces;

import com.example.analyzemate.Models.StockPaper;

import java.io.IOException;

public interface StockPaperCallback {
    void onStockPaperReceived(String json) throws IOException;
}
