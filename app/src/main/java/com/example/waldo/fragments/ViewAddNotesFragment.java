package com.example.waldo.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.waldo.adapter.NotesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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

            toolbar_tittle.setText("71 Archibald Road ,kelson kelton");
        }
        else if(sessionManager.getCategoryName().equalsIgnoreCase("pending")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.title_light_blue));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));

            toolbar_tittle.setText("71 Archibald Road ,kelson kelton");
        }
        else if(sessionManager.getCategoryName().equalsIgnoreCase("working")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.light_title_bar));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.title_bar));

            toolbar_tittle.setText("71 Archibald Road ,kelson kelton");
        }else{
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.dark_red));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_red));
            toolbar_tittle.setText("Notes");
        }




        manager = new LinearLayoutManager(getActivity());
        list_rv.setLayoutManager(manager);
        adapter = new NotesAdapter(getActivity());
        list_rv.setAdapter(adapter);
        listerner();

    }

    private void listerner() {
        adapter.clickListerner(new NotesAdapter.ClickItem() {
            @Override
            public void click() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Testing Note");
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

}

