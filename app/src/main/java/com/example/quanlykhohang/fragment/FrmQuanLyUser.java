package com.example.quanlykhohang.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.adapter.UserAdapter;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.model.Product;
import com.example.quanlykhohang.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class FrmQuanLyUser extends Fragment {

    RecyclerView rcvus;
    FloatingActionButton fltaddus;

    UserDao userDao;
    UserAdapter userAdapter;
    private ArrayList<User> list = new ArrayList<>();
    String ngayhomnay;

    public FrmQuanLyUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frm_quan_ly_user, container, false);
        rcvus = view.findViewById(R.id.rcvuser);
        fltaddus = view.findViewById(R.id.fltadduser);
        userDao = new UserDao(getActivity());


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvus.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(getActivity(), list);
        rcvus.setAdapter(userAdapter);

        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 1);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rcvus.setLayoutManager(layoutManager1);

        fltaddus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themus();
            }
        });
        hienthidulieu();

        return view;
    }

    public void themus() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //tao view va gan gan layout
        LayoutInflater inflater = getLayoutInflater();
        View view2 = inflater.inflate(R.layout.dialog_user, null);
        builder.setView(view2); // gan view vao hop thoai
        Dialog dialog = builder.create();
        dialog.show();// show hop thoai
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edttk = view2.findViewById(R.id.edt_user_tk);
        EditText edtmk = view2.findViewById(R.id.edt_user_mk);
        Button btnthemus = view2.findViewById(R.id.btn_user_addus);


        btnthemus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tk = edttk.getText().toString();
                String mk = edtmk.getText().toString();

                if (TextUtils.isEmpty(tk) || TextUtils.isEmpty(mk)) {
                    Toastdep(1,"Nhập đầy đủ thông tin");
                } else {
                    if (userDao.checkTonTai(tk)) {
                        Toastdep(1,"thủ kho đã tồn tại");
                    } else {
                        Calendar ngay = Calendar.getInstance();//tạo đối tượng để lấy ngày giờ hiện tại
                        int year = ngay.get(Calendar.YEAR);
                        int month = ngay.get(Calendar.MONTH)+1;
                        int day = ngay.get(Calendar.DAY_OF_MONTH);
                        ngayhomnay = day+"/"+month+"/"+year;
                        User user = new User(tk, mk, "1",ngayhomnay );
                        if (userDao.insertUser(user)) {
                            Toastdep(2,"Thêm thành công");
                            hienthidulieu();
                            dialog.dismiss();
                        } else {
                            Toastdep(1,"Thêm thất bại");
                        }


                    }

                }
            }
        });
    }
    public void hienthidulieu(){
        list.clear();
        list.addAll(userDao.truyvandanhsachthukho("1"));
        userAdapter.notifyDataSetChanged();
    }
    public void Toastdep(int kieu ,String nd){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, getActivity().findViewById(R.id.custom_toast_layout));

// Tùy chỉnh nội dung Toast
        ImageView imageView = layout.findViewById(R.id.custom_toast_icon);
        if(kieu==1){
            Glide.with(getActivity()).asGif().load(R.raw.anhthsai).into(imageView);
        }
        else{
            Glide.with(getActivity()).asGif().load(R.raw.anhthdung).into(imageView);
        }


        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(nd);

// Tạo đối tượng Toast
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

// Hiển thị Toast
        toast.show();
    }
}