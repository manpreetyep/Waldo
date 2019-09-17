package com.example.waldo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.waldo.Utils.Constants;
import com.example.waldo.adapter.NamesAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddNewProperty extends Fragment {

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

    @BindView(R.id.ed_ownername)
    EditText ed_ownername;

    @BindView(R.id.ed_ownerphone)
    EditText ed_ownerphone;

    @BindView(R.id.ed_owneremail)
    EditText ed_owneremail;

    @BindView(R.id.ed_site_name)
    EditText ed_site_name;


    @BindView(R.id.ed_zip_code)
    EditText ed_zip_code;

    @BindView(R.id.ed_city)
    EditText ed_city;

    @BindView(R.id.ed_country)
    EditText ed_country;

    @BindView(R.id.ed_address)
    EditText ed_address;

    Unbinder unbinder;

    private final static int PLACE_PICKER_REQUEST = 999;

    String countries[] ={ "New Zealand"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_property,container,false);
        unbinder = ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.light_title_bar));
        }
        //back_click.setVisibility(View.VISIBLE);
        text_click_title_button.setText("Next");
        title_text.setText("Add New Property");
        text_click_title_button.setVisibility(View.GONE);
        add_icon.setVisibility(View.GONE);

    }

    public void launchFragment(Fragment fragment){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction().addToBackStack(null);
        ft.replace(R.id.frame_container,fragment);
        ft.commit();
    }
    @OnClick ({R.id.back_button,R.id.text_click_title_button,R.id.ed_address,R.id.ed_country,R.id.txt_continue})
    void ClickAction(View view) {
        if(view==back_click){

        }else if(view==text_click_title_button){

        }else if(view==ed_country){
            CountryDialog();
        }else if(view==txt_continue){

           // launchFragment(new Add_PropertyWordDescription());

            if(ed_ownername.getText().toString().isEmpty()){

            }else if(ed_ownerphone.getText().toString().isEmpty()){

            }else if(ed_owneremail.getText().toString().isEmpty()){

            }else if(ed_site_name.getText().toString().isEmpty()){

            }else if(ed_address.getText().toString().isEmpty()){

            }else if(ed_zip_code.getText().toString().isEmpty()){

            }else if(ed_city.getText().toString().isEmpty()){

            }else if(ed_country.getText().toString().isEmpty()){

            }else{
                Constants.OWNER_NAME = ed_ownername.getText().toString();
                Constants.OWNER_PHONE = ed_ownerphone.getText().toString();
                Constants.OWNER_EMAIL = ed_owneremail.getText().toString();
                Constants.OWNER_SITE_NAME = ed_site_name.getText().toString();
                Constants.OWNER_ADDRESS = ed_address.getText().toString();
                Constants.OWNER_ZIP = ed_zip_code.getText().toString();
                Constants.OWNER_CITY = ed_city.getText().toString();
                Constants.OWNER_COUNTRY = ed_country.getText().toString();
                launchFragment(new Add_PropertyWordDescription());
            }




        }else if(view==ed_address){
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                // for activty
                //startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                // for fragment
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case PLACE_PICKER_REQUEST:
                    Place place = (Place) PlacePicker.getPlace(getActivity(), data);
                    String placeName = String.format("Place: %s", place.getName());
                    Toast.makeText(getActivity(), ""+placeName, Toast.LENGTH_SHORT).show();
                    double latitude = place.getLatLng().latitude;
                    double longitude = place.getLatLng().longitude;

            }
        }
    }



    public void CountryDialog(){
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.country_select_dialog, null);
        TextView txt_cancel =  sheetView.findViewById(R.id.txt_cancel);
        TextView txt_done =  sheetView.findViewById(R.id.txt_done);
        RecyclerView list_rv = sheetView.findViewById(R.id.list_rv);
        LinearLayoutManager manager =  new LinearLayoutManager(getActivity());
        list_rv.setLayoutManager(manager);
        NamesAdapter adapter = new NamesAdapter(getActivity());
        list_rv.setAdapter(adapter);
        /*ListView list_countries =  sheetView.findViewById(R.id.list_countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,countries);
        list_countries.setAdapter(adapter);*/
        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
