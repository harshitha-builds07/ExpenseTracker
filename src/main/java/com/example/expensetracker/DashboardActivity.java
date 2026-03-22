package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import java.util.*;

import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.*;
import android.graphics.Color;

public class DashboardActivity extends AppCompatActivity {

    TextView txtDashTotal, txtDashCount;
    ListView listDash;
    PieChart pieChart;
    BarChart barChart;

    DBHelper db;
    ArrayList<ExpenseModel> list;
    ArrayList<String> displayList;
    ArrayAdapter<String> adapter;

    int total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtDashTotal = findViewById(R.id.txtDashTotal);
        txtDashCount = findViewById(R.id.txtDashCount);
        listDash = findViewById(R.id.listDash);
        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);

        db = new DBHelper(this);

        list = new ArrayList<>();
        displayList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                displayList);

        listDash.setAdapter(adapter);

        loadDashboard();
        loadPieChart();
        loadBarChart();

        listDash.setOnItemLongClickListener((p,v,pos,id)->{
            ExpenseModel m = list.get(pos);
            db.deleteExpense(m.getId());
            Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show();
            loadDashboard();
            loadPieChart();
            loadBarChart();
            return true;
        });
    }

    private void loadDashboard(){

        Cursor c = db.getAllData();

        list.clear();
        displayList.clear();
        total=0;
        int count=0;

        while(c.moveToNext()){
            int id=c.getInt(0);
            String note=c.getString(1);
            String cat=c.getString(2);
            int amt=c.getInt(3);

            list.add(new ExpenseModel(id,note,cat,amt));
            displayList.add("₹"+amt+" - "+note+" ("+cat+")");

            total+=amt;
            count++;
        }
        c.close();

        txtDashTotal.setText("Total: ₹"+total);
        txtDashCount.setText("Transactions: "+count);
        adapter.notifyDataSetChanged();
    }

    private void loadPieChart(){

        ArrayList<PieEntry> entries=new ArrayList<>();
        String[] cats={"Food","Travel","Bills","Shopping","Others"};

        for(String c:cats){
            int s=db.getCategorySum(c);
            if(s>0) entries.add(new PieEntry(s,c));
        }

        PieDataSet set=new PieDataSet(entries,"");
        set.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data=new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void loadBarChart(){

        ArrayList<BarEntry> entries=new ArrayList<>();
        String[] cats={"Food","Travel","Bills","Shopping","Others"};

        for(int i=0;i<cats.length;i++){
            int sum=db.getCategorySum(cats[i]);
            entries.add(new BarEntry(i,sum));
        }

        BarDataSet set=new BarDataSet(entries,"Categories");
        set.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData data=new BarData(set);
        barChart.setData(data);
        barChart.invalidate();
    }
}
