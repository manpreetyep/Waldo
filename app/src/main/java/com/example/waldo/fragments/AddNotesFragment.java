package com.example.waldo.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.waldo.R;
import com.example.waldo.adapter.CompleteTaskAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_notes_fragment,container,false);
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
       // back_button.setVisibility(View.VISIBLE);
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.dark_red));
        }
        toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_red));
        toolbar_tittle.setText("Notes");


    }

    @OnClick({R.id.save_layout,R.id.cancel_layout})
    void clickAction(View view){
        if(view == save_layout){
            if(ed_notes.getText().toString().isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Please write something");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        openFragment();
                    }
                });
                builder.show();
            }else{
                openFragment();
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

}

