package com.blackwing.sqlite_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    public static boolean isDataPresent = true;
    public static boolean isDataDeleted = false;
    public static final String COLUMN_ID = "id";
    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
    public DBHandler(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    //called when you try to access db for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CUSTOMER_NAME + " TEXT, " +
                COLUMN_CUSTOMER_AGE + " INTEGER, " +
                COLUMN_ACTIVE_CUSTOMER + " BOOL) ";
        db.execSQL(createTableStatement);
    }

    //called when version of application is changed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(CustomerModel customerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
        cv.put(COLUMN_CUSTOMER_AGE, customerModel.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());

        long insert = db.insert(CUSTOMER_TABLE, null,cv);

        return insert != -1;
    }

    public List<CustomerModel> getEveryone() {
        List<CustomerModel> returnList = new ArrayList<>();
        //get data from db
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            //loop through each result set and put them into returnList
            isDataPresent = true;
            do {
                int customerId = cursor.getInt(0); //using 0 as id is always first at index 0
                String customerName = cursor.getString(1); //the next column
                int customerAge = cursor.getInt(2);
                boolean customerActive = cursor.getInt(3) == 1;
                CustomerModel newCustomer = new CustomerModel(customerId, customerName, customerAge, customerActive);
                returnList.add(newCustomer);
            } while (cursor.moveToNext());
        } else {
            isDataPresent = false;
            //do nothing
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean deleteOne (CustomerModel customerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CUSTOMER_TABLE +
                " WHERE " + COLUMN_ID + " = " + customerModel.getId();
        Cursor cursor = db.rawQuery(queryString,null);
        isDataDeleted = cursor.moveToFirst();
        cursor.close();
        return isDataDeleted;
    }

    public boolean isDataPresent() {
        return isDataPresent;
    }
}
