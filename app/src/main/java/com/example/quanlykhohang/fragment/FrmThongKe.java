package com.example.quanlykhohang.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlykhohang.Interface.Typetoolbar;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.BillDao;
import com.example.quanlykhohang.dao.ProductDao;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.database.DbHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.security.AlgorithmParameterGenerator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FrmThongKe extends Fragment {

    BarChart mpBarchart;
    Spinner spnthang;
    String thangdangchon;
    int sl;
    int sl2, sl3;
    int tonggianhap = 0, tonggiaxuat = 0, tongdoanhthu = 0;
    String tonggianhapp, tonggiaxuatt;
    String user;
    UserDao userDao;
    ProductDao productDao;
    private Typetoolbar dataPassListener;
    String namht;
    int dk=0;
    TextView txtslnhap, txtslxuat, txtslcon, txttiennhap, txttienxuat, txtdoanhthu;

    public FrmThongKe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frm_thong_ke, container, false);
        mpBarchart = view.findViewById(R.id.barChart);
        spnthang = view.findViewById(R.id.spinner_thang);
        txtslnhap = view.findViewById(R.id.txttkslnhap);
        txtslxuat = view.findViewById(R.id.txttkslxuat);
        txtslcon = view.findViewById(R.id.txttkslconlai);
        txttiennhap = view.findViewById(R.id.txttktiennhap);
        txttienxuat = view.findViewById(R.id.txttktienxuat);
        txtdoanhthu = view.findViewById(R.id.txttkdoanhthu);
        userDao = new UserDao(getActivity());
        productDao = new ProductDao(getActivity());
        dataPassListener.onDataPass("menu");
        laydl();
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");
        list.add("12");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.itemspinner, list);
        spnthang.setAdapter(adapter);

        Calendar lich = Calendar.getInstance();//tạo đối tượng để lấy ngày giờ hiện tại
        int year = lich.get(Calendar.YEAR);
        namht = String.valueOf(year);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Thiết lập hiển thị nút "Up" trên ActionBar
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icmenuthuong);
        }

        BillDao billDao = new BillDao(getActivity());
        if(dk!=1){
            userDao.updateLastAction(user,"xem thống kê");
        }

        spnthang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thangdangchon = list.get(position);
                Log.d("kkkkk", "onItemSelected: " + thangdangchon);
                if (thangdangchon.equals("1") || thangdangchon.equals("3") || thangdangchon.equals("5") || thangdangchon.equals("7") || thangdangchon.equals("8") || thangdangchon.equals("10") || thangdangchon.equals("12")) {
                    // code thang 31 ngay
                    String bienthaythe = thangdangchon;
                    String thoigiantv = "1" + "/" + bienthaythe + "/" + namht;
                    String thoigiantv2 = "31" + "/" + bienthaythe + "/" + namht;
                    sl = billDao.getQuantityByTypeBillAndDateRange("nhập kho", thoigiantv, thoigiantv2);
                    sl2 = billDao.getQuantityByTypeBillAndDateRange("xuất kho", thoigiantv, thoigiantv2);
                    sl3 = sl - sl2;
                    int tong = 0;
                    int sltinhton = 0;
                    int giaton = 0;
                    List<String> idProductList = productDao.demsoluongton();
                    for(int k = 0; k<idProductList.size();k++){
                        giaton = Integer.parseInt(productDao.getPriceNhapByName(idProductList.get(k)));
                        sltinhton = Integer.parseInt(productDao.getSLTonByName(idProductList.get(k)));
                        tong+= (giaton*sltinhton);
                    }


                    tonggianhapp = String.valueOf(billDao.laytongdiem("nhập kho", thoigiantv, thoigiantv2));
                    tonggiaxuatt = String.valueOf(billDao.laytongdiem("xuất kho", thoigiantv, thoigiantv2));

                    tonggianhap = Integer.parseInt(tonggianhapp);
                    tonggiaxuat = Integer.parseInt(tonggiaxuatt);
                    if(tonggiaxuat==0){
                        tongdoanhthu = 0;
                    }
                    else{
                        tongdoanhthu = (tonggiaxuat+tong) - tonggianhap;
                    }


                    txttiennhap.setText(tonggianhapp + " VND");
                    txttienxuat.setText(tonggiaxuatt + " VND");
                    txtdoanhthu.setText(tongdoanhthu + " VND");

                    txtslnhap.setText(String.valueOf(sl));
                    txtslxuat.setText(String.valueOf(sl2));
                    txtslcon.setText(String.valueOf(sl3));

                    ArrayList<BarEntry> barEntries1 = new ArrayList<>();
                    barEntries1.add(new BarEntry(0, sl));
                    ArrayList<BarEntry> barEntries2 = new ArrayList<>();
                    barEntries2.add(new BarEntry(0, sl2));
                    ArrayList<BarEntry> barEntries3 = new ArrayList<>();
                    barEntries3.add(new BarEntry(0, sl3));

                    BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Nhập kho");
                    barDataSet1.setColor(Color.RED);

                    BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Xuất kho");
                    barDataSet2.setColor(Color.BLUE);

                    BarDataSet barDataSet3 = new BarDataSet(barEntries3, "Tồn kho");
                    barDataSet3.setColor(Color.GREEN);

                    BarData data = new BarData(barDataSet1, barDataSet2, barDataSet3);
                    mpBarchart.setData(data);

                    XAxis xAxis = mpBarchart.getXAxis();
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1);
                    xAxis.setGranularityEnabled(true);

                    mpBarchart.setDragEnabled(true);
                    mpBarchart.setVisibleXRangeMaximum(4);
