package com.example.waldo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.waldo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.txt_sign)
    TextView txt_sign;

    @BindView(R.id.txt_forgot)
    TextView txt_forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick ({R.id.txt_sign,R.id.txt_forgot})
    void Click(View view){
        if(view == txt_sign){
            startActivity(new Intent(LoginActivity.this,MainDashborad.class));
            finish();
        }else if(view == txt_forgot){
            startActivity(new Intent(LoginActivity.this,ForgotPassword.class));

        }

    }
}
