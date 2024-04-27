package com.example.analyzemate.Models;

import java.util.Date;

public class ExistingUser {
    public Integer id;
    public String email;

    public boolean is_active;
    public boolean is_superuser;

    public boolean is_verified;

    public Float balance;

    public String patronymic;

    public String name;

    public String surname;

    public String birthdate;

    public String config;

    public ExistingUser(Integer id, String email, boolean is_active, boolean is_superuser, boolean is_verified, Float balance, String patronymic, String name, String surname, String birthdate, String config) {
        this.id = id;
        this.email = email;
        this.is_active = is_active;
        this.is_superuser = is_superuser;
        this.is_verified = is_verified;
        this.balance = balance;
        this.patronymic = patronymic;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.config = config;
    }
}