//
                    float barSpace = 0.1f;  // Điều chỉnh khoảng cách giữa các cột
                    float groupSpace = 0.2f; // Điều chỉnh khoảng cách giữa các nhóm
                    data.setBarWidth(0.25f);
//
                    mpBarchart.getXAxis().setAxisMinimum(0);
                    mpBarchart.getXAxis().setAxisMaximum(0 + mpBarchart.getBarData().getGroupWidth(groupSpace, barSpace) * 1);
                    mpBarchart.getAxisLeft().setAxisMinimum(0);
//
                    mpBarchart.groupBars(0, groupSpace, barSpace);
                    mpBarchart.invalidate();

                } else if (thangdangchon.equals("2")) {

                    if (isLeapYear(Integer.parseInt(namht))) {
                        // nam nhuan
                        String bienthaythe = thangdangchon;
                        String thoigiantv = "1" + "/" + bienthaythe + "/" + namht;
                        String thoigiantv2 = "29" + "/" + bienthaythe + "/" + namht;
                        int sl = billDao.getQuantityByTypeBillAndDateRange("nhập kho", thoigiantv, thoigiantv2);
                        int sl2 = billDao.getQuantityByTypeBillAndDateRange("xuất kho", thoigiantv, thoigiantv2);
                        sl3 = sl - sl2;
                        txtslnhap.setText(String.valueOf(sl));
                        txtslxuat.setText(String.valueOf(sl2));
                        txtslcon.setText(String.valueOf(sl3));

                        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
                        barEntries1.add(new BarEntry(0, sl));
                        ArrayList<BarEntry> barEntries2 = new ArrayList<>();
                        barEntries2.add(new BarEntry(0, sl2));
                        ArrayList<BarEntry> barEntries3 = new ArrayList<>();
                        barEntries3.add(new BarEntry(0, sl3));

                        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Nhập kho");
                        barDataSet1.setColor(Color.RED);

                        BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Xuất kho");
                        barDataSet2.setColor(Color.BLUE);

                        BarDataSet barDataSet3 = new BarDataSet(barEntries3, "Tồn kho");
                        barDataSet3.setColor(Color.GREEN);

                        BarData data = new BarData(barDataSet1, barDataSet2, barDataSet3);
                        mpBarchart.setData(data);

                        XAxis xAxis = mpBarchart.getXAxis();
                        xAxis.setCenterAxisLabels(true);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1);
                        xAxis.setGranularityEnabled(true);

                        mpBarchart.setDragEnabled(true);
                        mpBarchart.setVisibleXRangeMaximum(4);
//
                        float barSpace = 0.1f;  // Điều chỉnh khoảng cách giữa các cột
                        float groupSpace = 0.2f; // Điều chỉnh khoảng cách giữa các nhóm
                        data.setBarWidth(0.25f);
//
                        mpBarchart.getXAxis().setAxisMinimum(0);
                        mpBarchart.getXAxis().setAxisMaximum(0 + mpBarchart.getBarData().getGroupWidth(groupSpace, barSpace) * 1);
                        mpBarchart.getAxisLeft().setAxisMinimum(0);
//
                        mpBarchart.groupBars(0, groupSpace, barSpace);
                        mpBarchart.invalidate();

                    } else {
                        // ko phai nam nhuan
                        String bienthaythe = thangdangchon;
                        String thoigiantv = "1" + "/" + bienthaythe + "/" + namht;
                        String thoigiantv2 = "28" + "/" + bienthaythe + "/" + namht;
                        int sl = billDao.getQuantityByTypeBillAndDateRange("nhập kho", thoigiantv, thoigiantv2);
                        int sl2 = billDao.getQuantityByTypeBillAndDateRange("xuất kho", thoigiantv, thoigiantv2);
                        sl3 = sl - sl2;
                        txtslnhap.setText(String.valueOf(sl));
                        txtslxuat.setText(String.valueOf(sl2));
                        txtslcon.setText(String.valueOf(sl3));

                        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
                        barEntries1.add(new BarEntry(0, sl));
                        ArrayList<BarEntry> barEntries2 = new ArrayList<>();
                        barEntries2.add(new BarEntry(0, sl2));
                        ArrayList<BarEntry> barEntries3 = new ArrayList<>();
                        barEntries3.add(new BarEntry(0, sl3));

                        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Nhập kho");
                        barDataSet1.setColor(Color.RED);

                        BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Xuất kho");
                        barDataSet2.setColor(Color.BLUE);

                        BarDataSet barDataSet3 = new BarDataSet(barEntries3, "Tồn kho");
                        barDataSet3.setColor(Color.GREEN);

                        BarData data = new BarData(barDataSet1, barDataSet2, barDataSet3);
                        mpBarchart.setData(data);

                        XAxis xAxis = mpBarchart.getXAxis();
                        xAxis.setCenterAxisLabels(true);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1);
                        xAxis.setGranularityEnabled(true);

                        mpBarchart.setDragEnabled(true);
                        mpBarchart.setVisibleXRangeMaximum(4);
