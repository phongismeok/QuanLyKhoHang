package com.example.quanlykhohang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlykhohang.database.DbHelper;
import com.example.quanlykhohang.model.Bill;
import com.example.quanlykhohang.model.Product;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BillDao {
    private final DbHelper dbhelper;
    public static final String TABLE_NAME = "Bill";
    public static final String COLUMN_IDBILL = "idBill";
    public static final String COLUMN_TYPEBILL = "typeBill";
    public static final String COLUMN_CREATEDDATE = "createdDate";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TT = "tongtien";
    public static final String COLUMN_CREATEDBYUSER = "createdByUser";


    public BillDao(Context context) {
        this.dbhelper = new DbHelper(context);
    }

    public ArrayList<Bill> selectAll() {
        ArrayList<Bill> list = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("select * from Bill", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Bill bill = new Bill();
                    bill.setIdBill(cursor.getInt(0));
                    bill.setTypeBill(cursor.getString(1));
                    bill.setCreatedDate(cursor.getString(2));
                    bill.setNote(cursor.getString(3));
                    bill.setTongtien(cursor.getString(4));
                    bill.setCreatedByUser(cursor.getString(5));

                    list.add(bill);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public boolean updateBill(Bill bill){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        long row = db.update("Bill",values,"BillID=?",new String[]{String.valueOf(bill.getIdBill())});
        return (row>0);
    }

    public boolean deleteBill (int BillID){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long row = db.delete("Bill","BillID=?",new String[]{String.valueOf(BillID)});
        return (row>0);
    }

    public long insertBill(Bill bill) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("typeBill", bill.getTypeBill());
        values.put("createdDate", bill.getCreatedDate());
        values.put("note", bill.getNote());
        values.put("tongtien", bill.getTongtien());
        values.put("status", bill.getStatus());
        values.put("createdByUser", bill.getCreatedByUser());

        // Insert into the database and get the inserted row ID
        long insertedId = db.insert("Bill", null, values);

        db.close();

        return insertedId;
    }
    public List<Bill> getBillsByTypeAndStatus(String typeBill, String status) {
        List<Bill> bills = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        String query = "SELECT * FROM Bill WHERE typeBill = ? AND status = ?";
        Cursor cursor = db.rawQuery(query, new String[]{typeBill, status});

        if (cursor.moveToFirst()) {
            do {
                int idBill = cursor.getInt(cursor.getColumnIndexOrThrow("idBill"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("typeBill"));
                String createdDate = cursor.getString(cursor.getColumnIndexOrThrow("createdDate"));
                String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
                String tongTien = cursor.getString(cursor.getColumnIndexOrThrow("tongtien"));
                String billStatus = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String createdByUser = cursor.getString(cursor.getColumnIndexOrThrow("createdByUser"));

                // Tạo đối tượng Bill và thêm vào danh sách
                Bill bill = new Bill(idBill, type, createdDate, note, tongTien, billStatus, createdByUser);
                bills.add(bill);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return bills;
    }

public int getQuantityByTypeBillAndDateRange(String typeBill, String startDate, String endDate) {
    SQLiteDatabase db = dbhelper.getReadableDatabase();
    int totalQuantity = 0;

    // Chuyển định dạng ngày tháng của startDate và endDate nếu cần thiết
    startDate = convertDateFormat(startDate, "d/MM/yyyy", "yyyy-MM-dd HH:mm:ss");
    endDate = convertDateFormat(endDate, "d/MM/yyyy", "yyyy-MM-dd HH:mm:ss");

    // Xây dựng câu truy vấn SQL
    String query = "SELECT SUM(quantity) AS total_quantity FROM BillDetail " +
            "JOIN Bill ON BillDetail.idBill = Bill.idBill " +
            "WHERE Bill.typeBill = ? AND Bill.createdDate BETWEEN ? AND ?";

    // Thực hiện truy vấn
    Cursor cursor = db.rawQuery(query, new String[]{typeBill, startDate, endDate});

    // Kiểm tra và xử lý kết quả truy vấn
    if (cursor.moveToFirst()) {
        totalQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("total_quantity"));
    }

    // Đóng cursor và database
    cursor.close();
    db.close();

    // Trả về tổng quantity
    return totalQuantity;
}

    // Phương thức chuyển đổi định dạng ngày tháng
    private String convertDateFormat(String inputDate, String inputFormat, String outputFormat) {
        try {
            SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat, Locale.getDefault());
            SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat, Locale.getDefault());
            Date date = inputFormatter.parse(inputDate);
            return outputFormatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int laytongdiem(String typeBill, String startDate, String endDate) {
        int totalAmount = 0;

        // Chuyển định dạng ngày tháng của startDate và endDate nếu cần thiết
        startDate = convertDateFormat(startDate, "d/MM/yyyy", "yyyy-MM-dd HH:mm:ss");
        endDate = convertDateFormat(endDate, "d/MM/yyyy", "yyyy-MM-dd HH:mm:ss");

        // Tạo một đối tượng SQLiteDatabase
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        // Tạo câu truy vấn SQL để lấy tổng tiền từ bảng Bill
        String query = "SELECT SUM(CAST(tongtien AS REAL)) AS total FROM Bill " +
                "WHERE typeBill = ? AND createdDate BETWEEN ? AND ?";

        // Thực hiện truy vấn sử dụng rawQuery
        Cursor cursor = db.rawQuery(query, new String[]{typeBill, startDate, endDate});

        // Kiểm tra xem có dữ liệu không và di chuyển con trỏ đến dòng đầu tiên
        if (cursor.moveToFirst()) {
            // Lấy giá trị tổng tiền từ cột "total"
            totalAmount = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return totalAmount;
    }

    public void luuTrubill(String trangthai) {
        // Thiết lập giá trị mới cho trangthai
        String newTrangThaiValue = "luutru";

        // Xây dựng dòng lệnh UPDATE sử dụng ContentValues
        ContentValues values = new ContentValues();
        values.put("status", newTrangThaiValue);

        // Xây dựng điều kiện WHERE
        String whereClause = "idBill = ?";
        String[] whereArgs = {String.valueOf(trangthai)};

        // Mở kết nối đến database
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        // Thực hiện câu lệnh UPDATE
        db.update("Bill", values, whereClause, whereArgs);

        // Đóng kết nối sau khi thực hiện xong
        db.close();
    }
    public void huyluuTrubill(String trangthai) {
        // Thiết lập giá trị mới cho trangthai
        String newTrangThaiValue = "ok";

        // Xây dựng dòng lệnh UPDATE sử dụng ContentValues
        ContentValues values = new ContentValues();
        values.put("status", newTrangThaiValue);

        // Xây dựng điều kiện WHERE
        String whereClause = "idBill = ?";
        String[] whereArgs = {String.valueOf(trangthai)};

        // Mở kết nối đến database
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        // Thực hiện câu lệnh UPDATE
        db.update("Bill", values, whereClause, whereArgs);

        // Đóng kết nối sau khi thực hiện xong
        db.close();
    }

}
