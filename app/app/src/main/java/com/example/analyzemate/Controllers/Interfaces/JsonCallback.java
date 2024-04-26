package com.example.analyzemate.Controllers.Interfaces;

import java.io.IOException;

public interface JsonCallback {
    void onSuccess(String json) throws IOException;
    void onFailure(IOException e);
}
