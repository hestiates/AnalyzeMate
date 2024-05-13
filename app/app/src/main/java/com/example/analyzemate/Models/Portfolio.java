package com.example.analyzemate.Models;
import com.example.analyzemate.Models.State;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    public Integer id;
    public Float balance;
    public Integer idOwner;
    public ArrayList<State> securities = new ArrayList<State>();

    public Portfolio(Integer id, Float balance, Integer idOwner) {
        this.id = id;
        this.balance = balance;
        this.idOwner = idOwner;
    }

    public void AddToPortfolio(State paper) {
        securities.add(paper);
    }
}

