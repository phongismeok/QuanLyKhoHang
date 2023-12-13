package com.example.quanlykhohang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlykhohang.Interface.Typetoolbar;
import com.example.quanlykhohang.activities.LoginActivity;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.fragment.FrmChonLoaiBill;
import com.example.quanlykhohang.fragment.FrmDoiMK;
import com.example.quanlykhohang.fragment.FrmQuanLyProduct;
import com.example.quanlykhohang.fragment.FrmQuanLyUser;
import com.example.quanlykhohang.fragment.FrmThayDoiThongTin;
import com.example.quanlykhohang.fragment.FrmThongKe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements Typetoolbar {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView nav;
    String vaitro;
    String ten;
    UserDao userDao;
    String type = "";


    BottomNavigationView bottomnav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomnav = findViewById(R.id.bottomnav);
        drawerLayout = findViewById(R.id.drawerlayout);
        toolbar = findViewById(R.id.toolbar);

        nav = findViewById(R.id.nav);
        userDao = new UserDao(this);
        nhandl();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_app, R.string.close_app);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setItemIconTintList(null);


        Intent intent = getIntent();
        vaitro = intent.getStringExtra("trangthai");
        SharedPreferences sharedPreferences = getSharedPreferences("VAITRO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("vaitro", vaitro);
        editor.apply();


        View headerView = ((NavigationView) findViewById(R.id.nav)).getHeaderView(0);
        TextView textViewUsername = headerView.findViewById(R.id.txtten);
        TextView textViewVaitro = headerView.findViewById(R.id.txtvaitro);
        ImageView imgavt = headerView.findViewById(R.id.imgavtngang);
        if (vaitro.equals("thukho")) {
            textViewVaitro.setText("Thủ kho");
        } else {
            textViewVaitro.setText("Admin");
        }

        textViewUsername.setText(ten);

        byte[] anhus = userDao.getAvatarByUsername(ten);
        if (anhus != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(anhus, 0, anhus.length);

            if (bitmap != null) {
                imgavt.setImageBitmap(bitmap);
            } else {
                // null
            }
        } else {
            // null
        }

        if (vaitro.equals("admin")) {

        } else if (vaitro.equals("thukho")) {
            nav.getMenu().findItem(R.id.qluser).setVisible(false);
        }

        //hien thị luôn
        if (savedInstanceState == null) {
            FrmQuanLyProduct frmhome = new FrmQuanLyProduct();
            repalceFrg(frmhome);
        }
        nav.setItemIconTintList(null);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if (item.getItemId() == R.id.qlbill) {
//                    FrmChonLoaiBill bill = new FrmChonLoaiBill();
//                    repalceFrg(bill);
//                }
                if (item.getItemId() == R.id.dangxuat) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
//                if (item.getItemId() == R.id.qlproduct) {
//                    FrmQuanLyProduct pro = new FrmQuanLyProduct();
//                    repalceFrg(pro);
//                }
                if (item.getItemId() == R.id.qluser) {
                    FrmQuanLyUser us = new FrmQuanLyUser();
                    repalceFrg(us);
                }
                if (item.getItemId() == R.id.dmk) {
                    FrmDoiMK us = new FrmDoiMK();
                    repalceFrg(us);
                }
//                if (item.getItemId() == R.id.thaydoitt) {
//                    FrmThayDoiThongTin us = new FrmThayDoiThongTin();
//                    repalceFrg(us);
//                }
//                if (item.getItemId() == R.id.thongke) {
//                    FrmThongKe us = new FrmThongKe();
//                    repalceFrg(us);
//                }
                getSupportActionBar().setTitle(item.getTitle());
                return false;
            }
        });

        bottomnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.qlbillngang) {
                    FrmChonLoaiBill frmdapan = new FrmChonLoaiBill();
                    repalceFrg(frmdapan);
                } else if (item.getItemId() == R.id.qlproductngang) {
                    FrmQuanLyProduct frmThongkeAd = new FrmQuanLyProduct();
                    repalceFrg(frmThongkeAd);
                } else if (item.getItemId() == R.id.thaydoittngang) {
                    FrmThayDoiThongTin frmdethi = new FrmThayDoiThongTin();
                    repalceFrg(frmdethi);
                } else if (item.getItemId() == R.id.thongkengang) {
                    FrmThongKe frmloaicau = new FrmThongKe();
                    repalceFrg(frmloaicau);
                }


                getSupportActionBar().setTitle(item.getTitle());
                return true;
            }
        });

    }

    public void repalceFrg(Fragment frg) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frmnav, frg).commit();
    }

    public void nhandl() {
        SharedPreferences sharedPreferences2 = getSharedPreferences("TK", Context.MODE_PRIVATE);
        ten = sharedPreferences2.getString("tk", "");

    }


    @Override
    public void onDataPass(String data) {
        type = data;
        Log.d("lalala", "onDataPass: " + type);
        if (type.equals("thoat")) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icquaylai);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        } else {
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_app, R.string.close_app);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }

    }
}