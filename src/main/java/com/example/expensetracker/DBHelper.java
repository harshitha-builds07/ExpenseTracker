package com.example.expensetracker;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "expense.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE expenses(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "note TEXT," +
                "category TEXT," +
                "amount INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS expenses");
        onCreate(db);
    }

    public void insertExpense(String note,String category,int amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("note",note);
        cv.put("category",category);
        cv.put("amount",amount);
        db.insert("expenses",null,cv);
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM expenses",null);
    }

    public int getTotalExpense(){
        int total = 0;
        Cursor c = getReadableDatabase()
                .rawQuery("SELECT SUM(amount) FROM expenses",null);
        if(c.moveToFirst()) total = c.getInt(0);
        c.close();
        return total;
    }

    public int getCategorySum(String cat){
        int sum = 0;
        Cursor c = getReadableDatabase()
                .rawQuery("SELECT SUM(amount) FROM expenses WHERE category=?",
                        new String[]{cat});
        if(c.moveToFirst()) sum = c.getInt(0);
        c.close();
        return sum;
    }

    public void deleteExpense(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("expenses","id=?",new String[]{String.valueOf(id)});
    }
}
