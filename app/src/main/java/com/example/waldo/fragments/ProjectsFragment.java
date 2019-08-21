package com.example.waldo.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.waldo.R;
import com.example.waldo.Utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProjectsFragment extends Fragment {
    @BindView(R.id.layout_completed)
    LinearLayout layout_completed;

    @BindView(R.id.layout_pending)
    LinearLayout layout_pending;

    @BindView(R.id.layout_working)
    LinearLayout layout_working;


    private Unbinder unbinder;
    SessionManager sessionManager;

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
}

