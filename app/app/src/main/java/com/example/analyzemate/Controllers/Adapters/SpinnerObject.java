package com.example.analyzemate.Controllers.Adapters;

import androidx.annotation.NonNull;

public class SpinnerObject {
    private String name;
    private int value;

    public SpinnerObject(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
