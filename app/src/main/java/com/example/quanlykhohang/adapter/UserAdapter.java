package com.example.quanlykhohang.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.model.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {
    private final Context context;
    private final ArrayList<User> list;
    UserDao userDao;

    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
        userDao = new UserDao(context);
    }

    @NonNull
    @Override
    public UserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_user, null);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewholder holder, int position) {
        User user = list.get(position);
        holder.txttk.setText(user.getUsername());
        holder.txtmk.setText(user.getPassword());
        holder.txtsdt.setText(user.getNumberphone());
        holder.txtprofile.setText(user.getProfile());
        holder.txtngaytaotk.setText(user.getCreatedDate());
        holder.txtlancuoilg.setText(user.getLastLogin());
        holder.txthanhdongcuoi.setText(user.getLastAction());

        byte[] anhus = user.getAvatar();
        if (anhus != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(anhus, 0, anhus.length);

            if (bitmap != null) {
                holder.imgavt.setImageBitmap(bitmap);
            } else {
                // null
            }
        } else {
            // null
        }

        holder.imgxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("chú ý");
                builder.setMessage("bạn có chắc chắn muốn xóa thủ kho này không");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String layus = user.getUsername();
                        userDao.deleteUser(layus);
                        Toastdep(2,"Xóa thành công");
                        hienthidulieu();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                // tạo ra hộp thoại
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.imgsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateus(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        // khai bao bien
        TextView txttk, txtmk, txtsdt, txtprofile, txtngaytaotk, txtlancuoilg, txthanhdongcuoi;
        ImageView imgsua, imgxoa,imgavt;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            // anh xa
            txttk = itemView.findViewById(R.id.itemuser_username);
            txtmk = itemView.findViewById(R.id.itemuser_password);
            txtsdt = itemView.findViewById(R.id.itemuser_numberphone);
            txtprofile = itemView.findViewById(R.id.itemuser_profile);
            txtngaytaotk = itemView.findViewById(R.id.itemuser_createdDate);
            txtlancuoilg = itemView.findViewById(R.id.itemuser_lastLogin);
            txthanhdongcuoi = itemView.findViewById(R.id.itemuser_lastAction);
            imgavt = itemView.findViewById(R.id.itemuser_avatar);
            imgsua = itemView.findViewById(R.id.itemuser_btnsua);
            imgxoa = itemView.findViewById(R.id.itemuser_btnxoa);
        }


    }

    public void hienthidulieu() {
        list.clear();
        list.addAll(userDao.truyvandanhsachthukho("1"));
        notifyDataSetChanged();
    }

    public void updateus(User us) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // tao view , gan layout vao view
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_user, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // anh xa
        EditText edtpro = view.findViewById(R.id.edt_uduser_profile);
        EditText edtmk = view.findViewById(R.id.edt_uduser_mk);

        Button btnsuapro = view.findViewById(R.id.btn_uduser_addus);

        // gan du lieu tu textview len edittext
        edtpro.setText(us.getProfile());
        edtmk.setText(us.getPassword());

        btnsuapro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // lay du lieu tu cac editText them vao bien de check
                String check1 = edtpro.getText().toString();
                String check2 = edtmk.getText().toString();

                if (TextUtils.isEmpty(check1) || TextUtils.isEmpty(check2)) {
                    Toastdep(1,"Nhập đầy đủ thông tin");
                } else {
                        us.setProfile(edtpro.getText().toString());
                        us.setPassword(edtmk.getText().toString());
                        if (userDao.updateUser(us)) {
                            dialog.dismiss();
                            Toastdep(2,"Update thành công");
                            hienthidulieu();
                        } else {
                            Toastdep(1,"Update thất bại");
                        }

                }
            }
        });
    }
    public void Toastdep(int kieu ,String nd){
        LayoutInflater inflater2 = ((Activity) context).getLayoutInflater();
        View layout = inflater2.inflate(R.layout.custom_toast, ((Activity) context).findViewById(R.id.custom_toast_layout));

// Tùy chỉnh nội dung Toast
        ImageView imageView = layout.findViewById(R.id.custom_toast_icon);
        if(kieu==1){
            Glide.with(context).asGif().load(R.raw.anhthsai).into(imageView);
        }
        else{
            Glide.with(context).asGif().load(R.raw.anhthdung).into(imageView);
        }


        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(nd);

// Tạo đối tượng Toast
        Toast toast = new Toast(context.getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

// Hiển thị Toast
        toast.show();
    }
}
