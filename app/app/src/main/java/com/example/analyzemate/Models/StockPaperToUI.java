package com.example.analyzemate.Models;

public class StockPaperToUI {
    public String ticker;
    public String name;
    public Double price;

    public StockPaperToUI(String ticker, String name, Double price) {
        this.ticker = ticker;
        this.name = name;
        this.price = price;
    }
}
