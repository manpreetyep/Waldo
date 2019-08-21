package com.example.waldo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.waldo.R;
import com.example.waldo.fragments.ProfileFragment;
import com.example.waldo.fragments.ProjectsFragment;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainDashborad extends AppCompatActivity{

    @BindView(R.id.project_layout)
    LinearLayout project_layout;

    @BindView(R.id.home_layout)
    LinearLayout home_layout;

    @BindView(R.id.logout_layout)
    LinearLayout logout_layout;

    @BindView(R.id.img_home)
    ImageView img_home;

    @BindView(R.id.img_project)
    ImageView img_project;

    @BindView(R.id.txt_home)
    TextView txt_home;

    @BindView(R.id.txt_project)
    TextView txt_project;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashborad);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.frame_container,new ProjectsFragment());
        ft.commit();
    }

    @OnClick ({R.id.project_layout,R.id.home_layout,R.id.logout_layout})
    void Click(View view){
        if(view == project_layout){
            img_project.setImageResource(R.mipmap.projects);
            img_home.setImageResource(R.mipmap.home_gray);
            txt_home.setTextColor(getResources().getColor(R.color.grey));
            txt_project.setTextColor(getResources().getColor(R.color.title_bar));
             launchFragment(new ProjectsFragment());
        }else if(view == home_layout){
            img_project.setImageResource(R.mipmap.projects_gray);
            img_home.setImageResource(R.mipmap.home);
            txt_home.setTextColor(getResources().getColor(R.color.title_bar));
            txt_project.setTextColor(getResources().getColor(R.color.grey));
            launchFragment(new ProfileFragment());
        }else if(view == logout_layout){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainDashborad.this);
            builder.setTitle("Waldo");
            builder.setMessage("Do you want to Log out ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    startActivity(new Intent(MainDashborad.this,LoginActivity.class));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
    }



    public void launchFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.frame_container,fragment);
        ft.commit();
     }


}
