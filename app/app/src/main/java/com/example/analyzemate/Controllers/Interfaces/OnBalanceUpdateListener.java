package com.example.analyzemate.Controllers.Interfaces;

/**
 * Интерфейс обратного вызова, который оповещает
 * активити об обновлении баланса
 */
public interface OnBalanceUpdateListener {
    void onBalanceUpdated(double balance);
}
