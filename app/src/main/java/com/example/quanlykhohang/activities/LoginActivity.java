package com.example.quanlykhohang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quanlykhohang.MainActivity;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.model.User;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    EditText edtUserName, edtPassWord;
    Button btnlogin,btnhuy;
    CheckBox chkRememberPass;
    UserDao usdao;
    String vaitro;
    String ngayhomnay;
    String strUserName, strPassWord;

    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUserName = findViewById(R.id.edttk);
        edtPassWord = findViewById(R.id.edtmk);
        chkRememberPass = findViewById(R.id.chkluumk);
        btnlogin = findViewById(R.id.btnLogin);
        btnhuy = findViewById(R.id.btnCancel);

        // doc User, pass trong SharedPreferences
        SharedPreferences pref = getSharedPreferences("USER_FILE",MODE_PRIVATE);
        String user = pref.getString("USERNAME", "");
        String pass = pref.getString("PASSWORD","");
        Boolean rem = pref.getBoolean("REMEMBER",false);

        edtUserName.setText(user);
        edtPassWord.setText(pass);
        chkRememberPass.setChecked(rem);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checktt();
            }
        });

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtUserName.setText("");
                edtPassWord.setText("");
                chkRememberPass.setChecked(false);
            }
        });

    }

    public void checktt() {
        usdao = new UserDao(this);
        strUserName = edtUserName.getText().toString();
        strPassWord = edtPassWord.getText().toString();
        String postion = String.valueOf(usdao.getTrangThaiByTaiKhoan(strUserName));

        if (usdao.checkLogin(strUserName, strPassWord)) {
            Toastdep(2,"đăng nhập thành công");
            rememberUser(strUserName,strPassWord,chkRememberPass.isChecked());
            if (postion.equals("0")) {
                vaitro = "admin";
                Intent intent3 = new Intent(this, MainActivity.class);
                intent3.putExtra("trangthai", vaitro);
                startActivity(intent3);
            }
            else{
                Calendar ngay = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String ngayhomnay = sdf.format(ngay.getTime());
                usdao.updateLastLogin(strUserName,ngayhomnay);
                usdao.updateLastAction(strUserName,"Đăng nhập");
                vaitro = "thukho";
                Intent intent5 = new Intent(this, MainActivity.class);
                intent5.putExtra("trangthai", vaitro);
                startActivity(intent5);
            }
            SharedPreferences sharedPreferences = getSharedPreferences("TK", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("tk", strUserName);
            editor.apply();
        } else {
            Toastdep(1,"sai tài khoản mật khẩu");
        }


    }
    public void rememberUser(String u, String p, boolean status){
        SharedPreferences pref = getSharedPreferences("USER_FILE",MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        if (!status){
            //Xóa tình trạng lưu trữ trước đó
            edit.clear();
        }else {
            edit.putString("USERNAME",u);
            edit.putString("PASSWORD",p);
            edit.putBoolean("REMEMBER",status);
        }
        edit.commit();
    }
    public void Toastdep(int kieu ,String nd){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_layout));

// Tùy chỉnh nội dung Toast
        ImageView imageView = layout.findViewById(R.id.custom_toast_icon);
        if(kieu==1){
            Glide.with(this).asGif().load(R.raw.anhthsai).into(imageView);
        }
        else{
            Glide.with(this).asGif().load(R.raw.anhchao).into(imageView);
        }


        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(nd);

// Tạo đối tượng Toast
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

// Hiển thị Toast
        toast.show();
    }

}