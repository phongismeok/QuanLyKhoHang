package com.example.quanlykhohang.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quanlykhohang.Interface.FragmentChangeListener;
import com.example.quanlykhohang.Interface.OnImageSelectedListener;
import com.example.quanlykhohang.Interface.Typetoolbar;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.adapter.ProductAdapter;
import com.example.quanlykhohang.dao.ProductDao;
import com.example.quanlykhohang.dao.UserDao;
import com.example.quanlykhohang.model.Bill;
import com.example.quanlykhohang.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class FrmQuanLyProduct extends Fragment implements OnImageSelectedListener, FragmentChangeListener {

    public FrmQuanLyProduct() {
        // Required empty public constructor
    }

    RecyclerView rcvpro;
    FloatingActionButton fltaddpro;


    ProductDao prodao;
    UserDao userDao;
    ProductAdapter productAdapter;
    private ArrayList<Product> list = new ArrayList<>();
    private ArrayList<Product> listtk = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imganh;
    String user;
    byte[] img;
    private FloatingActionButton fabOption1;
    private FloatingActionButton fabOption2;
    private boolean isMenuOpen = false;
    private Typetoolbar dataPassListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frm_quan_ly_product, container, false);
        rcvpro = view.findViewById(R.id.rcvpro);
        fltaddpro = view.findViewById(R.id.fltaddpro);

        prodao = new ProductDao(getActivity());
        userDao = new UserDao(getActivity());

        fabOption1 = view.findViewById(R.id.fab_option1);
        fabOption2 = view.findViewById(R.id.fab_option2);
        EditText edttimkiem = view.findViewById(R.id.edttimkiemproduct);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvpro.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(getActivity(), list, this);
        rcvpro.setAdapter(productAdapter);

        dataPassListener.onDataPass("menu");


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Thiết lập hiển thị nút "Up" trên ActionBar
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icmenuthuong);
        }

        hienthidulieu();

        fltaddpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMenu();
            }
        });
        fabOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thempro();
                toggleMenu();
            }
        });

        fabOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrmDanhSachLuuTru newFragment = new FrmDanhSachLuuTru(); // chuyen ve frm chon loai cau hoi
                replaceFragment(newFragment);
                toggleMenu();
            }
        });

        edttimkiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list.clear();
                for (Product product:listtk) {
                    if(String.valueOf(product.getName()).
                            contains(charSequence.toString())){
                        list.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



// cho rcv ra giua
        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 1);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rcvpro.setLayoutManager(layoutManager1);
        laydl();

        return view;
    }

    public void thempro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //tao view va gan gan layout
        LayoutInflater inflater = getLayoutInflater();
        View view2 = inflater.inflate(R.layout.dialog_product, null);
        builder.setView(view2); // gan view vao hop thoai
        Dialog dialog = builder.create();
        dialog.show();// show hop thoai
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtten = view2.findViewById(R.id.edt_dialogpro_ten);
        EditText edtgianhap = view2.findViewById(R.id.edt_dialogpro_gianhap);
        EditText edtgiaxuat = view2.findViewById(R.id.edt_dialogpro_giaxuat);
        Button btnthempro = view2.findViewById(R.id.btn_dialogpro_addpro);
        imganh = view2.findViewById(R.id.IMGpro);
        ImageView btnlayanh = view2.findViewById(R.id.btnAddIMGpro);

        btnlayanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        btnthempro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenpro = edtten.getText().toString();
                String gianhap = edtgianhap.getText().toString();
                String giaxuat = edtgiaxuat.getText().toString();

                try {
                    BitmapDrawable drawable = (BitmapDrawable) imganh.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // chuyen hinh ve
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    // khai bao mang chua du lieu
                    img = stream.toByteArray();

                    if (img != null && img.length > 0) {
                        // Dữ liệu ảnh có giá trị, tiếp tục thêm vào cơ sở dữ liệu
                    } else {
                        Toastdep(1, "Lỗi khi chuyển đổi ảnh");
                        return;
                    }
                } catch (Exception e) {
                    Toastdep(1, "bạn chưa chọn ảnh");
                }


                if (TextUtils.isEmpty(tenpro) || TextUtils.isEmpty(gianhap) || TextUtils.isEmpty(giaxuat)) {
                    Toastdep(1, "Nhập đầy đủ thông tin");
                } else {
                    if (prodao.checktrungten(tenpro)) {
                        Toastdep(1, "tên sản phẩm này đã tồn tại trong kho hoặc kho lưu trữ");
                    } else {
                        try {
                            int gnhap = Integer.parseInt(gianhap);
                            int gxuat = Integer.parseInt(giaxuat);
                            if (gnhap < 0 || gxuat < 0) {
                                Toastdep(1, "giá nhập xuất phải lớn hơn 0");
                            } else {
                                Product product = new Product(tenpro, gianhap, giaxuat, img, "ok", user, 0);
                                if (prodao.insertProduct(product)) {
                                    dialog.dismiss();
                                    Toastdep(2, "Thêm thành công");
                                    userDao.updateLastAction(user, "Thêm sản phẩm");
                                    hienthidulieu();
                                    productAdapter.notifyDataSetChanged();
                                } else {
                                    Toastdep(1, "Thêm thất bại");
                                }
                            }
                        } catch (Exception e) {
                            Toastdep(1, "giá nhập và xuất phải là số");
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // lay du lieu uri laf duong dan trong android
            Uri uri = data.getData();
            Log.d("ImagePath", "Selected Image Path: " + uri.toString());
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (!(imganh == null)) {
                    imganh.setImageBitmap(bitmap);
                } else {
                    imganh.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onImageSelected(Product product, ImageView anhsp) {
        if (anhsp == null) {
            anhsp = imganh;
        }
        this.imganh = anhsp;
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    public void laydl() {
        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("TK", Context.MODE_PRIVATE);
        user = sharedPreferences2.getString("tk", "");
        Log.d("neee", "laydl: " + user);
    }

    public void hienthidulieu() {
        list.clear();
        list.addAll(prodao.getProductByStatus("ok"));
        listtk.addAll(prodao.getProductByStatus("ok"));
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmnav, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void Toastdep(int kieu, String nd) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, getActivity().findViewById(R.id.custom_toast_layout));

// Tùy chỉnh nội dung Toast
        ImageView imageView = layout.findViewById(R.id.custom_toast_icon);
        if (kieu == 1) {
            Glide.with(getActivity()).asGif().load(R.raw.anhthsai).into(imageView);
        } else {
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