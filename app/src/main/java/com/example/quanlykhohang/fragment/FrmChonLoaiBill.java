package com.example.quanlykhohang.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.quanlykhohang.Interface.FragmentChangeListener;
import com.example.quanlykhohang.Interface.Typetoolbar;
import com.example.quanlykhohang.R;


public class FrmChonLoaiBill extends Fragment implements FragmentChangeListener {

    public FrmChonLoaiBill() {
        // Required empty public constructor
    }
    Button nhap, xuat;
    private Typetoolbar dataPassListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frm_chon_loai_bill, container, false);
        nhap = view.findViewById(R.id.btnbillnhap);
        xuat = view.findViewById(R.id.btnbillxuat);
        dataPassListener.onDataPass("menu");

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Thiết lập hiển thị nút "Up" trên ActionBar
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icmenuthuong);
        }

        nhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrmQuanLyBillNhap newFragment = new FrmQuanLyBillNhap();
                replaceFragment(newFragment);
            }
        });
        xuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrmQuanLyBillXuat newFragment = new FrmQuanLyBillXuat();
                replaceFragment(newFragment);
            }
        });

        return view;
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmnav, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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