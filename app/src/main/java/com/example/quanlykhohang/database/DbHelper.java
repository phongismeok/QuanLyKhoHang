package com.example.quanlykhohang.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//trong 1 du an chi nen co 1 file dbhelper
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context) {
        super(context, "phong.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tbUser = "create table User (username TEXT primary key," +
                "password TEXT," +
                "numberphone TEXT, " +
                "position TEXT, " +
                "avatar BLOB, " +
                "profile TEXT, " +
                "lastLogin TEXT, " +
                "createdDate TEXT," +
                "lastAction TEXT)";
        db.execSQL(tbUser);

        String datauser = "insert into User values('ad','1','123345636','0',null,'day la ad','14/10/2023','10/10/2023','mua')";
        db.execSQL(datauser);

        String tbProduct = "create table Product (idProduct INTEGER primary key autoincrement, " +
                "name TEXT, " +
                "quantity INTEGER, " +
                "pricenhap TEXT," +
                "pricexuat TEXT," +
                "photo BLOB," +
                "status TEXT," +
                "dateluukho TEXT," +
                "username TEXT references User(username))";
        db.execSQL(tbProduct);


        String tbBill = "create table Bill (idBill INTEGER primary key autoincrement," +
                "typeBill TEXT," +
                "createdDate TEXT, " +
                "note TEXT," +
                "tongtien TEXT," +
                "status TEXT," +
                "createdByUser TEXT references User(username))";
        db.execSQL(tbBill);


        String tbBillDetail = "create table BillDetail (idBillDetail integer primary key autoincrement , " +
                "idBill integer references Bill(idBill), " +
                "idProduct integer references Product(idProduct), " +
                "quantity text , " +
                "name text , " +
                "price text )";
        db.execSQL(tbBillDetail);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("drop table if exists Bill");
            db.execSQL("drop table if exists User");
            db.execSQL("drop table if exists Product");
            db.execSQL("drop table if exists BillDetail");
            onCreate(db);

        }
    }
}
