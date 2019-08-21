package com.example.waldo.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waldo.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;*/

public class ScanPropertyFragment extends Fragment implements SurfaceHolder.Callback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tittle)
    TextView toolbar_tittle;

    @BindView(R.id.back_button)
    TextView back_button;

    @BindView(R.id.surface_view)
    SurfaceView surfaceView;

    Camera camera;
    SurfaceHolder surfaceHolder;


    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.scan_property_fragment,container,false);
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
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.title_bar));
        }

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        start_camera();


       // MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.view_map);

        toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.title_bar));
        toolbar_tittle.setText("scan a property");

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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
          start_camera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
/*
    private void start_camera() {
        try{
            camera = Camera.open();
            Toast.makeText(getActivity(), "hiii  123", Toast.LENGTH_SHORT).show();
        }catch(RuntimeException e){
            Log.e("exception", "init_camera: " + e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        param.setPreviewFrameRate(20);
        param.setPreviewSize(176, 144);
        camera.setParameters(param);
        try {
            Toast.makeText(getActivity(), "hiii  134", Toast.LENGTH_SHORT).show();
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            Log.e("exception 135 ", "init_camera: " + e);
            return;
        }
    }
*/


    private void start_camera() {
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private void stop_camera() {
        camera.stopPreview();
        camera.release();
    }*/

    @Override
    public void onStop() {
        super.onStop();
        //stop_camera();
    }
}

