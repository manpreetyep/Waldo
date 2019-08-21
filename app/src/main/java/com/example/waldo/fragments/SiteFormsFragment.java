package com.example.waldo.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import com.example.waldo.R;
import com.example.waldo.adapter.MyDocumentAdapter;
import com.example.waldo.adapter.SiteFormAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SiteFormsFragment extends Fragment {

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
    SiteFormAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.site_forms_fragment,container,false);
        unbinder = ButterKnife.bind(this, view);
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
        //back_button.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
        toolbar_tittle.setText("Site Forms");
        manager = new LinearLayoutManager(getActivity());
        tasks_rv.setLayoutManager(manager);
        adapter = new SiteFormAdapter(getActivity());
        tasks_rv.setAdapter(adapter);
        listerner();

    }

    private void listerner() {
        adapter.clickListerner(new SiteFormAdapter.ClickItem() {
            @Override
            public void click() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction().addToBackStack(null);
                ft.replace(R.id.frame_container,new SiteFormDetailFragment());
                ft.commit();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

