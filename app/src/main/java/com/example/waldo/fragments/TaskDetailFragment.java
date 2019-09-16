package com.example.waldo.fragments;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waldo.R;
import com.example.waldo.Rest.ApiInterFace;
import com.example.waldo.Rest.Rest;
import com.example.waldo.Utils.SessionManager;
import com.example.waldo.adapter.CompleteTaskAdapter;

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

public class TaskDetailFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tittle)
    TextView toolbar_tittle;

    @BindView(R.id.back_button)
    TextView back_button;

    @BindView(R.id.txt_email)
    TextView txt_email;

    @BindView(R.id.txt_site_name)
    TextView txt_site_name;

    @BindView(R.id.txt_site_des)
    TextView txt_site_des;

    @BindView(R.id.txt_site_addess)
    TextView txt_site_addess;

    @BindView(R.id.txt_site_phone_no)
    TextView txt_site_phone_no;

    @BindView(R.id.scan_layout)
    RelativeLayout scan_layout;

    @BindView(R.id.notes_layout)
    RelativeLayout notes_layout;

    @BindView(R.id.location_layout)
    RelativeLayout location_layout;

    @BindView(R.id.upload_layout)
    RelativeLayout upload_layout;

    @BindView(R.id.main_layout)
    LinearLayout main_layout;

    @BindView(R.id.site_layout)
    RelativeLayout site_layout;



    SessionManager sessionManager;
    LinearLayoutManager manager;
    private Unbinder unbinder;
    CompleteTaskAdapter adapter;
    ProgressDialog progressDialog;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.task_detail_fragment,container,false);
        unbinder = ButterKnife.bind(this, view);
        sessionManager =  new SessionManager(getActivity());
        init();
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(){
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(sessionManager.getCategoryName().equalsIgnoreCase("complete")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.light_green));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.green));
        }
        else if(sessionManager.getCategoryName().equalsIgnoreCase("pending")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.title_light_blue));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
        }
        else if(sessionManager.getCategoryName().equalsIgnoreCase("working")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.light_title_bar));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.title_bar));
        }
        toolbar_tittle.setText(sessionManager.getPropertyAddress());

        getpropertyDetail();

    }

    @OnClick ({R.id.notes_layout,R.id.scan_layout,R.id.upload_layout,R.id.location_layout,
            R.id.site_layout,R.id.txt_email})
    void clickAction(View v){

        if(v == scan_layout){
            dialogOpen();

        }else if(v == upload_layout){
            launchFragment(new ImageVideoFragment());
        }else if(v == location_layout){
            launchFragment(new MapFragment());
        }else if(v == notes_layout){
            launchFragment(new ViewAddNotesFragment());
        }else if(v == site_layout){
            launchFragment(new SiteFormsFragment());
        }else if(v == txt_email){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            getActivity().startActivity(intent);
        }
    }

    private void dialogOpen() {
        Dialog dialog =  new Dialog(getActivity());
        dialog.setContentView(R.layout.scan_property_dialog);
        TextView txt_confirm = dialog.findViewById(R.id.txt_confirm);
        TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
        TextView txt_address = dialog.findViewById(R.id.txt_addess);
        txt_address.setText("Do you want to scan in to "+sessionManager.getPropertyAddress()+"?");
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                launchFragment(new ScanPropertyFragment());
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void launchFragment(Fragment fragment){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction().addToBackStack(null);
        ft.replace(R.id.frame_container,fragment);
        ft.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public void getpropertyDetail() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Log.e("id",""+sessionManager.getId());
        Call<ResponseBody> call=null;
        call = apiInterFace.getImages(Rest.BASE_URL+"Api/getproperty/"+sessionManager.getPropertyId());
        //call = apiInterFace.getpropertyDetail("710");
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
                        Log.e("properties",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {

                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.getString("status");
                        if(status.equalsIgnoreCase("success")){
                            main_layout.setVisibility(View.VISIBLE);

                           JSONObject data = jsonObject.getJSONObject("data");
                           data.getString("id");
                           txt_site_name.setText(data.getString("site_name"));
                            txt_site_des.setText(data.getString("site_description"));
                           data.getString("logo");
                            txt_site_addess.setText(data.getString("address"));
                           data.getString("city");
                           data.getString("country");
                           data.getString("site_code");
                           data.getString("user_id");
                           data.getString("zip_code");
                           data.getString("start_date");
                           data.getString("end_date");
                           data.getString("assigned_contact");
                           data.getString("assigned_inspector");
                           data.getString("contact_email");
                           data.getString("contact_phone");
                           data.getString("working_stage");
                           data.getString("site_status");
                           data.getString("is_deleted");
                           data.getString("created_at");
                           data.getString("updated_at");
                           data.getString("deleted_at");
                           data.getString("estimate_high");
                           data.getString("estimate_low");
                           data.getString("latitude");
                           data.getString("longitude");
                           data.getString("site_owner");
                            txt_site_phone_no.setText(data.getString("site_owner_number"));
                            txt_email.setText(data.getString("site_owner_email"));
                           data.getString("current_expenses");
                           data.getString("current_wages");
                           data.getString("visit_report");
                           data.getString("stage_name");
                           data.getString("assignedId");
                           data.getString("property_id");
                           data.getString("assigned_forms");
                           data.getString("todayEvents");
                           data.getString("assignedContactName");
                           data.getString("assignedContactPhno");
                           data.getString("assignedInspectorName");
                           data.getString("assignedInspectorPhno");
                           data.getString("last_in");
                           data.getString("last_out");


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

