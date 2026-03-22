package com.example.expensetracker;

public class ExpenseModel {

    int id, amount;
    String note, category;

    public ExpenseModel(int id, String note,
                        String category, int amount) {
        this.id = id;
        this.note = note;
        this.category = category;
        this.amount = amount;
    }

    public int getId() { return id; }
    public String getNote() { return note; }
    public String getCategory() { return category; }
    public int getAmount() { return amount; }
}
