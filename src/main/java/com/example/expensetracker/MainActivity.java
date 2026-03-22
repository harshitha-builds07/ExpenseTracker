package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    EditText edtAmount, edtNote;
    Button btnAdd, btnDashboard;
    TextView txtTotal;
    Spinner spCategory;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        edtNote = findViewById(R.id.edtNote);
        btnAdd = findViewById(R.id.btnAdd);
        btnDashboard = findViewById(R.id.btnDashboard);
        txtTotal = findViewById(R.id.txtTotal);
        spCategory = findViewById(R.id.spCategory);

        db = new DBHelper(this);

        // Spinner data
        String[] cats = {"Food","Travel","Bills","Shopping","Others"};

        ArrayAdapter<String> ad =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        cats);

        spCategory.setAdapter(ad);

        loadTotal();

        btnAdd.setOnClickListener(v -> addExpense());

        btnDashboard.setOnClickListener(v ->
                startActivity(new Intent(
                        MainActivity.this,
                        DashboardActivity.class)));
    }

    private void addExpense() {

        String amountStr = edtAmount.getText().toString();
        String note = edtNote.getText().toString();
        String category = spCategory.getSelectedItem().toString();

        if(amountStr.isEmpty()){
            Toast.makeText(this,
                    "Enter amount",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int amount = Integer.parseInt(amountStr);

        db.insertExpense(note, category, amount);

        Toast.makeText(this,
                "Expense added",
                Toast.LENGTH_SHORT).show();

        edtAmount.setText("");
        edtNote.setText("");

        loadTotal();
    }

    private void loadTotal() {
        int total = db.getTotalExpense();
        txtTotal.setText("Total Spent: ₹" + total);
    }
}