//
                        float barSpace = 0.1f;  // Điều chỉnh khoảng cách giữa các cột
                        float groupSpace = 0.2f; // Điều chỉnh khoảng cách giữa các nhóm
                        data.setBarWidth(0.25f);
//
                        mpBarchart.getXAxis().setAxisMinimum(0);
                        mpBarchart.getXAxis().setAxisMaximum(0 + mpBarchart.getBarData().getGroupWidth(groupSpace, barSpace) * 1);
                        mpBarchart.getAxisLeft().setAxisMinimum(0);
//
                        mpBarchart.groupBars(0, groupSpace, barSpace);
                        mpBarchart.invalidate();

                    }
                } else {
                    // code thang 30 ngay
                    String bienthaythe = thangdangchon;
                    String thoigiantv = "1" + "/" + bienthaythe + "/" + namht;
                    String thoigiantv2 = "30" + "/" + bienthaythe + "/" + namht;
                    int sl = billDao.getQuantityByTypeBillAndDateRange("nhập kho", thoigiantv, thoigiantv2);
                    int sl2 = billDao.getQuantityByTypeBillAndDateRange("xuất kho", thoigiantv, thoigiantv2);
                    sl3 = sl - sl2;
                    txtslnhap.setText(String.valueOf(sl));
                    txtslxuat.setText(String.valueOf(sl2));
                    txtslcon.setText(String.valueOf(sl3));

                    ArrayList<BarEntry> barEntries1 = new ArrayList<>();
                    barEntries1.add(new BarEntry(0, sl));
                    ArrayList<BarEntry> barEntries2 = new ArrayList<>();
                    barEntries2.add(new BarEntry(0, sl2));
                    ArrayList<BarEntry> barEntries3 = new ArrayList<>();
                    barEntries3.add(new BarEntry(0, sl3));

                    BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Nhập kho");
                    barDataSet1.setColor(Color.RED);

                    BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Xuất kho");
                    barDataSet2.setColor(Color.BLUE);

                    BarDataSet barDataSet3 = new BarDataSet(barEntries3, "Tồn kho");
                    barDataSet3.setColor(Color.GREEN);

                    BarData data = new BarData(barDataSet1, barDataSet2, barDataSet3);
                    mpBarchart.setData(data);

                    XAxis xAxis = mpBarchart.getXAxis();
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1);
                    xAxis.setGranularityEnabled(true);

                    mpBarchart.setDragEnabled(true);
                    mpBarchart.setVisibleXRangeMaximum(4);
//
                    float barSpace = 0.1f;  // Điều chỉnh khoảng cách giữa các cột
                    float groupSpace = 0.2f; // Điều chỉnh khoảng cách giữa các nhóm
                    data.setBarWidth(0.25f);
//
                    mpBarchart.getXAxis().setAxisMinimum(0);
                    mpBarchart.getXAxis().setAxisMaximum(0 + mpBarchart.getBarData().getGroupWidth(groupSpace, barSpace) * 1);
                    mpBarchart.getAxisLeft().setAxisMinimum(0);
//
                    mpBarchart.groupBars(0, groupSpace, barSpace);
                    mpBarchart.invalidate();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public boolean isLeapYear(int year) {
        // Sử dụng lớp Calendar để kiểm tra năm nhuận
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);

        // Phương thức isLeapYear() trả về true nếu năm là năm nhuận
        return calendar.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }
    public void laydl(){
        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("TK", Context.MODE_PRIVATE);
        user = sharedPreferences2.getString("tk", "");
        Log.d("neee", "laydl: "+user);
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