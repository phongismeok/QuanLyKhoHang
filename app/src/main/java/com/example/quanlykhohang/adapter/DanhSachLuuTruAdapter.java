package com.example.quanlykhohang.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlykhohang.Interface.OnImageSelectedListener;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.ProductDao;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.model.Product;

import java.util.ArrayList;


public class DanhSachLuuTruAdapter extends RecyclerView.Adapter<DanhSachLuuTruAdapter.viewholder>{

    private final Context context;
    private final ArrayList<Product> list;
    ProductDao productDao;
    UserDao userDao;
    String user;
    int dk=0;
    private OnImageSelectedListener imageSelectedListener;


    public DanhSachLuuTruAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
        productDao = new ProductDao(context);
    }

    @NonNull
    @Override
    public DanhSachLuuTruAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_luutru, null);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachLuuTruAdapter.viewholder holder, int position) {
        Product product = list.get(position);
        holder.txttensp.setText(product.getName());
        holder.txtsl.setText(product.getQuantity()+"");
        holder.txtgianhap.setText(product.getPricenhap());
        holder.txtgiaxuat.setText(product.getPricexuat());
        holder.txtnguoithem.setText(product.getUsername());
        holder.txtngayluutru.setText(product.getDateluukho());
        userDao = new UserDao(context);
        laydl();
        if(dk!=1){
            userDao.updateLastAction(user,"xem sản phẩm lưu trữ");
        }

        byte[] anhsp = product.getPhoto();
        if (anhsp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(anhsp, 0, anhsp.length);

            if (bitmap != null) {
                holder.imganhsp.setImageBitmap(bitmap);
            } else {
                // null
            }
        } else {
            // null
        }

        holder.btnhuyluutru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("chú ý");
                builder.setMessage("bạn có chắc chắn muốn bỏ lưu trữ sản phẩm này không");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userDao.updateLastAction(user,"bỏ lưu trữ sản phẩm");
                        dk = 1;
                        int idht = product.getIdProduct();
                        String epid = String.valueOf(idht);
                        Toastdep(2,"hủy lưu trữ thành công");
                        productDao.huyluuTruProduct(epid);
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class viewholder extends RecyclerView.ViewHolder {
        // khai bao bien
        TextView txttensp, txtsl, txtgianhap, txtgiaxuat, txtnguoithem,txtngayluutru;
        ImageView imganhsp;
        ImageView btnhuyluutru;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            // anh xa
            txttensp = itemView.findViewById(R.id.itemluutru_name);
            txtsl = itemView.findViewById(R.id.itemluutru_quantity);
            txtgianhap = itemView.findViewById(R.id.itemluutru_gianhap);
            txtgiaxuat = itemView.findViewById(R.id.itemluutru_giaxuat);
            txtnguoithem = itemView.findViewById(R.id.itemluutru_ngtao);
            txtngayluutru = itemView.findViewById(R.id.itemluutru_ngayluutru);
            imganhsp = itemView.findViewById(R.id.itemluutru_anh);
            btnhuyluutru = itemView.findViewById(R.id.itemluutru_btnboluutru);
        }


    }
    public void hienthidulieu(){
        list.clear();
        list.addAll(productDao.getProductByStatus("luutru"));
        notifyDataSetChanged();
    }
    public void laydl(){
        SharedPreferences sharedPreferences2 = context.getSharedPreferences("TK", Context.MODE_PRIVATE);
        user = sharedPreferences2.getString("tk", "");
        Log.d("neee", "laydl: "+user);
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
