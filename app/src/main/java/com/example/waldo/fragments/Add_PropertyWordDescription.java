package com.example.waldo.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waldo.R;
import com.example.waldo.Utils.SessionManager;
import com.example.waldo.adapter.Add_PropertyWorkDescriptionAdapter;
import com.example.waldo.models.PropertyData;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Add_PropertyWordDescription extends Fragment {

    @BindView(R.id.back_button)
    TextView back_click;

    @BindView(R.id.text_click_title_button)
    TextView text_click_title_button;

    @BindView(R.id.toolbar_tittle)
    TextView title_text;

    @BindView(R.id.txt_continue)
    TextView txt_continue;

    @BindView(R.id.add_icon)
    ImageView add_icon;

    @BindView(R.id.properties_rv)
    RecyclerView properties_rv;

    Unbinder unbinder;
    LinearLayoutManager layoutManager;
    Add_PropertyWorkDescriptionAdapter adadpter;
    ArrayList<PropertyData.Forms> formList = new ArrayList<>();
    ArrayList<String> saveForm = new ArrayList<>();

    String arr[]={"Asbestos Cladding General","Baseboard Cladding","Paneled Cement Board","Gable Ends"
            ,"Gutters and Drains","Super Six Roofing","General Environmental Clean","Limpet"
            ,"Wrapping of Pipes","Soffits","Textured Ceiling","Vinyl Flooring","Others"};


    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_property_work_description,container,false);
        unbinder = ButterKnife.bind(this,view);
        sessionManager = new SessionManager(getActivity());
        init();
        return view;
    }
    public void launchFragment(Fragment fragment){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction().addToBackStack(null);
        ft.replace(R.id.frame_container,fragment);
        ft.commit();
    }

    private void init() {
        //back_click.setVisibility(View.VISIBLE);

        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.light_title_bar));
        }
        text_click_title_button.setText("Next");
        title_text.setText("Add New Property");
        text_click_title_button.setVisibility(View.GONE);
        add_icon.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(getActivity());
        properties_rv.setLayoutManager(layoutManager);
        adadpter = new Add_PropertyWorkDescriptionAdapter(getActivity(),formList);
        properties_rv.setAdapter(adadpter);
        for(int j =0;j<arr.length;j++){
            PropertyData.Forms forms = new PropertyData.Forms();
            forms.id = "";
            forms.ischecked = false;
            forms.form_name = arr[j];
            formList.add(forms);
        }
        adadpter.notifyDataSetChanged();
        listerner();

    }

    private void listerner() {
        adadpter.clickListerner(new Add_PropertyWorkDescriptionAdapter.ClickItem() {
            @Override
            public void click(int pos,boolean type) {
                PropertyData.Forms forms = formList.get(pos);
                forms.ischecked = type;
                formList.set(pos,forms);
                adadpter.notifyItemChanged(pos);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    @OnClick({R.id.back_button,R.id.txt_continue})
    void ClickAction(View view) {
        if(view==back_click){

        }else if(view==text_click_title_button){

        }else if(view==txt_continue){
            saveForm.clear();
            for(int j=0;j<formList.size();j++){
                if(formList.get(j).ischecked){
                    saveForm.add(formList.get(j).form_name);
                    sessionManager.saveDescriptionArr(saveForm);
                    Log.e("is checked",""+formList.get(j).form_name);
                }
            }

            if(saveForm.size() ==0){
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            }else{
                launchFragment(new Add_EstimatedDetail());
            }

        }
    }

}
