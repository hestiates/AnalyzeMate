package com.example.analyzemate.Models;

import java.text.DateFormat;
import java.util.Date;

public class User {
    public String login;
    public String name;
    public String surname;
    public String patronymic;
    public Date date;
    public Float balance;
    public String password;

    public User(String login, String name, String surname, String patronymic, Date date,
                String password) {
       this.login = login;
       this.name = name;
       this.surname = surname;
       this.patronymic = patronymic;
       this.date = date;
       this.balance = 0.0F;
       this.password = password;
    }
}
