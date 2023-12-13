package com.example.quanlykhohang.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quanlykhohang.Interface.Typetoolbar;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.adapter.DanhSachLuuTruAdapter;
import com.example.quanlykhohang.adapter.ProductAdapter;
import com.example.quanlykhohang.dao.ProductDao;
import com.example.quanlykhohang.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;


public class FrmDanhSachLuuTru extends Fragment {

    public FrmDanhSachLuuTru() {
        // Required empty public constructor
    }
    RecyclerView rcvluutru;

    ProductDao prodao;
    private Typetoolbar dataPassListener;
    DanhSachLuuTruAdapter productAdapter;
    private ArrayList<Product> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frm_danh_sach_luu_tru, container, false);
        rcvluutru = view.findViewById(R.id.rcvdanhsachluutru);
        prodao = new ProductDao(getActivity());
        dataPassListener.onDataPass("thoat");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvluutru.setLayoutManager(layoutManager);
        productAdapter = new DanhSachLuuTruAdapter(getActivity(),list);
        rcvluutru.setAdapter(productAdapter);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Thiết lập hiển thị nút "Up" trên ActionBar
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icquaylai);

        }



        hienthidulieu();

        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 1);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rcvluutru.setLayoutManager(layoutManager1);

        return view;
    }
    public void hienthidulieu(){
        list.clear();
        list.addAll(prodao.getProductByStatus("luutru"));
        productAdapter.notifyDataSetChanged();
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