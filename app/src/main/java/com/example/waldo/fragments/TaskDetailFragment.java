package com.example.waldo.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
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

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
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
import com.example.waldo.Utils.Constants;
import com.example.waldo.Utils.SessionManager;
import com.example.waldo.adapter.CompleteTaskAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDetailFragment extends Fragment implements View.OnClickListener {

    RelativeLayout  notes_layout,scan_layout,site_layout,location_layout,upload_layout;
    LinearLayout main_layout;
    Toolbar toolbar;
    TextView back_button,toolbar_tittle,txt_email,txt_site_name,txt_site_des,
            txt_site_addess,txt_site_phone_no;
    TextView scan_in_text;
    SessionManager sessionManager;
    LinearLayoutManager manager;
    private Unbinder unbinder;
    CompleteTaskAdapter adapter;
    ProgressDialog progressDialog;
    String log_ID="",vv="",endTime,startTime,timeDiff;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.task_detail_fragment,container,false);

        scan_in_text = view.findViewById(R.id.scan_in_text);
        sessionManager =  new SessionManager(getActivity());
        init(view);
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
    private void init(View  view){
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        toolbar = view.findViewById(R.id.toolbar);
        notes_layout = view.findViewById(R.id.notes_layout);
        scan_layout = view.findViewById(R.id.scan_layout);
        site_layout = view.findViewById(R.id.site_layout);
        location_layout = view.findViewById(R.id.location_layout);
        upload_layout = view.findViewById(R.id.upload_layout);
        main_layout = view.findViewById(R.id.main_layout);
        back_button = view.findViewById(R.id.back_button);
        toolbar_tittle = view.findViewById(R.id.toolbar_tittle);
        txt_email = view.findViewById(R.id.txt_email);
        txt_site_name = view.findViewById(R.id.txt_site_name);
        txt_site_des = view.findViewById(R.id.txt_site_des);
        txt_site_addess = view.findViewById(R.id.txt_site_addess);
        txt_site_phone_no = view.findViewById(R.id.txt_site_phone_no);


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

        if(Constants.VALUE.isEmpty()){
            scan_layout.setBackgroundResource(R.drawable.scan_in_property_back);
            scan_in_text.setText("SCAN IN TO PROPERTY");
        }else if(Constants.VALUE.equalsIgnoreCase("match") && Constants.GET_PROP_ID.equalsIgnoreCase(sessionManager.getPropertyId())){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                scan_layout.setBackgroundResource(R.drawable.working_back);
            }
            scan_in_text.setText("LOGOUT OF PROPERTY");
        }


        notes_layout.setOnClickListener(this);
        scan_layout.setOnClickListener(this);
        upload_layout.setOnClickListener(this);
        location_layout.setOnClickListener(this);
        site_layout.setOnClickListener(this);
        txt_email.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if(v == scan_layout){
            if(Constants.VALUE.isEmpty()){
                dialogOpen();
            }else if(Constants.VALUE.equalsIgnoreCase("match")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                endTime = sdf.format(new Date());
                logoutDialog();
            }

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



    private void logoutDialog() {
        Dialog dialog =  new Dialog(getActivity());
        dialog.setContentView(R.layout.logout_property_dialog);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.RIGHT|Gravity.LEFT);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.x = 70; // left margin
        layoutParams.y = 70; // bottom margin
        dialog.getWindow().setAttributes(layoutParams);
        TextView txt_property = dialog.findViewById(R.id.txt_property);
        TextView txt_confirm = dialog.findViewById(R.id.txt_confirm);
        TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
        TextView txt_time_in = dialog.findViewById(R.id.txt_time_in);
        TextView txt_time_out = dialog.findViewById(R.id.txt_time_out);
        TextView txt_total_time = dialog.findViewById(R.id.txt_total_time);
        LinearLayout two_button_lay = dialog.findViewById(R.id.two_button_lay);
        txt_time_in.setText(startTime);
        txt_time_out.setText(endTime);

        String  starttime = startTime.substring(11,16);
        int startHours = Integer.parseInt(starttime.substring(0,2));
        int startMin = Integer.parseInt(starttime.substring(3,5));

        String  endtime = endTime.substring(11,16);
        int endHours = Integer.parseInt(endtime.substring(0,2));
        int endMin = Integer.parseInt(endtime.substring(3,5));

        int diffHours = endHours - startHours;
        int diffMin  = endMin - startMin;

        txt_total_time.setText(diffHours +"hr "+diffMin+"min");


        two_button_lay.setVisibility(View.VISIBLE);
        txt_property.setText(sessionManager.getPropertyAddress());
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                endLog();
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
    private void showStatus() {
        Dialog dialog =  new Dialog(getActivity());
        dialog.setContentView(R.layout.logout_status);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setGravity(Gravity.RIGHT|Gravity.LEFT);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.x = 70; // left margin
        layoutParams.y = 70; // bottom margin
        dialog.getWindow().setAttributes(layoutParams);
        TextView ok = dialog.findViewById(R.id.txt_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void dialogOpen() {
        Dialog dialog =  new Dialog(getActivity());
        dialog.setContentView(R.layout.scan_property_dialog);
        TextView txt_confirm = dialog.findViewById(R.id.txt_confirm);
        TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
        TextView txt_address = dialog.findViewById(R.id.txt_addess);
        LinearLayout single_button_lay = dialog.findViewById(R.id.single_button_lay);
        LinearLayout two_button_lay = dialog.findViewById(R.id.two_button_lay);
        two_button_lay.setVisibility(View.VISIBLE);
        single_button_lay.setVisibility(View.GONE);
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
        //ft.remove(new TaskDetailFragment());
        fragment.setTargetFragment(this, 10);
        ft.replace(R.id.frame_container,fragment);
        ft.commit();
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
                           Log.e("log in",""+data.getString("last_in"));
                           Log.e("log last_out",""+data.getString("last_out"));


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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(data.hasExtra("data")){
                vv = data.getStringExtra("data");
                /*if(vv.equalsIgnoreCase("match")){
                    vv = data.getStringExtra("data");
                    scan_layout.setBackgroundResource(R.drawable.working_back);
                    scan_in_text.setText("LOGOUT OF PROPERTY");
                    startLog();
                }else{

                }*/
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(Constants.VALUE.equalsIgnoreCase("match")){
            startLog();
        }
    }

    private void startLog() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        startTime = sdf.format(new Date());
        Log.e("Current","Date:"+startTime);
       /* progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();*/
        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Call<ResponseBody> call= null;
        call = apiInterFace.startlog(sessionManager.getId(),sessionManager.getPropertyId(),startTime);
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
                        Log.e("start log data....",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {
                        // list.clear();
                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.getString("status");
                        if(status.equalsIgnoreCase("success")){
                            log_ID = jsonObject.getString("data");
                        }

                       // endLog();
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


    private void endLog() {

        Log.e("log_ID"," :"+log_ID);
       /* progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();*/
        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Call<ResponseBody> call= null;
        call = apiInterFace.endlog(sessionManager.getId(),sessionManager.getPropertyId(),endTime,log_ID);
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
                        Log.e("End log data....",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {
                        // list.clear();
                        JSONObject jsonObject = new JSONObject(json);
                        if(jsonObject.getString("status").equalsIgnoreCase("success")){
                            showStatus();
                            Constants.VALUE = "";
                            scan_layout.setBackgroundResource(R.drawable.scan_in_property_back);
                            scan_in_text.setText("SCAN IN TO PROPERTY");

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

