package com.example.quanlykhohang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlykhohang.database.DbHelper;
import com.example.quanlykhohang.model.Product;


import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private final DbHelper dbhelper;
    public static final String TABLE_NAME = "Product";
    public static final String COLUMN_ID = "idProduct";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_PRICENHAP = "pricenhap";
    public static final String COLUMN_PRICEXUAT = "pricexuat";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_DATELUUKHO = "dateluukho";
    public static final String COLUMN_USERNAME = "username";

    public ProductDao(Context context) {
        this.dbhelper = new DbHelper(context);
    }

    public ArrayList<Product> selectAll() {
        ArrayList<Product> list = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("select * from Product", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Product product = new Product();
                    product.setIdProduct(cursor.getInt(0));
                    product.setName(cursor.getString(1));
                    product.setQuantity(cursor.getInt(2));
                    product.setPricenhap(cursor.getString(3));
                    product.setPricexuat(cursor.getString(4));
                    product.setPhoto(cursor.getBlob(5));
                    product.setStatus(cursor.getString(6));
                    product.setDateluukho(cursor.getString(7));
                    product.setUsername(cursor.getString(8));
                    list.add(product);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getProductByStatus(String targetStatus) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        // Sử dụng tham số hóa để tránh SQL injection
        String query = "SELECT * FROM Product WHERE status = ?";
        Cursor cursor = db.rawQuery(query, new String[]{targetStatus});

        if (cursor.moveToFirst()) {
            do {
                int idpro = cursor.getInt(cursor.getColumnIndexOrThrow("idProduct"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                String pricenhap = cursor.getString(cursor.getColumnIndexOrThrow("pricenhap"));
                String pricexuat = cursor.getString(cursor.getColumnIndexOrThrow("pricexuat"));
                byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String dateluukho = cursor.getString(cursor.getColumnIndexOrThrow("dateluukho"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));

                Product product = new Product(idpro, name, quantity, pricenhap, pricexuat, photo, status, dateluukho, username);
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }

    public boolean insertProduct(Product pro) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", pro.getName());
        values.put("pricenhap", pro.getPricenhap());
        values.put("pricexuat", pro.getPricexuat());
        values.put("photo", pro.getPhoto());
        values.put("status", pro.getStatus());
        values.put("username", pro.getUsername());
        values.put("quantity", pro.getQuantity());
        long row = db.insert("Product", null, values);
        return (row > 0);
    }

    public boolean updateProduct(Product pro) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", pro.getName());
        values.put("pricenhap", pro.getPricenhap());
        values.put("pricexuat", pro.getPricexuat());
        values.put("photo", pro.getPhoto());
        values.put("username", pro.getUsername());
        long row = db.update("Product", values, "idProduct=?", new String[]{String.valueOf(pro.getIdProduct())});
        return (row > 0);
    }

    public void luuTruProduct(String trangthai) {
        // Thiết lập giá trị mới cho trangthai
        String newTrangThaiValue = "luutru";

        // Xây dựng dòng lệnh UPDATE sử dụng ContentValues
        ContentValues values = new ContentValues();
        values.put("status", newTrangThaiValue);

        // Xây dựng điều kiện WHERE
        String whereClause = "idProduct = ?";
        String[] whereArgs = {String.valueOf(trangthai)};

        // Mở kết nối đến database
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        // Thực hiện câu lệnh UPDATE
        db.update("Product", values, whereClause, whereArgs);

        // Đóng kết nối sau khi thực hiện xong
        db.close();
    }

    public void huyluuTruProduct(String trangthai) {
        // Thiết lập giá trị mới cho trangthai
        String newTrangThaiValue = "ok";

        // Xây dựng dòng lệnh UPDATE sử dụng ContentValues
        ContentValues values = new ContentValues();
        values.put("status", newTrangThaiValue);

        // Xây dựng điều kiện WHERE
        String whereClause = "idProduct = ?";
        String[] whereArgs = {String.valueOf(trangthai)};

        // Mở kết nối đến database
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        // Thực hiện câu lệnh UPDATE
        db.update("Product", values, whereClause, whereArgs);

        // Đóng kết nối sau khi thực hiện xong
        db.close();
    }

    public boolean updateDate(int idProduct, String ngay) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("dateluukho", ngay);

        // Câu điều kiện để xác định dòng cần cập nhật
        String selection = "idProduct = ?";
        String[] selectionArgs = {String.valueOf(idProduct)};

        // Thực hiện cập nhật
        int count = db.update("Product", values, selection, selectionArgs);

        db.close();

        return count > 0;
    }

    public List<String> gettentheott() {
        List<String> productNames = new ArrayList<>();

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        if (db == null) {
            // Xử lý khi không thể mở được database
            return productNames;
        }

        String query = "SELECT name FROM Product WHERE status = 'ok'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // Lấy tên sản phẩm từ cột "name"
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                // Thêm tên sản phẩm vào danh sách
                productNames.add(productName);
            } while (cursor.moveToNext());
        }

        // Đóng con trỏ sau khi sử dụng
        cursor.close();

        // Đóng database sau khi sử dụng
        db.close();

        return productNames;
    }

    public void congsl(String productName, int quantityToAdd) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        // Tạo một ContentValues để chứa giá trị mới của quantity
        ContentValues values = new ContentValues();

        // Truy vấn cơ sở dữ liệu để lấy giá trị hiện tại của quantity
        Cursor cursor = db.query("Product", new String[]{"quantity"}, "name=?", new String[]{productName}, null, null, null);

        if (cursor.moveToFirst()) {
            // Lấy giá trị hiện tại của quantity
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

            // Tăng giá trị quantity theo số lượng cần thêm
            int newQuantity = currentQuantity + quantityToAdd;

            // Đặt giá trị mới vào ContentValues
            values.put("quantity", newQuantity);

            // Cập nhật giá trị mới vào cơ sở dữ liệu
            db.update("Product", values, "name=?", new String[]{productName});
        }

        // Đóng Cursor
        cursor.close();

        // Đóng database
        db.close();
    }

    public void trusl(String productName, int quantityToAdd) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        // Tạo một ContentValues để chứa giá trị mới của quantity
        ContentValues values = new ContentValues();

        // Truy vấn cơ sở dữ liệu để lấy giá trị hiện tại của quantity
        Cursor cursor = db.query("Product", new String[]{"quantity"}, "name=?", new String[]{productName}, null, null, null);

        if (cursor.moveToFirst()) {
            // Lấy giá trị hiện tại của quantity
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

            // Tăng giá trị quantity theo số lượng cần tru
            int newQuantity = currentQuantity - quantityToAdd;

            // Đặt giá trị mới vào ContentValues
            values.put("quantity", newQuantity);

            // Cập nhật giá trị mới vào cơ sở dữ liệu
            db.update("Product", values, "name=?", new String[]{productName});
        }

        // Đóng Cursor
        cursor.close();

        // Đóng database
        db.close();
    }

    public String getPriceNhapByName(String productName) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        // Các cột bạn muốn trả về
        String[] projection = {"pricenhap"};

        // Mệnh đề WHERE
        String selection = "name=?";
        String[] selectionArgs = {productName};

        // Thực hiện truy vấn
        Cursor cursor = db.query("Product", projection, selection, selectionArgs, null, null, null);

        String priceNhap = null;

        if (cursor.moveToFirst()) {
            // Lấy giá trị của cột pricenhap từ Cursor
            priceNhap = cursor.getString(cursor.getColumnIndexOrThrow("pricenhap"));
        }

        // Đóng Cursor
        cursor.close();

        // Đóng database
        db.close();

        return priceNhap;
    }

    public String getPriceXuatByName(String productName) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        // Các cột bạn muốn trả về
        String[] projection = {"pricexuat"};

        // Mệnh đề WHERE
        String selection = "name=?";
        String[] selectionArgs = {productName};

        // Thực hiện truy vấn
        Cursor cursor = db.query("Product", projection, selection, selectionArgs, null, null, null);

        String priceXuat = null;

        if (cursor.moveToFirst()) {
            // Lấy giá trị của cột pricenhap từ Cursor
            priceXuat = cursor.getString(cursor.getColumnIndexOrThrow("pricexuat"));
        }

        // Đóng Cursor
        cursor.close();

        // Đóng database
        db.close();

        return priceXuat;
    }

    public String getSLTonByName(String productName) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        // Các cột bạn muốn trả về
        String[] projection = {"quantity"};

        // Mệnh đề WHERE
        String selection = "name=?";
        String[] selectionArgs = {productName};

        // Thực hiện truy vấn
        Cursor cursor = db.query("Product", projection, selection, selectionArgs, null, null, null);

        String soluongcon = null;

        if (cursor.moveToFirst()) {
            // Lấy giá trị của cột pricenhap từ Cursor
            soluongcon = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));
        }

        // Đóng Cursor
        cursor.close();

        // Đóng database
        db.close();

        return soluongcon;
    }

    public boolean checktrungten(String name) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Product where name=?", new String[]{name});
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            return true;
        } else {
            return false;
        }
    }

    public List<String> demsoluongton() {
        List<String> idProductList = new ArrayList<>();

        String selectQuery = "SELECT name FROM Product WHERE quantity != 0 AND status = 'ok'";
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // Lấy giá trị của name từ cột đầu tiên (index 0)
                String ten = cursor.getString(0);

                // Thêm name vào danh sách
                idProductList.add(ten);
            } while (cursor.moveToNext());
        }

        // Đóng con trỏ khi đã sử dụng xong
        cursor.close();
        return idProductList;
    }

}
