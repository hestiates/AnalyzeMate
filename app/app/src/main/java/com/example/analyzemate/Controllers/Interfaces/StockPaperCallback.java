package com.example.analyzemate.Controllers.Interfaces;

import com.example.analyzemate.Models.StockPaper;

public interface StockPaperCallback {
    void onStockPaperReceived(StockPaper stockPaper);
}
