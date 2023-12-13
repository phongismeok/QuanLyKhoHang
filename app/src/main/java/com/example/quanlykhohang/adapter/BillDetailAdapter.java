package com.example.quanlykhohang.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.BillDetailDao;
import com.example.quanlykhohang.model.BillDetail;

import java.util.ArrayList;

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.viewholder>{
    private final Context context;
    private final ArrayList<BillDetail> list;
    BillDetailDao billDetailDao;

    public BillDetailAdapter(Context context, ArrayList<BillDetail> list) {
        this.context = context;
        this.list = list;
        billDetailDao = new BillDetailDao(context);
    }

    @NonNull
    @Override
    public BillDetailAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_billdetail, null);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillDetailAdapter.viewholder holder, int position) {
        BillDetail billDetail = list.get(position);
        holder.txttensp.setText(billDetail.getName());
        holder.txtgia.setText(billDetail.getPrice());
        holder.txtsl.setText(billDetail.getQuantity());
        holder.txtstt.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class viewholder extends RecyclerView.ViewHolder {
        // khai bao bien
        TextView txttensp,txtsl,txtgia,txtstt;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            // anh xa
            txtsl = itemView.findViewById(R.id.itembilldt_quantity);
            txttensp = itemView.findViewById(R.id.itembilldt_tensp);
            txtgia = itemView.findViewById(R.id.itembilldt_price);
            txtstt = itemView.findViewById(R.id.txtsttbilldt);
        }


    }
}
