package com.example.quanlykhohang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlykhohang.database.DbHelper;
import com.example.quanlykhohang.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final DbHelper dbhelper;
    public static final String COLUMN_USERNAME = "username";

    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NUMBERPHONE = "numberphone";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_PROFILE = "profile";
    public static final String COLUMN_LASTLOGIN = "lastLogin";
    public static final String COLUMN_CREATEDDATE = "createdDate";
    public static final String COLUMN_LASTACTION = "lastAction";
    public static final String TABLE_NAME = "User";

    public UserDao(Context context) {
        this.dbhelper = new DbHelper(context);
    }

    public ArrayList<User> selectAll() {
        ArrayList<User> list = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("select * from User", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    User user = new User();
                    user.setUsername(cursor.getString(0));
                    user.setPassword(cursor.getString(1));
                    user.setNumberphone(cursor.getString(2));
                    user.setPosition(cursor.getString(3));
                    user.setAvatar(cursor.getBlob(4));
                    user.setProfile(cursor.getString(5));
                    user.setLastLogin(cursor.getString(6));
                    user.setCreatedDate(cursor.getString(7));
                    user.setLastAction(cursor.getString(8));

                    list.add(user);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertUser(User us){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",us.getUsername());
        values.put("password",us.getPassword());
        values.put("position",us.getPosition());
        values.put("createdDate",us.getCreatedDate());
        long row = db.insert("User",null,values);
        return (row > 0);
    }

    public boolean updateUser(User us){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password",us.getPassword());
        values.put("profile",us.getProfile());
        long row = db.update("User",values,"username=?",new String[]{us.getUsername()});
        return (row>0);
    }

    public void doimk(String username, String newPassword) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("password", newPassword);

        // Xác định điều kiện cập nhật bằng cách sử dụng id_user
        String whereClause = "username = ?";
        String[] whereArgs = { String.valueOf(username) };

        // Thực hiện câu lệnh UPDATE
        db.update("User", values, whereClause, whereArgs);

        // Đóng kết nối đến database
        db.close();
    }

    public boolean deleteUser (String username){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long row = db.delete("User","username=?",new String[]{username});
        return (row>0);
    }
    public boolean checkLogin(String tk, String mk) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from User where username=? and password=?", new String[]{tk, mk});
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            return true;
        } else {
            return false;
        }
    }

    public boolean checkTonTai(String tk) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from User where username=?", new String[]{tk});
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            return true;
        } else {
            return false;
        }
    }

    public String getTrangThaiByTaiKhoan(String taiKhoan) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String position = null;

        String[] columnsToReturn = {"position"};
        String selection = "username" + " = ?";
        String[] selectionArgs = {taiKhoan};

        Cursor cursor = db.query("User", columnsToReturn, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            position = cursor.getString(cursor.getColumnIndexOrThrow("position"));
            cursor.close();
        }

        return position;
    }
    public String getPasswordByUsername(String username) {
        String password = null;

        SQLiteDatabase db = dbhelper.getReadableDatabase();

        // Tạo câu truy vấn SQL để lấy mật khẩu từ bảng User
        String query = "SELECT password FROM User WHERE username = ?";

        // Thực hiện truy vấn sử dụng rawQuery
        Cursor cursor = db.rawQuery(query, new String[]{username});

        // Kiểm tra xem có dữ liệu không và di chuyển con trỏ đến dòng đầu tiên
        if (cursor.moveToFirst()) {
            // Lấy giá trị mật khẩu từ cột "password"
            password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return password;
    }

    public String laysdt(String username) {
        String numberphone = null;

        SQLiteDatabase db = dbhelper.getReadableDatabase();

        // Tạo câu truy vấn SQL để lấy numberphone từ bảng User
        String query = "SELECT numberphone FROM User WHERE username = ?";

        // Thực hiện truy vấn sử dụng rawQuery
        Cursor cursor = db.rawQuery(query, new String[]{username});

        // Kiểm tra xem có dữ liệu không và di chuyển con trỏ đến dòng đầu tiên
        if (cursor.moveToFirst()) {
            // Lấy giá trị numberphone từ cột "numberphone"
            numberphone = cursor.getString(cursor.getColumnIndexOrThrow("numberphone"));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return numberphone;
    }
    public String layprofile(String username) {
        String numberphone = null;

        SQLiteDatabase db = dbhelper.getReadableDatabase();

        // Tạo câu truy vấn SQL để lấy numberphone từ bảng User
        String query = "SELECT profile FROM User WHERE username = ?";

        // Thực hiện truy vấn sử dụng rawQuery
        Cursor cursor = db.rawQuery(query, new String[]{username});

        // Kiểm tra xem có dữ liệu không và di chuyển con trỏ đến dòng đầu tiên
        if (cursor.moveToFirst()) {
            // Lấy giá trị numberphone từ cột "numberphone"
            numberphone = cursor.getString(cursor.getColumnIndexOrThrow("profile"));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return numberphone;
    }
    public boolean capnhatsdt(String username, String newNumberphone) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        // Giá trị cần cập nhật
        ContentValues values = new ContentValues();
        values.put("numberphone", newNumberphone);

        // Câu lệnh WHERE để xác định dòng cần cập nhật
        String whereClause = "username = ?";

        // Tham số cho WHERE clause
        String[] whereArgs = {username};

        // Thực hiện cập nhật và kiểm tra kết quả
        int rowsAffected = db.update("User", values, whereClause, whereArgs);

        // Đóng database
        db.close();

        // Trả về true nếu có ít nhất một dòng đã được cập nhật
        return rowsAffected > 0;
    }

    public boolean capnhatpro(String username, String pro) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        // Giá trị cần cập nhật
        ContentValues values = new ContentValues();
        values.put("profile", pro);

        // Câu lệnh WHERE để xác định dòng cần cập nhật
        String whereClause = "username = ?";

        // Tham số cho WHERE clause
        String[] whereArgs = {username};

        // Thực hiện cập nhật và kiểm tra kết quả
        int rowsAffected = db.update("User", values, whereClause, whereArgs);

        // Đóng database
        db.close();

        // Trả về true nếu có ít nhất một dòng đã được cập nhật
        return rowsAffected > 0;
    }
    public boolean updateAvatar(String username, byte[] newAvatar) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("avatar", newAvatar);

        // Assuming "username" is the column you use to uniquely identify users
        String whereClause = "username=?";
        String[] whereArgs = {username};

        // Execute the update and check if it was successful
        int rowsUpdated = db.update("User", values, whereClause, whereArgs);
        db.close();

        // Return true if at least one row was updated, indicating a successful update
        return rowsUpdated > 0;
    }

    public byte[] getAvatarByUsername(String username) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        byte[] avatar = null;

        // Define a projection that specifies which columns you want to retrieve.
        String[] projection = {"avatar"};

        // Specify the WHERE clause to find the user by username.
        String selection = "username=?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                "User",     // The table to query
                projection, // The array of columns to return
                selection,  // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // don't sort order
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Get the avatar column index
            int avatarColumnIndex = cursor.getColumnIndex("avatar");

            // Retrieve the avatar data
            avatar = cursor.getBlob(avatarColumnIndex);

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return avatar;
    }

    public List<User> truyvandanhsachthukho(String position) {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        // Define a projection that specifies which columns you want to retrieve.
        String[] projection = {"username", "password", "numberphone", "position", "avatar", "profile", "lastLogin", "createdDate", "lastAction"};

        // Specify the WHERE clause to filter users by position.
        String selection = "position=?";
        String[] selectionArgs = {position};

        Cursor cursor = db.query(
                "User",       // The table to query
                projection,   // The array of columns to return
                selection,    // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null,         // don't group the rows
                null,         // don't filter by row groups
                null          // don't sort order
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Create a User object and populate its fields from the cursor
                User user = new User();
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                user.setNumberphone(cursor.getString(cursor.getColumnIndexOrThrow("numberphone")));
                user.setPosition(cursor.getString(cursor.getColumnIndexOrThrow("position")));
                user.setAvatar(cursor.getBlob(cursor.getColumnIndexOrThrow("avatar")));
                user.setProfile(cursor.getString(cursor.getColumnIndexOrThrow("profile")));
                user.setLastLogin(cursor.getString(cursor.getColumnIndexOrThrow("lastLogin")));
                user.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow("createdDate")));
                user.setLastAction(cursor.getString(cursor.getColumnIndexOrThrow("lastAction")));

                // Add the User object to the list
                userList.add(user);
            }

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return userList;
    }

    public void updateLastLogin(String username, String newLastLogin) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("lastLogin", newLastLogin);

        // Câu lệnh WHERE để xác định dòng cần cập nhật
        String whereClause = "username=?";
        String[] whereArgs = {username};

        // Thực hiện cập nhật
        db.update("User", values, whereClause, whereArgs);

        // Đóng kết nối database
        db.close();
    }
    public void updateLastAction(String username, String newLastAction) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("lastAction", newLastAction);

        // Câu lệnh WHERE để xác định dòng cần cập nhật
        String whereClause = "username=?";
        String[] whereArgs = {username};

        // Thực hiện cập nhật
        db.update("User", values, whereClause, whereArgs);

        // Đóng kết nối database
        db.close();
    }
}
