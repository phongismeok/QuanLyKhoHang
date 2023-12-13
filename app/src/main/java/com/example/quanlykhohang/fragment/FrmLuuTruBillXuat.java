package com.example.quanlykhohang.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlykhohang.Interface.FragmentChangeListener;
import com.example.quanlykhohang.Interface.Typetoolbar;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.adapter.LuuTruNhapAdapter;
import com.example.quanlykhohang.adapter.LuuTruXuatAdapter;
import com.example.quanlykhohang.dao.BillDao;
import com.example.quanlykhohang.model.Bill;

import java.util.ArrayList;

public class FrmLuuTruBillXuat extends Fragment implements FragmentChangeListener {

    public FrmLuuTruBillXuat() {
        // Required empty public constructor
    }
    RecyclerView rcvluutruxuat;

    BillDao billDao;
    LuuTruXuatAdapter adapter;
    private ArrayList<Bill> list = new ArrayList<>();
    private Typetoolbar dataPassListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frm_luu_tru_bill_xuat, container, false);
        rcvluutruxuat = view.findViewById(R.id.rcvdanhsachluutruxuat);

        billDao = new BillDao(getActivity());
        dataPassListener.onDataPass("thoat");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvluutruxuat.setLayoutManager(layoutManager);
        adapter = new LuuTruXuatAdapter(getActivity(),list);
        rcvluutruxuat.setAdapter(adapter);

        hienthidulieu();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FrmQuanLyBillXuat newFragment = new FrmQuanLyBillXuat(); // chuyen ve frm chon loai cau hoi
                replaceFragment(newFragment);
            }
        });

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Thiết lập hiển thị nút "Up" trên ActionBar
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icquaylai);
        }

        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 1);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rcvluutruxuat.setLayoutManager(layoutManager1);
        return view;
    }
    public void hienthidulieu(){
        list.clear();
        list.addAll(billDao.getBillsByTypeAndStatus("xuất kho","luutru"));
        adapter.notifyDataSetChanged();
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

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmnav, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}