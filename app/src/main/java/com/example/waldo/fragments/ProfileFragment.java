package com.example.waldo.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.waldo.R;
import com.example.waldo.Rest.ApiInterFace;
import com.example.waldo.Rest.Rest;
import com.example.waldo.Utils.SessionManager;
import com.example.waldo.activities.OpenImageVideo;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tittle)
    TextView toolbar_tittle;

    @BindView(R.id.mail_text)
    TextView mail_text;

    @BindView(R.id.txt_name)
    TextView txt_name;

    @BindView(R.id.txt_number)
    TextView txt_number;

    @BindView(R.id.txt_address)
    TextView txt_address;

    @BindView(R.id.layout_view_add_notes)
    LinearLayout layout_view_add_notes;

    @BindView(R.id.layout_upload_image)
    LinearLayout layout_upload_image;

    @BindView(R.id.user_image)
    ImageView user_image;

    @BindView(R.id.layout_mydocument)
    LinearLayout layout_mydocument;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    String userIMage="";

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_fragment,container,false);
        unbinder = ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getActivity());
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.dark_red));
        }
        toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_red));
        toolbar_tittle.setText("My Profile");
        sessionManager.setCategoryName("");
        GetUser();



            user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!userIMage.isEmpty()){
                    startActivity(new Intent(getActivity(), OpenImageVideo.class).putExtra("image_link",userIMage));
                    getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                }
            });


        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    @OnClick({R.id.layout_mydocument,R.id.mail_text,R.id.layout_upload_image,R.id.layout_view_add_notes})
    void clickAction(View view){
        if(view == layout_mydocument){
            launchFragment(new MyDocumentFragment());
        }else if(view == layout_upload_image){
            sessionManager.setCategoryName("");
            launchFragment(new ImageVideoFragment());
        }else if(view == layout_view_add_notes){
            sessionManager.setCategoryName("");
            launchFragment(new ViewAddNotesFragment());
        }else if(view == mail_text){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            getActivity().startActivity(intent);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void launchFragment(Fragment fragment){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction().addToBackStack(null);
        ft.replace(R.id.frame_container,fragment);
        ft.commit();
    }

    public void GetUser() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Log.e("id",""+sessionManager.getId());
        Log.e("id",""+sessionManager.getUserType());

        Call<ResponseBody> call = apiInterFace.getprofile(sessionManager.getId(),sessionManager.getUserType());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String json = "";

                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if (response.body() == null) {

                    Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        json = response.body().string();
                        Log.e("Profile",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {

                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.getString("status");
                        if(status.equalsIgnoreCase("success")){
                            JSONObject data = jsonObject.getJSONObject("data");
                            txt_number.setText(data.getString("phone_no"));
                            txt_name.setText(data.getString("name"));
                            txt_address.setText(data.getString("address"));
                            userIMage= data.getString("profile_img");
                            Log.e("userIMage","in "+userIMage);
                            mail_text.setText(sessionManager.getEmail());
                            Glide.with(getActivity())
                                    .load(data.getString("profile_img"))
                                    .thumbnail(0.5f)
                                    .apply(new RequestOptions().placeholder(R.mipmap.user)
                                    .error(R.mipmap.user))
                                    .into(user_image);


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

