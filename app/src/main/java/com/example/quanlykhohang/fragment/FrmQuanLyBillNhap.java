package com.example.quanlykhohang.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quanlykhohang.Interface.FragmentChangeListener;
import com.example.quanlykhohang.Interface.Typetoolbar;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.adapter.BillNhapAdapter;
import com.example.quanlykhohang.adapter.UserAdapter;
import com.example.quanlykhohang.dao.BillDao;
import com.example.quanlykhohang.dao.BillDetailDao;
import com.example.quanlykhohang.dao.ProductDao;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.model.Bill;
import com.example.quanlykhohang.model.BillDetail;
import com.example.quanlykhohang.model.Product;
import com.example.quanlykhohang.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


public class FrmQuanLyBillNhap extends Fragment implements FragmentChangeListener {


    public FrmQuanLyBillNhap() {
        // Required empty public constructor
    }
    RecyclerView rcvbillnhap;
    FloatingActionButton fltaddbillnhap;

    BillDao billDao;
    BillNhapAdapter adapter;
    Spinner spinnersp;
    String luachon;
    String sl,ghichu;
    TableLayout tableLayout;
    EditText edtsoluong;
    int rowCount;
    int trangthaichon = 0;
    int tongtien;
    ArrayList<Integer> giatien = new ArrayList<>();
    String user;
    ArrayList<String> themgia = new ArrayList<>();
    ArrayList<String> themten = new ArrayList<>();
    ArrayList<String> themsl = new ArrayList<>();
    long themid;
    HashSet<String> uniqueValues = new HashSet<>();
    private ArrayList<Bill> list = new ArrayList<>();
    private FloatingActionButton fabOption1;
    private FloatingActionButton fabOption2;
    private boolean isMenuOpen = false;
    private Typetoolbar dataPassListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frm_quan_ly_bill_nhap, container, false);
        rcvbillnhap = view.findViewById(R.id.rcvbillnhap);
        fltaddbillnhap = view.findViewById(R.id.fltbillnhap);
        billDao = new BillDao(getActivity());
        fabOption1 = view.findViewById(R.id.fab_option1bnhap);
        fabOption2 = view.findViewById(R.id.fab_option2bnhap);
        laydl();
        dataPassListener.onDataPass("thoat");

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Thiết lập hiển thị nút "Up" trên ActionBar
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icquaylai);
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FrmChonLoaiBill newFragment = new FrmChonLoaiBill(); // chuyen ve frm chon loai cau hoi
                replaceFragment(newFragment);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvbillnhap.setLayoutManager(layoutManager);
        adapter = new BillNhapAdapter(getActivity(),list,this);
        rcvbillnhap.setAdapter(adapter);

        fltaddbillnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMenu();
            }
        });
        fabOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thembill();
                toggleMenu();
            }
        });

        fabOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrmLuuTruBillNhap newFragment = new FrmLuuTruBillNhap(); // chuyen ve frm chon loai cau hoi
                replaceFragment(newFragment);
                toggleMenu();
            }
        });


        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 1);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rcvbillnhap.setLayoutManager(layoutManager1);

        hienthidl();
        return view;
    }

    public void thembill() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //tao view va gan gan layout
        LayoutInflater inflater = getLayoutInflater();
        View view2 = inflater.inflate(R.layout.dialog_billnhap, null);
        builder.setView(view2); // gan view vao hop thoai
        Dialog dialog = builder.create();
        dialog.show();// show hop thoai
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        spinnersp = view2.findViewById(R.id.spinner_sanpham);
        Button btnchon = view2.findViewById(R.id.btnchotdon);
        Button btnthem = view2.findViewById(R.id.btnthembillnhap);
        EditText edtghichu = view2.findViewById(R.id.edtghichu);
        edtsoluong = view2.findViewById(R.id.edtsl);
        tableLayout = view2.findViewById(R.id.tableLayout);

        ProductDao productDao = new ProductDao(getActivity());

        ArrayList<String> list = new ArrayList<>();
        list = (ArrayList<String>) productDao.gettentheott();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.itemspinner, list);
        spinnersp.setAdapter(adapter);


        ArrayList<String> finalList = list;

        if(list.size()==0){
            spinnersp.setVisibility(View.INVISIBLE);
            Toastdep(1,"Bạn chưa thêm sản phẩm");
        }
        else{
            spinnersp.setVisibility(View.VISIBLE);
        }

        spinnersp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                luachon = finalList.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnchon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sl = edtsoluong.getText().toString();

                if(sl.equals("")){
                    Toastdep(1,"Vui lòng nhập đủ thông tin");
                }else{
                    if(!uniqueValues.contains(luachon)){
                        TableRow tableRow = new TableRow(getActivity());

                        TextView textViewCol1 = new TextView(getActivity());
                        textViewCol1.setText(luachon);
                        textViewCol1.setGravity(Gravity.CENTER);
                        textViewCol1.setPadding(8, 8, 8, 8);
                        textViewCol1.setTextSize(25);

                        TextView textViewCol2 = new TextView(getActivity());
                        textViewCol2.setText(sl);
                        textViewCol2.setGravity(Gravity.CENTER);
                        textViewCol2.setPadding(8, 8, 8, 8);
                        textViewCol2.setTextSize(25);

                        tableRow.addView(textViewCol1);
                        tableRow.addView(textViewCol2);

                        tableLayout.addView(tableRow);
                        trangthaichon = 1;
                        uniqueValues.add(luachon);
                    }
                    else{
                        for (int i = 0; i < tableLayout.getChildCount(); i++) {
                            View view4 = tableLayout.getChildAt(i);
                            if (view4 instanceof TableRow) {
                                TableRow row = (TableRow) view4;
                                TextView textViewCol1 = (TextView) row.getChildAt(0); // Đây là TextView đầu tiên trong TableRow
                                TextView textViewCol2 = (TextView) row.getChildAt(1); // Đây là TextView thứ hai trong TableRow

                                String currentLuachon = textViewCol1.getText().toString();
                                if (currentLuachon.equals(luachon)) {
                                    // Cập nhật giá trị trong TextView nếu luachon trùng khớp
                                    textViewCol2.setText(sl);
                                    trangthaichon = 1;
                                    break; // Thoát khỏi vòng lặp sau khi đã cập nhật giá trị

                                }
                            }
                        }
                    }

                }

            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                uniqueValues.clear();
                trangthaichon = 0;
                themten.clear();
                themgia.clear();
                themsl.clear();
                giatien.clear();
            }
        });


        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ghichu = edtghichu.getText().toString();
                int columnIndexToRetrieve = 0; // Thay thế 1 bằng số cột bạn quan tâm (đếm từ 0)
                int columnIndexToRetrieve2 = 1; // Thay thế 1 bằng số cột bạn quan tâm (đếm từ 0)

                rowCount = tableLayout.getChildCount();
                List<String> columnDataList = new ArrayList<>();
                List<String> columnDataList2 = new ArrayList<>();

                for (int i = 0; i < rowCount; i++) {
                    View view3 = tableLayout.getChildAt(i);

                    if (view3 instanceof TableRow) {
                        TableRow row = (TableRow) view3;

                        int colCount = row.getChildCount();
                        if (columnIndexToRetrieve < colCount) {
                            View colView = row.getChildAt(columnIndexToRetrieve);
                            View colView2 = row.getChildAt(columnIndexToRetrieve2);

                            if (colView instanceof TextView) {
                                TextView textView = (TextView) colView;
                                String cellData = textView.getText().toString();
                                columnDataList.add(cellData);
                            }
                            if (colView2 instanceof TextView) {
                                TextView textView = (TextView) colView2;
                                String cellData2 = textView.getText().toString();
                                columnDataList2.add(cellData2);
                            }
                        }
                    }
                }
                // code them so luong vao database tai day
                for(int e=0;e<columnDataList.size();e++){
                    productDao.congsl(columnDataList.get(e), Integer.parseInt(columnDataList2.get(e)));

                    // code tinh tong tien
                    String gia = productDao.getPriceNhapByName(columnDataList.get(e));
                    int epgia = Integer.parseInt(gia);
                    int sl = Integer.parseInt(columnDataList2.get(e));
                    int tt1 = epgia*sl;
                    giatien.add(tt1);
                    tongtien = 0;
                    // Duyệt qua từng phần tử và cộng vào biến sum
                    for (int value : giatien) {
                        tongtien += value;
                    }

                    // code insert hoa don ct tai day
                    String ten = columnDataList.get(e);
                    String sluong = columnDataList2.get(e);
                    themgia.add(gia);
                    themten.add(ten);
                    themsl.add(sluong);


                }
                if(trangthaichon ==1){
                    Calendar ngay = Calendar.getInstance();//tạo đối tượng để lấy ngày giờ hiện tại
                    int year = ngay.get(Calendar.YEAR);
                    int month = ngay.get(Calendar.MONTH)+1;
                    int day = ngay.get(Calendar.DAY_OF_MONTH);
//                    String ngayhomnay = day+"/"+month+"/"+year;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String ngayhomnay = sdf.format(ngay.getTime());



                    String eptongtien = String.valueOf(tongtien);
                    // code them bill
                    Bill bill = new Bill("nhập kho",ngayhomnay,ghichu,eptongtien,"ok",user);


                    themid = billDao.insertBill(bill);
                    int epid = (int) themid;

                    Toastdep(2,"Thêm thành công");
                    hienthidl();

                    BillDetailDao billDetailDao = new BillDetailDao(getActivity());
                    Log.d("zzzzz", "onClick: id bill "+themid);
                    for(int f=0; f < themten.size();f++){
                        String addten = themten.get(f);
                        String addgia = themgia.get(f);
                        String addsl = themsl.get(f);
                        BillDetail billDetail = new BillDetail(epid,addsl,addten,addgia);
                        if(billDetailDao.insertBillDetail(billDetail)){

                        }

                    }

                    dialog.dismiss();
                }
                else{
                    Toastdep(1,"Bạn chưa nhập sản phẩm nào");
                }

            }
        });


    }
    public void laydl(){
        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("TK", Context.MODE_PRIVATE);
        user = sharedPreferences2.getString("tk", "");
        Log.d("neee", "laydl: "+user);
    }
    public void hienthidl(){
        list.clear();
        list.addAll(billDao.getBillsByTypeAndStatus("nhập kho","ok"));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmnav, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
    private void toggleMenu() {
        if (isMenuOpen) {
            hideMenu();
        } else {
            showMenu();
        }
    }

    private void showMenu() {
        isMenuOpen = true;
        fabOption1.show();
        fabOption2.show();
    }

    private void hideMenu() {
        isMenuOpen = false;
        fabOption1.hide();
        fabOption2.hide();
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