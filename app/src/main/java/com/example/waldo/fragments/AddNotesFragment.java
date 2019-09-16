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
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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

public class AddNotesFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tittle)
    TextView toolbar_tittle;

    @BindView(R.id.back_button)
    TextView back_button;

    @BindView(R.id.cancel_layout)
    LinearLayout cancel_layout;

    @BindView(R.id.save_layout)
    LinearLayout save_layout;

    @BindView(R.id.ed_notes)
    EditText ed_notes;

    private Unbinder unbinder;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    String type="";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_notes_fragment,container,false);
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
            type ="property";
            toolbar_tittle.setText("71 Archibald Road ,kelson kelton");
        }
        else if(sessionManager.getCategoryName().equalsIgnoreCase("pending")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.title_light_blue));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
            type ="property";
            toolbar_tittle.setText("71 Archibald Road ,kelson kelton");
        }
        else if(sessionManager.getCategoryName().equalsIgnoreCase("working")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.light_title_bar));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.title_bar));
            type ="property";
            toolbar_tittle.setText("71 Archibald Road ,kelson kelton");
        }else{
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.dark_red));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_red));
            toolbar_tittle.setText("Notes");
            type ="profile";
        }




    }

    @OnClick({R.id.save_layout,R.id.cancel_layout})
    void clickAction(View view){
        if(view == save_layout){
            if(ed_notes.getText().toString().isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Please enter note");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                builder.show();
            }else{

                AddNote(type);


            }


        }else if(view == cancel_layout){
            openFragment();
        }

    }

    void openFragment(){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.remove(new AddNotesFragment());
        ft.replace(R.id.frame_container,new ViewAddNotesFragment());
        ft.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void AddNote(String type) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);

        Call<ResponseBody> call= null;

        if(type.equalsIgnoreCase("profile"))
            call = apiInterFace.addProfileNote(sessionManager.getId(),sessionManager.getUserType(),ed_notes.getText().toString());
        else{
            //need propID
            //call = apiInterFace.addNote(sessionManager.getId(),sessionManager.getPropId(),ed_notes.getText().toString());
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
                        Log.e("add notes..",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {
                       // list.clear();
                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.getString("status");

                        if(status.equalsIgnoreCase("success")){
                            openFragment();
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

