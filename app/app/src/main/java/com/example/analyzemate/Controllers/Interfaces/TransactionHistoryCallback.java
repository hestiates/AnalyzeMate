package com.example.analyzemate.Controllers.Interfaces;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;

public interface TransactionHistoryCallback {
    void onTransactionHistoryReceived(JSONArray jsonArray) throws JSONException, IOException, ParseException;
}
