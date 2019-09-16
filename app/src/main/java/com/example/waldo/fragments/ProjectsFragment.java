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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsFragment extends Fragment {

    @BindView(R.id.layout_completed)
    LinearLayout layout_completed;

    @BindView(R.id.layout_pending)
    LinearLayout layout_pending;

    @BindView(R.id.layout_working)
    LinearLayout layout_working;

    @BindView(R.id.txt_complete_count)
    TextView txt_complete_count;

    @BindView(R.id.txt_pending_count)
    TextView txt_pending_count;

    @BindView(R.id.txt_working_count)
    TextView txt_working_count;

    Unbinder unbinder;
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.projects_fragment,container,false);
        unbinder = ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getActivity());
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.light_title_bar));
        }
        dashboard();
        //yourView.getBackground().setAlpha(127);
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @OnClick ({R.id.layout_completed,R.id.layout_pending,R.id.layout_working})
     void clickAction(View view){
        if(view == layout_completed){
            sessionManager.setCategoryName("complete");
            launchFragment(new CompletedFragment());
        }else if(view == layout_pending){
            sessionManager.setCategoryName("pending");
            launchFragment(new PendingFragment());
        }else if(view == layout_working){
            sessionManager.setCategoryName("working");
            launchFragment(new WorkingFragment());
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

    public void dashboard() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Log.e("id",""+sessionManager.getId());


        Call<ResponseBody> call = apiInterFace.dashboard(sessionManager.getId());
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
                        Log.e("dashboard",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {

                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.getString("status");
                        if(status.equalsIgnoreCase("success")){
                            JSONObject data = jsonObject.getJSONObject("data");
                            String pending = data.getString("pending_quotestates");
                            String working = data.getString("working");
                            String complete = data.getString("complete");
                            String not_accepted = data.getString("not_accepted");
                            String due = data.getString("due");
                            txt_pending_count.setText(pending);
                            txt_complete_count.setText(complete);
                            txt_working_count.setText(working);

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

