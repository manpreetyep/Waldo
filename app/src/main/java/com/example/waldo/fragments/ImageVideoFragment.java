package com.example.waldo.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.waldo.R;
import com.example.waldo.adapter.CompleteTaskAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ImageVideoFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tittle)
    TextView toolbar_tittle;

    @BindView(R.id.back_button)
    TextView back_button;

    @BindView(R.id.list_rv)
    RecyclerView list_rv;

    @BindView(R.id.camera_layout)
    LinearLayout camera_layout;

    @BindView(R.id.video_layout)
    LinearLayout video_layout;

    LinearLayoutManager manager;
    private Unbinder unbinder;
    CompleteTaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.image_video_fragment,container,false);
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
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.dark_red));
        }
       // back_button.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_red));
        toolbar_tittle.setText("Images/Video");
        manager = new LinearLayoutManager(getActivity());
    }

    @OnClick({R.id.camera_layout,R.id.video_layout})
    void onClick(View v){
        if(v==camera_layout){
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, 10);
        }else if(v==video_layout){
            Intent intent = new Intent("android.media.action.VIDEO_CAMERA");
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, 11 );

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

