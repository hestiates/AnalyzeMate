package com.example.analyzemate.Controllers.Interfaces;

import com.example.analyzemate.Models.Portfolio;

import java.util.ArrayList;

public interface PortfolioCallback {
    void PortfolioReceived(ArrayList<Portfolio> portfolioList);
}
