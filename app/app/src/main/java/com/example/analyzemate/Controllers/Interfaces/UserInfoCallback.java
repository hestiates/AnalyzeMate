package com.example.analyzemate.Controllers.Interfaces;

import com.example.analyzemate.Models.ExistingUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public interface UserInfoCallback {
    void onSuccess(ExistingUser existingUser) throws IOException, JSONException;
}
