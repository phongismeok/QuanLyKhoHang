package com.example.quanlykhohang.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quanlykhohang.Interface.DataRefreshListener;
import com.example.quanlykhohang.Interface.Typetoolbar;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.model.User;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;


public class FrmThayDoiThongTin extends Fragment implements DataRefreshListener {

    public FrmThayDoiThongTin() {
        // Required empty public constructor
    }

    TextView txttk, txtsdt, txtprofile;
    ImageView imgsua, imgavt;
    UserDao userDao;
    String tk, sdt, profile;
    byte[] img;
    String sdtmoi,promoi;
    int dk=0;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Typetoolbar dataPassListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frm_thay_doi_thong_tin, container, false);
        txttk = view.findViewById(R.id.suatt_tk);
        imgavt = view.findViewById(R.id.imgavt);
        txtsdt = view.findViewById(R.id.suatt_sdt);
        txtprofile = view.findViewById(R.id.suatt_profile);
        imgsua = view.findViewById(R.id.imgsuatt);
        nhandl();
        dataPassListener.onDataPass("menu");
        userDao = new UserDao(getActivity());
        sdt = userDao.laysdt(tk);
        profile = userDao.layprofile(tk);
        txttk.setText(tk);
        txtsdt.setText(sdt);
        txtprofile.setText(profile);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Thiết lập hiển thị nút "Up" trên ActionBar
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icmenuthuong);
        }

        byte[] anhus = userDao.getAvatarByUsername(tk);
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

        if(dk!=1){
            userDao.updateLastAction(tk,"xem thông tin cá nhân");
        }

        imgsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suatt();
            }
        });
        imgavt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                layanh();

                return true;
            }
        });


        return view;
    }

    public void nhandl() {
        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("TK", Context.MODE_PRIVATE);
        tk = sharedPreferences2.getString("tk", "");
    }

    public void suatt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //tao view va gan gan layout
        LayoutInflater inflater = getLayoutInflater();
        View view2 = inflater.inflate(R.layout.dialog_suatt, null);
        builder.setView(view2); // gan view vao hop thoai
        Dialog dialog = builder.create();
        dialog.show();// show hop thoai
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtsdt = view2.findViewById(R.id.edt_suatt_sdt);
        EditText edtpro = view2.findViewById(R.id.edt_suatt_profile);
        Button btnok = view2.findViewById(R.id.btn_suatt_sua);


        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdt = edtsdt.getText().toString();
                String pro = edtpro.getText().toString();

                if (TextUtils.isEmpty(sdt) || TextUtils.isEmpty(pro)) {
                    Toastdep(1,"Nhập đầy đủ thông tin");
                } else {
                    try {
                        long sdt2 = Long.parseLong(sdt);
                        if (sdt.length() == 10) {
                            if(userDao.capnhatsdt(tk, sdt)){
                                if(userDao.capnhatpro(tk, pro)){
                                    userDao.updateLastAction(tk,"thay đổi thông tin cá nhân");
                                    dk=1;
                                    Toastdep(2,"Cập nhật thông tin thành công");
                                }
                                else {
                                    Toastdep(1,"Cập nhật thông tin thất bại");
                                }
                            }
                            else{
                                Toastdep(1,"Cập nhật thông tin thất bại");
                            }

                            onDataRefresh();
                            dialog.dismiss();

                        } else {
                            Toastdep(1,"sdt phải là 10 số");
                        }
                    } catch (Exception e) {
                        Toastdep(1,"sdt phải là số");
                    }
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // lay du lieu uri laf duong dan trong android
            Uri uri = data.getData();
            Log.d("ImagePath", "Selected Image Path: " + uri.toString());
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if(!(imgavt == null)){
                    imgavt.setImageBitmap(bitmap);
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) imgavt.getDrawable();
                        Bitmap bitmap2 = drawable.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // chuyen hinh ve
                        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        // khai bao mang chua du lieu
                        img = stream.toByteArray();

                        if (img != null && img.length > 0) {
                            if(userDao.updateAvatar(tk,img)){
                                Toastdep(2,"Cập nhật ảnh thành công");
                                View headerView = ((NavigationView) getActivity().findViewById(R.id.nav)).getHeaderView(0);
                                ImageView imgavt = headerView.findViewById(R.id.imgavtngang);
                                byte[] anhus = userDao.getAvatarByUsername(tk);
                                if (anhus != null) {
                                    Bitmap bitmap3 = BitmapFactory.decodeByteArray(anhus, 0, anhus.length);

                                    if (bitmap3 != null) {
                                        imgavt.setImageBitmap(bitmap3);
                                    } else {
                                        // null
                                    }
                                } else {
                                    // null
                                }

                            }
                            else{
                                Toastdep(1,"Cập nhật ảnh thất bại");
                            }
                        } else {
                            Toastdep(1,"Lỗi khi chuyển đổi ảnh");
                        }
                    }catch (Exception e){
                        Toastdep(1,"bạn chưa chọn ảnh");
                    }
                }else {
                    imgavt.setImageBitmap(bitmap);

                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void layanh(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onDataRefresh() {
        sdtmoi = userDao.laysdt(tk);
        promoi = userDao.layprofile(tk);

        txtsdt.setText(sdtmoi);
        txtprofile.setText(promoi);
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
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dataPassListener = (Typetoolbar) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDataPassListener");
        }
    }
}