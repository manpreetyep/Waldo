package com.example.waldo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.waldo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPassword extends AppCompatActivity {

    @BindView(R.id.txt_reset)
    TextView txt_reset;

    @BindView(R.id.txt_back)
    TextView txt_back;

    @BindView(R.id.ed_email)
    EditText ed_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.txt_reset,R.id.txt_back})
    void clickAction(View view){
        if(view == txt_reset) {

            if(ed_email.getText().toString().isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                builder.setMessage("Please enter Email address");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }else{
                finish();
            }

        }else if(view == txt_back) {
            finish();
        }

    }


}
