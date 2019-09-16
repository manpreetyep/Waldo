package com.example.waldo.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waldo.R;
import com.example.waldo.Rest.ApiInterFace;
import com.example.waldo.Rest.Rest;
import com.example.waldo.Utils.SessionManager;
import com.example.waldo.adapter.NotesAdapter;
import com.example.waldo.models.ProfileNotesModel;

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

public class ViewAddNotesFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tittle)
    TextView toolbar_tittle;

    @BindView(R.id.back_button)
    TextView back_button;

    @BindView(R.id.list_rv)
    RecyclerView list_rv;

    @BindView(R.id.add_notes_layout)
    RelativeLayout add_notes_layout;

    LinearLayoutManager manager;
    private Unbinder unbinder;
    NotesAdapter adapter;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    ArrayList<ProfileNotesModel> list = new ArrayList<>();
    String type="",propertyId="";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.view_add_notes_fragment,container,false);
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(){
       // back_button.setVisibility(View.VISIBLE);
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);



        if(sessionManager.getCategoryName().equalsIgnoreCase("complete")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.light_green));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.green));
            type="property";
            toolbar_tittle.setText(sessionManager.getPropertyAddress());
        }
        else if(sessionManager.getCategoryName().equalsIgnoreCase("pending")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.title_light_blue));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
            type="property";
            toolbar_tittle.setText(sessionManager.getPropertyAddress());
        }
        else if(sessionManager.getCategoryName().equalsIgnoreCase("working")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.light_title_bar));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.title_bar));
            type="property";
            toolbar_tittle.setText(sessionManager.getPropertyAddress());
        }else{
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.dark_red));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_red));
            toolbar_tittle.setText("Notes");
            type="profile";
        }




        manager = new LinearLayoutManager(getActivity());
        list_rv.setLayoutManager(manager);
        adapter = new NotesAdapter(getActivity(),list);
        list_rv.setAdapter(adapter);
        //GetProfileNotes(type);
        listerner();

    }

    private void listerner() {
        adapter.clickListerner(new NotesAdapter.ClickItem() {
            @Override
            public void click(int i) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(list.get(i).description);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
/*
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });*/

                builder.show();
            }
        });
    }

    @OnClick ({R.id.add_notes_layout})
    void onClick(View v){
        if(v==add_notes_layout){
            launchFragment(new AddNotesFragment());
        }
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

    public void GetProfileNotes(String type) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", sessionManager.getId());
        map.put("user_type", sessionManager.getUserType());

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Call<ResponseBody> call= null;
        if(type.equalsIgnoreCase("profile"))
            call = apiInterFace.getProfileNotes(sessionManager.getId(),sessionManager.getUserType());
        else{
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), new JSONObject(map).toString());
            call = apiInterFace.getNotes(Rest.BASE_URL + "Api/getNotes/"+sessionManager.getPropertyId()+"/"+sessionManager.getId());
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
                        Log.e("Notes..",json);
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
                                JSONObject object = data.getJSONObject(j);
                                ProfileNotesModel model = new ProfileNotesModel();
                                model.admin_id =object.getString("admin_id");
                                model.created_at =object.getString("created_at");
                                model.deleted_at =object.getString("deleted_at");
                                model.description =object.getString("description");
                                model.is_deleted =object.getString("is_deleted");
                                model.status =object.getString("status");
                                model.updated_at =object.getString("updated_at");
                                model.user_id =object.getString("user_id");
                                list.add(model);
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

    @Override
    public void onResume() {
        super.onResume();

        GetProfileNotes(type);

    }
}

