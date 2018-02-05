package com.example.kamal.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by KAMAL on 2/5/2018.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static String DBNAME = "Test.db";
    private static String TABLE_NAME = "EXPENSETABLE";
    private static String COLM_ID = "ID";
    private static String COLM_TITLE = "TITLE";
    private static String COLM_AMT = "AMOUNT";
    private static String COLM_DATE = "DATE";
    private static String COLM_CATEGORY = "CATEGORY";
    private SQLiteDatabase database;

    public DbHelper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COLM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLM_TITLE + " VARCHAR, " + COLM_AMT + " VARCHAR, "
                + COLM_DATE + " VARCHAR, " + COLM_CATEGORY + " VARCHAR);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(ExpenseModel expenseModel) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLM_TITLE, expenseModel.getTitle());
        contentValues.put(COLM_AMT, expenseModel.getAmount());
        contentValues.put(COLM_DATE, expenseModel.getDate());
        contentValues.put(COLM_CATEGORY, expenseModel.getCategory());
        long result = database.insert(TABLE_NAME, null, contentValues);
        database.close();
        if (result != -1)
            return true;
        else
            return false;
    }

    public int updateData(ExpenseModel expenseModel) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLM_TITLE, expenseModel.getTitle());
        contentValues.put(COLM_AMT, expenseModel.getAmount());
        contentValues.put(COLM_DATE, expenseModel.getDate());
        contentValues.put(COLM_CATEGORY, expenseModel.getCategory());
        int result = database.update(TABLE_NAME, contentValues, COLM_ID + " = ?", new String[]{expenseModel.getID()});
        database.close();
        return result;
    }

    public int deleteRecord(ExpenseModel expenseModel) {
        database = this.getReadableDatabase();
        int result = database.delete(TABLE_NAME, COLM_ID + " = ?", new String[]{expenseModel.getID()});
        database.close();
        return result;
    }

    public ArrayList<ExpenseModel> getAllRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<ExpenseModel> expensesList = new ArrayList<>();
        ExpenseModel contactModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                contactModel = new ExpenseModel();
                contactModel.setID(cursor.getString(0));
                contactModel.setTitle(cursor.getString(1));
                contactModel.setAmount(cursor.getString(2));
                contactModel.setDate(cursor.getString(3));
                contactModel.setCategory(cursor.getString(4));
                expensesList.add(contactModel);
            }
        }
        cursor.close();
        database.close();
        return expensesList;
    }

    public double getExpenseForParticularDate(String date) {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, COLM_DATE + " = ?", new String[]{date}, null, null, null);
        ArrayList<ExpenseModel> expensesList = new ArrayList<>();
        ExpenseModel contactModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                contactModel = new ExpenseModel();
                contactModel.setID(cursor.getString(0));
                contactModel.setTitle(cursor.getString(1));
                contactModel.setAmount(cursor.getString(2));
                contactModel.setDate(cursor.getString(3));
                contactModel.setCategory(cursor.getString(4));
                expensesList.add(contactModel);
            }
        }
        cursor.close();
        database.close();

        double total = 0.0;
        for (ExpenseModel expenseModel : expensesList) {
            total = total + Double.parseDouble(expenseModel.getAmount());
        }
        return total;
    }

    public double getExpenseForParticularCategory(String category) {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, COLM_CATEGORY + " = ?", new String[]{category}, null, null, null);
        ArrayList<ExpenseModel> expensesList = new ArrayList<>();
        ExpenseModel contactModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                contactModel = new ExpenseModel();
                contactModel.setID(cursor.getString(0));
                contactModel.setTitle(cursor.getString(1));
                contactModel.setAmount(cursor.getString(2));
                contactModel.setDate(cursor.getString(3));
                contactModel.setCategory(cursor.getString(4));
                expensesList.add(contactModel);
            }
        }
        cursor.close();
        database.close();

        double total = 0.0;
        for (ExpenseModel expenseModel : expensesList) {
            total = total + Double.parseDouble(expenseModel.getAmount());
        }
        return total;
    }

}
