package com.example.waldo.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.waldo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tittle)
    TextView toolbar_tittle;

    @BindView(R.id.mail_text)
    TextView mail_text;

    @BindView(R.id.layout_view_add_notes)
    LinearLayout layout_view_add_notes;

    @BindView(R.id.layout_upload_image)
    LinearLayout layout_upload_image;

    @BindView(R.id.layout_mydocument)
    LinearLayout layout_mydocument;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_fragment,container,false);
        unbinder = ButterKnife.bind(this, view);
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.dark_red));
        }
        toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_red));
        toolbar_tittle.setText("My Profile");
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    @OnClick({R.id.layout_mydocument,R.id.mail_text,R.id.layout_upload_image,R.id.layout_view_add_notes})
    void clickAction(View view){
        if(view == layout_mydocument){
            launchFragment(new MyDocumentFragment());
        }else if(view == layout_upload_image){
            launchFragment(new ImageVideoFragment());
        }else if(view == layout_view_add_notes){
            launchFragment(new ViewAddNotesFragment());
        }else if(view == mail_text){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            getActivity().startActivity(intent);
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

