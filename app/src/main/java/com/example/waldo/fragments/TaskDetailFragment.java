package com.example.waldo.fragments;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.waldo.R;
import com.example.waldo.Utils.SessionManager;
import com.example.waldo.adapter.CompleteTaskAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TaskDetailFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tittle)
    TextView toolbar_tittle;

    @BindView(R.id.back_button)
    TextView back_button;

    @BindView(R.id.scan_layout)
    RelativeLayout scan_layout;

    @BindView(R.id.notes_layout)
    RelativeLayout notes_layout;

    @BindView(R.id.location_layout)
    RelativeLayout location_layout;

    @BindView(R.id.upload_layout)
    RelativeLayout upload_layout;

    @BindView(R.id.site_layout)
    RelativeLayout site_layout;



    SessionManager sessionManager;
    LinearLayoutManager manager;
    private Unbinder unbinder;
    CompleteTaskAdapter adapter;

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
        toolbar_tittle.setText("71 Archibald Road ,kelson kelton");


    }

    @OnClick ({R.id.notes_layout,R.id.scan_layout,R.id.upload_layout,R.id.location_layout,
            R.id.site_layout})
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
        }
    }

    private void dialogOpen() {
        Dialog dialog =  new Dialog(getActivity());
        dialog.setContentView(R.layout.scan_property_dialog);
        TextView txt_confirm = dialog.findViewById(R.id.txt_confirm);
        TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
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

}

