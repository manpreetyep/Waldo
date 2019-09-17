package com.example.waldo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waldo.R;
import com.example.waldo.Rest.ApiInterFace;
import com.example.waldo.Rest.Rest;
import com.example.waldo.Utils.Constants;
import com.example.waldo.Utils.SessionManager;
import com.example.waldo.adapter.Add_PropertyWorkDescriptionAdapter;
import com.example.waldo.models.PropertyData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_AssignForms extends Fragment {

    @BindView(R.id.back_button)
    TextView back_click;

    @BindView(R.id.text_click_title_button)
    TextView text_click_title_button;

    @BindView(R.id.toolbar_tittle)
    TextView title_text;

    @BindView(R.id.txt_continue)
    TextView txt_continue;

    @BindView(R.id.add_icon)
    ImageView add_icon;

    @BindView(R.id.properties_rv)
    RecyclerView properties_rv;

    Unbinder unbinder;
    SessionManager sessionManager;
    LinearLayoutManager layoutManager;
    Add_PropertyWorkDescriptionAdapter adadpter;

    ProgressDialog progressDialog;
    ArrayList<PropertyData.Forms> formList = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_assign_forms_fragment,container,false);
        unbinder = ButterKnife.bind(this,view);
        sessionManager = new SessionManager(getActivity());
        init();
        return view;
    }
    public void launchFragment(Fragment fragment){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction().addToBackStack(null);
        ft.replace(R.id.frame_container,fragment);
        ft.commit();
    }

    private void init() {
        //back_click.setVisibility(View.VISIBLE);
        text_click_title_button.setText("Next");
        title_text.setText("Add New Property");
        text_click_title_button.setVisibility(View.GONE);
        add_icon.setVisibility(View.GONE);
        text_click_title_button.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(getActivity());
        properties_rv.setLayoutManager(layoutManager);
        adadpter = new Add_PropertyWorkDescriptionAdapter(getActivity(),formList);
        properties_rv.setAdapter(adadpter);
        listerner();
        getPropertyData();
    }

    private void listerner() {
        adadpter.clickListerner(new Add_PropertyWorkDescriptionAdapter.ClickItem() {
            @Override
            public void click(int pos,boolean type) {
                PropertyData.Forms forms = formList.get(pos);
                forms.ischecked = type;
                formList.set(pos,forms);
                adadpter.notifyItemChanged(pos);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    @OnClick({R.id.back_button,R.id.txt_continue})
    void ClickAction(View view) {
        if(view==back_click){

        }else if(view==text_click_title_button){

        }else if(view==txt_continue){

            addNewProperty();
          //  launchFragment(new Add_EstimatedDetail());
        }
    }



    public void addNewProperty() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JSONObject map = new JSONObject();
        JSONArray desArr = new JSONArray();
        JSONArray formArr = new JSONArray();
        try {
            map.put("address", Constants.OWNER_ADDRESS);
            map.put("assign_contact",Constants.OWNER_ADDRESS);
            map.put("assign_inspector",Constants.ASSIGNED_INSPECTOR);
            map.put("city",Constants.OWNER_CITY);
            map.put("country",Constants.OWNER_COUNTRY);
            map.put("estimate_high",Constants.HIGH);
            map.put("estimate_low",Constants.LOW);
            map.put("finish_date",Constants.FINISH_DATE);
            map.put("prop_logo","");
            map.put("site_name",Constants.OWNER_SITE_NAME);
            map.put("site_owner",Constants.OWNER_NAME);
            map.put("site_owner_email",Constants.OWNER_EMAIL);
            map.put("site_owner_number",Constants.OWNER_PHONE);
            map.put("start_date",Constants.START_DATE);
            map.put("user_id",sessionManager.getId());
            map.put("zip_code",Constants.OWNER_ZIP);
            map.put("site_description",desArr);
            map.put("assign_forms",formArr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Call<ResponseBody> call= null;

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), map.toString());
        call = apiInterFace.addNewProperty(body);


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
                        Log.e("add description..",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {

                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.getString("status");

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


    public void getPropertyData() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Call<ResponseBody> call= null;
        call = apiInterFace.getPropertyData(Rest.BASE_URL+"api/getPropertyData");
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
                        Log.e("Images video..",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {
                        formList.clear();
                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.getString("status");
                        if(status.equalsIgnoreCase("success")){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray forms = data.getJSONArray("forms");
                            for(int j=0;j<forms.length();j++){
                                JSONObject object = forms.getJSONObject(j);
                                PropertyData.Forms forms1 = new PropertyData.Forms();
                                forms1.id = object.getString("id");
                                forms1.form_name = object.getString("form_name");
                                forms1.ischecked =false;
                                formList.add(forms1);
                            }
                            adadpter.notifyDataSetChanged();
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
