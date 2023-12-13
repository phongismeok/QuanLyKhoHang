package com.example.quanlykhohang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlykhohang.database.DbHelper;

import com.example.quanlykhohang.model.BillDetail;

import java.util.ArrayList;
import java.util.List;

public class BillDetailDao {
    private final DbHelper dbhelper;
    public static final String TABLE_NAME = "BillDetail";
    public static final String COLUMN_ID = "idBillDetail";
    public static final String COLUMN_IDBILL = "idBill";
    public static final String COLUMN_IDPRODUCT = "idProduct";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public BillDetailDao(Context context) {
        this.dbhelper = new DbHelper(context);
    }

    public boolean insertBillDetail(BillDetail billdt){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idBill",billdt.getIdBill());
        values.put("quantity",billdt.getQuantity());
        values.put("name",billdt.getName());
        values.put("price",billdt.getPrice());
        long row = db.insert("BillDetail",null,values);
        return (row > 0);
    }

    public boolean updateBillDetail(BillDetail billdt){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();


        long row = db.update("BillDetail",values,"idBillDetail=?",new String[]{String.valueOf(billdt.getIdBillDetail())});
        return (row>0);
    }

    public boolean deleteBillDetail (int id){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long row = db.delete("BillDetail","idBillDetail=?",new String[]{String.valueOf(id)});
        return (row>0);
    }
    public List<BillDetail> getBillDetailsByBillId(int idBill) {
        List<BillDetail> billDetails = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        String query = "SELECT * FROM BillDetail WHERE idBill = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idBill)});

        if (cursor.moveToFirst()) {
            do {
                int idBillDetail = cursor.getInt(cursor.getColumnIndexOrThrow("idBillDetail"));
                int idProduct = cursor.getInt(cursor.getColumnIndexOrThrow("idProduct"));
                String quantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));

                // Tạo đối tượng BillDetail và thêm vào danh sách
                BillDetail billDetail = new BillDetail(idBillDetail, idBill, idProduct, quantity, name, price);
                billDetails.add(billDetail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return billDetails;
    }
}
