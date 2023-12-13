package com.example.quanlykhohang.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlykhohang.Interface.FragmentChangeListener;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.adapter.BillDetailAdapter;
import com.example.quanlykhohang.adapter.UserAdapter;
import com.example.quanlykhohang.dao.BillDao;
import com.example.quanlykhohang.dao.BillDetailDao;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.model.BillDetail;
import com.example.quanlykhohang.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FrmXemBillDetail extends Fragment implements FragmentChangeListener {

    public FrmXemBillDetail() {
        // Required empty public constructor
    }
    RecyclerView rcvbilldt;

    BillDetailDao billDetailDao;
    BillDetailAdapter billDetailAdapter;
    private ArrayList<BillDetail> list = new ArrayList<>();
    String idbill;
    int epidbill;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frm_xem_bill_detail, container, false);
        rcvbilldt = view.findViewById(R.id.rcvbilldt);
        billDetailDao = new BillDetailDao(getActivity());

        nhandl();
        epidbill = Integer.parseInt(idbill);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FrmChonLoaiBill newFragment = new FrmChonLoaiBill(); // chuyen ve frm chon loai cau hoi
                replaceFragment(newFragment);
            }
        });

        list = (ArrayList<BillDetail>) billDetailDao.getBillDetailsByBillId(epidbill);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvbilldt.setLayoutManager(layoutManager);
        billDetailAdapter = new BillDetailAdapter(getActivity(),list);
        rcvbilldt.setAdapter(billDetailAdapter);

        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 1);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rcvbilldt.setLayoutManager(layoutManager1);
        return view;
    }
    public void nhandl(){
        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("IDBILL", Context.MODE_PRIVATE);
        idbill = sharedPreferences2.getString("phongidbill", "");
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