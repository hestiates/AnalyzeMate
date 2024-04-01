package com.example.analyzemate.Models;

public class State {
    private int bankResource;
    private String name;

    public State(String name, int bank) {
        this.name = name;
        this.bankResource = bank;
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
}
