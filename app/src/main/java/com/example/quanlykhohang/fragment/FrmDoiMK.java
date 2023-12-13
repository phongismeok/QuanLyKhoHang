package com.example.quanlykhohang.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.UserDao;


public class FrmDoiMK extends Fragment {

    public FrmDoiMK() {
        // Required empty public constructor
    }

    EditText edtmkc, edtmkm, edtrmkm;
    Button btndmk;
    String tk,mk;
    UserDao userDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frm_doi_m_k, container, false);
        edtmkc = view.findViewById(R.id.edtdmkmkc);
        edtmkm = view.findViewById(R.id.edtdmkmkm);
        edtrmkm = view.findViewById(R.id.edtdmkrmkm);
        btndmk = view.findViewById(R.id.btndoimk);

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("TK", Context.MODE_PRIVATE);
        tk = sharedPreferences2.getString("tk", "");

        userDao = new UserDao(getActivity());
        mk = userDao.getPasswordByUsername(tk);

        btndmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mkc = edtmkc.getText().toString();
                String mkm = edtmkm.getText().toString();
                String rmkm = edtrmkm.getText().toString();


                if(TextUtils.isEmpty(mkc)|TextUtils.isEmpty(mkm)|TextUtils.isEmpty(rmkm)){
                    Toastdep(1,"vui lòng nhập đầy đủ thông tin");
                }
                else{
                    if(mkc.equals(mk)){
                        if(mkm.equals(rmkm)){
                            userDao.doimk(tk,mkm);
                            Toastdep(2,"đổi mật khẩu thành công");
                        }
                        else{
                            Toastdep(1,"mật khẩu nhập lại không chính xác");
                        }
                    }
                    else{
                        Toastdep(1,"mật khẩu cũ không chính xác");
                    }

                }
            }
        });

        return view;
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
}