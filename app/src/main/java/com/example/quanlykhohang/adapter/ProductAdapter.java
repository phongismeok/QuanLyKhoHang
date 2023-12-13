package com.example.quanlykhohang.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewholder> {
    private final Context context;
    private final ArrayList<Product> list;
    ProductDao productDao;
    private OnImageSelectedListener imageSelectedListener;
    ImageView imganhud;
    String ngayhomnay;
    UserDao userDao;
    String user;
    int dk=0;

    public ProductAdapter(Context context, ArrayList<Product> list,OnImageSelectedListener onImageSelectedListener) {
        this.context = context;
        this.list = list;
        this.imageSelectedListener = onImageSelectedListener;
        productDao = new ProductDao(context);
    }

    @NonNull
    @Override
    public ProductAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_product, null);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.viewholder holder, int position) {
        Product product = list.get(position);
        userDao = new UserDao(context);
        laydl();

        holder.txttensp.setText(product.getName());
        holder.txtsl.setText(product.getQuantity()+"");
        holder.txtgianhap.setText(product.getPricenhap());
        holder.txtgiaxuat.setText(product.getPricexuat());
        holder.txtnguoithem.setText(product.getUsername());

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
        if(dk!=1){
            userDao.updateLastAction(user,"Xem sản phẩm");
        }

        holder.btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatepro(product);
            }
        });

        Calendar ngay = Calendar.getInstance();//tạo đối tượng để lấy ngày giờ hiện tại
        int year = ngay.get(Calendar.YEAR);
        int month = ngay.get(Calendar.MONTH)+1;
        int day = ngay.get(Calendar.DAY_OF_MONTH);
        ngayhomnay = day+"/"+month+"/"+year;

        holder.btnluutru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("chú ý");
                builder.setMessage("bạn có chắc chắn muốn lưu trữ sản phẩm này không");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userDao.updateLastAction(user,"Lưu trữ sản phẩm");
                        int layidpro = product.getIdProduct();
                        dk = 1;
                        String epid = String.valueOf(layidpro);
                        productDao.luuTruProduct(epid);
                        Toastdep(2,"Lưu trữ thành công");
                        productDao.updateDate(layidpro,ngayhomnay);
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
        TextView txttensp, txtsl, txtgianhap, txtgiaxuat, txtnguoithem;
        ImageView imganhsp;
        ImageView btnsua, btnluutru;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            // anh xa
            txttensp = itemView.findViewById(R.id.itempro_name);
            txtsl = itemView.findViewById(R.id.itempro_quantity);
            txtgianhap = itemView.findViewById(R.id.itempro_pricenhap);
            txtgiaxuat = itemView.findViewById(R.id.itempro_pricexuat);
            txtnguoithem = itemView.findViewById(R.id.itempro_username);
            imganhsp = itemView.findViewById(R.id.itempro_photo);
            btnsua = itemView.findViewById(R.id.itempro_btnsua);
            btnluutru = itemView.findViewById(R.id.itempro_btnluutru);
        }


    }

    public void updatepro(Product pro) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // tao view , gan layout vao view
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_product, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // anh xa
        EditText edtten = view.findViewById(R.id.edt_dialogpro_ten);
        EditText edtgianhap = view.findViewById(R.id.edt_dialogpro_gianhap);
        EditText edtgiaxuat = view.findViewById(R.id.edt_dialogpro_giaxuat);
        Button btnsuapro = view.findViewById(R.id.btn_dialogpro_addpro);
        imganhud = view.findViewById(R.id.IMGpro);
        ImageView btnlayanh = view.findViewById(R.id.btnAddIMGpro);

        // gan du lieu tu textview len edittext
        edtten.setText(pro.getName());
        edtgianhap.setText(pro.getPricenhap());
        edtgiaxuat.setText(pro.getPricexuat());

        byte[] imgData = pro.getPhoto();
        if (imgData != null && imgData.length > 0) {
            Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
            imganhud.setImageBitmap(imgBitmap);

            btnlayanh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageSelectedListener.onImageSelected(pro, imganhud);
                }
            });
        }
        btnsuapro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDao userDao1 = new UserDao(context);
                // lay du lieu tu cac editText them vao bien de check
                String check1 = edtten.getText().toString();
                String check2 = edtgianhap.getText().toString();
                String check3 = edtgiaxuat.getText().toString();

                Bitmap bitmap = ((BitmapDrawable) imganhud.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                pro.setPhoto(byteArray);

                if (TextUtils.isEmpty(check1) || TextUtils.isEmpty(check2)|| TextUtils.isEmpty(check3)) {
                    Toastdep(1,"Nhập đầy đủ thông tin");

                } else {
                    if(productDao.checktrungten(check1)){
                        String tenss1 = pro.getName();
                        String tenss2 = edtten.getText().toString();
                        Log.d("aaaaa", "onClick: ten 1 "+tenss1);
                        Log.d("aaaaa", "onClick: ten 2 "+tenss2);
                        if(tenss1.equals(tenss2)){
                            try {
                                int c1 = Integer.parseInt(check2);
                                int c2 = Integer.parseInt(check3);
                                if(c1<0||c2<0){
                                    Toastdep(1,"giá nhập xuất phải lớn hơn 0");
                                }
                                else{
                                    pro.setName(edtten.getText().toString());
                                    pro.setPricenhap(edtgianhap.getText().toString());
                                    pro.setPricexuat(edtgiaxuat.getText().toString());
                                    if (productDao.updateProduct(pro)) {

                                        laydl();

                                        userDao1.updateLastAction(user,"Sửa sản phẩm");
                                        dk = 1;
                                        dialog.dismiss();
                                        Toastdep(2,"Update thành công");
                                        hienthidulieu();
                                    } else {
                                        Toastdep(1,"Update thất bại");
                                    }
                                }
                            }catch (Exception e){
                                Toastdep(1,"giá nhập xuất phải là số");
                            }
                        }
                        else{
                            //khac ten dang sua va trung voi he thong
                            Toastdep(1,"Tên này đã tồn tại trong hệ thống");
                        }
                    }else{
                        try {
                            int c1 = Integer.parseInt(check2);
                            int c2 = Integer.parseInt(check3);
                            if(c1<0||c2<0){
                                Toastdep(1,"giá nhập xuất phải lớn hơn 0");
                            }
                            else{
                                pro.setName(edtten.getText().toString());
                                pro.setPricenhap(edtgianhap.getText().toString());
                                pro.setPricexuat(edtgiaxuat.getText().toString());
                                if (productDao.updateProduct(pro)) {
                                    dialog.dismiss();
                                    Toastdep(2,"Update thành công");
                                    hienthidulieu();
                                } else {
                                    Toastdep(1,"Update thất bại");
                                }
                            }
                        }catch (Exception e){
                            Toastdep(1,"giá nhập xuất phải là số");
                        }
                    }
                }
            }
        });


    }
    public void hienthidulieu(){
        list.clear();
        list.addAll(productDao.getProductByStatus("ok"));
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
