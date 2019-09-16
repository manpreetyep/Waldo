package com.example.waldo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waldo.R;
import com.example.waldo.Rest.ApiInterFace;
import com.example.waldo.Rest.Rest;
import com.example.waldo.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.txt_sign)
    TextView txt_sign;

    @BindView(R.id.txt_forgot)
    TextView txt_forgot;

    @BindView(R.id.ed_username)
    EditText ed_username;

    @BindView(R.id.ed_password)
    EditText ed_password;

    ProgressDialog progressDialog;

    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        manager = new SessionManager(this);
    }

    @OnClick ({R.id.txt_sign,R.id.txt_forgot})
    void Click(View view){
        if(view == txt_sign){

            if(ed_username.getText().toString().isEmpty()){
                Toast.makeText(this, "Please fill User Name", Toast.LENGTH_SHORT).show();
            }else if(ed_password.getText().toString().isEmpty()){
                Toast.makeText(this, "Please fill password", Toast.LENGTH_SHORT).show();
            }else{
                LoginUser();

            }


        }else if(view == txt_forgot){
            startActivity(new Intent(LoginActivity.this,ForgotPassword.class));

        }

    }



    public void LoginUser() {

        /*progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();*/

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Call<ResponseBody> call = apiInterFace.login(ed_username.getText().toString().trim(),ed_password.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String json = "";

                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if (response.body() == null) {

                    Toast.makeText(LoginActivity.this, "No response from server", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        json = response.body().string();
                        Log.e("Login",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {

                        JSONObject jsonObject = new JSONObject(json);

                         String status = jsonObject.getString("status");
                         if(status.equalsIgnoreCase("success")){
                             JSONObject data = jsonObject.getJSONObject("data");
                             manager.setAddress(data.getString("address"));
                             manager.setBankAccount(data.getString("bank_account"));
                             data.getString("created_at");
                             data.getString("current_work_permit");
                             data.getString("deleted_at");
                             manager.setDob(data.getString("dob"));
                             manager.setDob(data.getString("dob"));
                             data.getString("driver_license");
                             manager.setEmail(data.getString("email"));
                             data.getString("emergency_contact");
                             manager.setId(data.getString("id"));
                             data.getString("ird_number");
                             data.getString("is_deleted");
                             data.getString("kiwi_saver_member");
                             data.getString("master_login_created");
                             manager.setName( data.getString("name"));
                             data.getString("nz_citizen");
                             data.getString("password");
                             data.getString("permanent_resident");
                             manager.setPhoneNo(data.getString("phone_no"));
                             manager.setProfileImg(data.getString("profile_img"));
                             data.getString("tax_code");
                             data.getString("updated_at");
                             manager.setUserType(data.getString("user_type"));
                             data.getString("work_in_nz");

                             startActivity(new Intent(LoginActivity.this,MainDashborad.class));
                             finish();
                         }else{
                             Toast.makeText(LoginActivity.this, ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                         }



                    } catch (JSONException e) {

                        e.printStackTrace();
                        Log.e("exception..",e.toString());

                        if (progressDialog!=null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }


}
