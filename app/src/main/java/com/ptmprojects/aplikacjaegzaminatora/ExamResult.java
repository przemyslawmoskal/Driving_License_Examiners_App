package com.ptmprojects.aplikacjaegzaminatora;

import java.util.Calendar;
import java.util.UUID;

public class ExamResult {

    int date;
    int orderNumber;
    String category;
    int result;
    UUID id;


    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ExamResult() {
        this(UUID.randomUUID());
    }

    public ExamResult(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
