package com.example.quanlykhohang.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.BillDao;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.model.Bill;

import java.util.ArrayList;

public class LuuTruXuatAdapter extends RecyclerView.Adapter<LuuTruXuatAdapter.viewholder>{
    private final Context context;
    private final ArrayList<Bill> list;
    BillDao billDao;
    String user;
    UserDao userDao;
    int dk=0;

    public LuuTruXuatAdapter(Context context, ArrayList<Bill> list) {
        this.context = context;
        this.list = list;
        billDao = new BillDao(context);
    }

    @NonNull
    @Override
    public LuuTruXuatAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_bill_luutru, null);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LuuTruXuatAdapter.viewholder holder, int position) {
        Bill bill = list.get(position);
        userDao = new UserDao(context);
        laydl();
        holder.txtnguoitao.setText(bill.getCreatedByUser());
        holder.txtngaytao.setText(bill.getCreatedDate());
        holder.txttongtien.setText(bill.getTongtien());
        holder.txtloaihd.setText(bill.getTypeBill());
        holder.txtghichu.setText(bill.getNote());
        holder.txtstt.setText(String.valueOf(position + 1));
        if(dk!=1){
            userDao.updateLastAction(user,"xem hóa đơn xuất lưu trữ");
        }


        holder.imghuyluutru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("chú ý");
                builder.setMessage("bạn có chắc chắn muốn bỏ lưu trữ hóa đơn này không");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        laydl();
                        dk=1;
                        userDao.updateLastAction(user,"huỷ lưu trữ hóa đơn xuất");
                        int idht = bill.getIdBill();
                        Toastdep(2,"hủy lưu trữ thành công");
                        String epid = String.valueOf(idht);
                        billDao.huyluuTrubill(epid);
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
        TextView txttongtien,txtghichu,txtloaihd,txtnguoitao,txtngaytao,txtstt;
        ImageView imghuyluutru;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            // anh xa
            txttongtien = itemView.findViewById(R.id.itembill_tongtienlt);
            txtghichu = itemView.findViewById(R.id.itembill_notelt);
            txtloaihd = itemView.findViewById(R.id.itembill_loaihdlt);
            txtnguoitao = itemView.findViewById(R.id.itembill_nguoitaolt);
            txtngaytao = itemView.findViewById(R.id.itembill_ngaytaolt);
            txtstt = itemView.findViewById(R.id.txtsttbilllt);
            imghuyluutru = itemView.findViewById(R.id.itembill_btnhuyluutrubill);
        }


    }
    public void hienthidulieu(){
        list.clear();
        list.addAll(billDao.getBillsByTypeAndStatus("xuất kho","luutru"));
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
