package com.example.waldo.fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waldo.R;
import com.example.waldo.Rest.ApiInterFace;
import com.example.waldo.Rest.Rest;
import com.example.waldo.Utils.SessionManager;
import com.example.waldo.adapter.CompleteTaskAdapter;
import com.example.waldo.models.PropertyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tittle)
    TextView toolbar_tittle;

    @BindView(R.id.back_button)
    TextView back_button;

    @BindView(R.id.tasks_rv)
    RecyclerView tasks_rv;

    LinearLayoutManager manager;
    private Unbinder unbinder;
    CompleteTaskAdapter adapter;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    ArrayList<PropertyModel> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.pending_fregment,container,false);
        unbinder = ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getActivity());
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
    private void init(){
        Window window = getActivity().getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.blue));
        }
       // back_button.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
        toolbar_tittle.setText("Pending quote");

        manager = new LinearLayoutManager(getActivity());
        tasks_rv.setLayoutManager(manager);
        adapter = new CompleteTaskAdapter(getActivity(),list);
        tasks_rv.setAdapter(adapter);
        getproperty();
        listerer();

    }
    void listerer() {
        adapter.clickListerner(new CompleteTaskAdapter.ClickItem() {
            @Override
            public void click(int p) {
                sessionManager.setPropertyId(list.get(p).id);
                sessionManager.setPropertyAddress(list.get(p).site_name);
                launchFragment(new TaskDetailFragment());
            }
        });
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
    public void getproperty() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Log.e("id",""+sessionManager.getId());


        Call<ResponseBody> call=null;
        if(sessionManager.getUserType().equalsIgnoreCase("admin")){
            Log.e("properties"," admin ");
            call = apiInterFace.propertiesAdmin(sessionManager.getId(),sessionManager.getCategoryName());
        }else{
            Log.e("properties"," emp ");
            call = apiInterFace.getpropertiesEmployee(sessionManager.getId(),sessionManager.getCategoryName());
        }
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
                        list.clear();
                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.getString("status");
                        if(status.equalsIgnoreCase("success")){
                            JSONArray data = jsonObject.getJSONArray("data");
                            for(int j=0;j<data.length();j++){
                                PropertyModel  propertyModel = new PropertyModel();
                                JSONObject object = data.getJSONObject(j);
                                propertyModel.id = object.getString("id");
                                propertyModel.site_name = object.getString("site_name");
                                propertyModel.address = object.getString("address");
                                propertyModel.city = object.getString("city");
                                propertyModel.country = object.getString("country");
                                list.add(propertyModel);
                            }
                            adapter.notifyDataSetChanged();


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

