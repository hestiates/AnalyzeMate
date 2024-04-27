package com.example.analyzemate.Controllers.Interfaces;

import com.example.analyzemate.Models.ExistingUser;

/**
 * Интерфейс обратного вызова, который оповещает
 * активити об обновлении баланса
 */
public interface OnBalanceUpdateListener {
    void onBalanceUpdated(float balance);
    void existingUserReceived(ExistingUser existingUser);
}
