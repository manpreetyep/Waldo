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
import com.example.waldo.adapter.MyDocumentAdapter;
import com.example.waldo.models.ProfileNotesModel;

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

public class MyDocumentFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tittle)
    TextView toolbar_tittle;

    @BindView(R.id.back_button)
    TextView back_button;

    @BindView(R.id.tasks_rv)
    RecyclerView tasks_rv;

    LinearLayoutManager manager;
    SessionManager sessionManager;
    private Unbinder unbinder;
    MyDocumentAdapter adapter;
    ProgressDialog progressDialog;
    ArrayList<ProfileNotesModel> list = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_document_fragment,container,false);
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
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.dark_red));
        }
        //back_button.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_red));
        toolbar_tittle.setText("My Documents");
        manager = new LinearLayoutManager(getActivity());
        tasks_rv.setLayoutManager(manager);
        adapter = new MyDocumentAdapter(getActivity());
        tasks_rv.setAdapter(adapter);
        listerner();
        getDocuments();

    }

    private void listerner() {
        adapter.clickListerner(new MyDocumentAdapter.ClickItem() {
            @Override
            public void click() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction().addToBackStack(null);
                ft.replace(R.id.frame_container,new SelectDocumentFragment());
                ft.commit();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getDocuments() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Log.e("id",""+sessionManager.getId());
        Log.e("id",""+sessionManager.getUserType());

        Call<ResponseBody> call = apiInterFace.getProfileDocuments(sessionManager.getId(),sessionManager.getUserType());
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

