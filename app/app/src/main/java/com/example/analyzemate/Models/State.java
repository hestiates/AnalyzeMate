package com.example.analyzemate.Models;

public class State {
    private  String uid;
    private Integer bankResource;
    private String name;
    private String cost;
    private String trend;

    public State(String uid, String name, Integer bank, String cost, String trend) {
        this.uid = uid;
        this.name = name;
        this.bankResource = bank;
        this.cost = cost;
        this.trend = trend;
    }

    public String getUID() {
        return this.uid;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBankResource() {
        return this.bankResource;
    }

    public void setBankResource(int bankResource) {
        this.bankResource = bankResource;
    }

    public String getCost() {
        return this.cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTrend() {
        return this.trend;
    }

    public void setTrend(String trend) {
        this.trend = (trend + "%");
    }
}
