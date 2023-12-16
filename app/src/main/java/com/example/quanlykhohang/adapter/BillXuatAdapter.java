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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlykhohang.Interface.FragmentChangeListener;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.BillDao;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.fragment.FrmXemBillDetail;
import com.example.quanlykhohang.model.Bill;

import java.util.ArrayList;

public class BillXuatAdapter extends RecyclerView.Adapter<BillXuatAdapter.viewholder>{
    private final Context context;
    private final ArrayList<Bill> list;
    private FragmentChangeListener fragmentChangeListener;
    BillDao billDao;
    String user;
    UserDao userDao;
    int dk=0;

    public BillXuatAdapter(Context context, ArrayList<Bill> list, FragmentChangeListener fragmentChangeListener) {
        this.context = context;
        this.list = list;
        this.fragmentChangeListener = fragmentChangeListener;
        billDao = new BillDao(context);
    }

    @NonNull
    @Override
    public BillXuatAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_bill, null);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillXuatAdapter.viewholder holder, int position) {
        Bill bill = list.get(position);
        userDao = new UserDao(context);
        laydl();
        holder.txtnguoitao.setText(bill.getCreatedByUser());
        holder.txtngaytao.setText(bill.getCreatedDate());
        holder.txttongtien.setText(bill.getTongtien());
        holder.txtloaihd.setText(bill.getTypeBill());
        holder.txtnote.setText(bill.getNote());
        if(dk!=1){
            userDao.updateLastAction(user,"xem hóa đơn xuất");
        }

        holder.txtstt.setText(String.valueOf(position+1));
        holder.btnxemct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laydl();
                userDao.updateLastAction(user,"xem hóa đơn xuất chi tiết");
                int idbill = bill.getIdBill();
                String epidbill = String.valueOf(idbill);
                String loaibill = bill.getTypeBill();
                SharedPreferences sharedPreferences = context.getSharedPreferences("IDBILL", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phongidbill", epidbill);
                editor.putString("phongloaibill", loaibill);
                editor.apply();

                FrmXemBillDetail newFragment = new FrmXemBillDetail();
                fragmentChangeListener.replaceFragment(newFragment);
            }
        });
        int idbill = bill.getIdBill();

        holder.imgluutru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laydl();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("chú ý");
                builder.setMessage("bạn có chắc chắn muốn lưu trữ hóa đơn này không");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        billDao.luuTrubill(String.valueOf(idbill));
                        userDao.updateLastAction(user,"lưu trữ hóa đơn xuất");
                        dk = 1;
                        Toastdep(2,"Lưu trữ thành công");
                        hientthidl();
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
        TextView txtnguoitao,txtngaytao,txttongtien,txtloaihd,txtnote,txtstt;
        ImageView btnxemct,imgluutru;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            // anh xa
            txtnguoitao = itemView.findViewById(R.id.itembill_nguoitao);
            txtngaytao = itemView.findViewById(R.id.itembill_ngaytao);
            txttongtien = itemView.findViewById(R.id.itembill_tongtien);
            txtloaihd = itemView.findViewById(R.id.itembill_loaihd);
            txtnote = itemView.findViewById(R.id.itembill_note);
            txtstt = itemView.findViewById(R.id.txtsttbill);
            btnxemct = itemView.findViewById(R.id.itembill_btnxemchitiet);
            imgluutru = itemView.findViewById(R.id.imageluutru);
        }


    }
    public void hientthidl() {
        list.clear();
        list.addAll(billDao.getBillsByTypeAndStatus("xuất kho", "ok"));
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
