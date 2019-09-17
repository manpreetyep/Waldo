package com.example.waldo.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waldo.R;
import com.example.waldo.Rest.ApiInterFace;
import com.example.waldo.Rest.Rest;
import com.example.waldo.Utils.Constants;
import com.example.waldo.Utils.SessionManager;
import com.example.waldo.models.PropertyData;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_EstimatedDetail extends Fragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.back_button)
    TextView back_click;

    @BindView(R.id.text_click_title_button)
    TextView text_click_title_button;

    @BindView(R.id.toolbar_tittle)
    TextView title_text;

    @BindView(R.id.txt_continue)
    TextView txt_continue;

    @BindView(R.id.ed_start_date)
    EditText ed_start_date;

    @BindView(R.id.ed_finish_date)
    EditText ed_finish_date;

    @BindView(R.id.ed_esti_high)
    EditText ed_esti_high;

    @BindView(R.id.ed_esti_low)
    EditText ed_esti_low;

    @BindView(R.id.ed_assigned_supervisor)
    EditText ed_assigned_supervisor;

    @BindView(R.id.ed_assigned_inspector)
    EditText ed_assigned_inspector;

    @BindView(R.id.add_icon)
    ImageView add_icon;

    Unbinder unbinder;
    Calendar myCalendar = Calendar.getInstance();
    String selectDate,type,typeDialog;
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    ArrayList<PropertyData.Supervisor> supervisorList = new ArrayList<>();
    ArrayList<PropertyData.Inspector> inspectorList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_estimated_detail,container,false);
        unbinder = ButterKnife.bind(this,view);
        sessionManager = new SessionManager(getActivity());
        Log.e("dfdfd",""+sessionManager.getFormArr().size());
        init();
        return view;
    }

    private void init() {
        //back_click.setVisibility(View.VISIBLE);
        text_click_title_button.setText("Next");
        title_text.setText("Add New Property");
        text_click_title_button.setVisibility(View.GONE);
        add_icon.setVisibility(View.GONE);
        getPropertyData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void launchFragment(Fragment fragment){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction().addToBackStack(null);
        ft.replace(R.id.frame_container,fragment);
        ft.commit();
    }

    @OnClick({R.id.ed_start_date,R.id.ed_finish_date,R.id.txt_continue
            ,R.id.ed_assigned_supervisor,R.id.ed_assigned_inspector})
    void ClickAction(View view) {
        if(view==back_click){

        }if(view==ed_assigned_supervisor){
            typeDialog = "supervisor";
            listDialog();

        }if(view==ed_assigned_inspector){
            typeDialog = "inspector";
            listDialog();
        }if(view==txt_continue){

            if(ed_esti_high.getText().toString().isEmpty()){

            }else if(ed_esti_low.getText().toString().isEmpty()){

            }else if(ed_start_date.getText().toString().isEmpty()){

            }else if(ed_finish_date.getText().toString().isEmpty()){

            }else if(ed_assigned_inspector.getText().toString().isEmpty()){

            }else if(ed_assigned_supervisor.getText().toString().isEmpty()){

            }else{
                launchFragment(new Add_AssignForms());
            }


        }else if(view==ed_start_date){
            type = "start";
            new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }else if(view==ed_finish_date){
            type = "finish";
            new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        String myFormat2 = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
//        if (txt_date != null)
        if (myCalendar.getTimeInMillis() > System.currentTimeMillis()) {
            Toast.makeText(getActivity(), "Invalid Date", Toast.LENGTH_SHORT).show();
            ed_start_date.setText("Select Date");

        } else {
            if(type.equalsIgnoreCase("start")){
                selectDate = sdf.format(myCalendar.getTime());
                ed_start_date.setText(sdf.format(myCalendar.getTime()));
                Log.e("Date format  "," "+sdf2.format(myCalendar.getTime()));
                Constants.START_DATE = sdf2.format(myCalendar.getTime());
            }else if(type.equalsIgnoreCase("finish")){
                selectDate = sdf.format(myCalendar.getTime());
                ed_finish_date.setText(sdf.format(myCalendar.getTime()));
                Constants.FINISH_DATE = sdf2.format(myCalendar.getTime());
            }


        }
    }


    public void getPropertyData() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Call<ResponseBody> call= null;
        call = apiInterFace.getPropertyData(Rest.BASE_URL+"api/getPropertyData");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String json = "";
                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (response.body() == null) {
                    Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        json = response.body().string();
                        Log.e("Images video..",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {
                        supervisorList.clear();
                        inspectorList.clear();

                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.getString("status");
                        if(status.equalsIgnoreCase("success")){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray supervisors = data.getJSONArray("supervisors");
                            JSONArray inspectors = data.getJSONArray("inspectors");
                            for(int j=0;j<supervisors.length();j++){
                                JSONObject object = supervisors.getJSONObject(j);
                                PropertyData.Supervisor supervisor = new PropertyData.Supervisor();
                                supervisor.id = object.getString("id");
                                supervisor.name = object.getString("name");
                                supervisorList.add(supervisor);
                            }

                            for(int j=0;j<inspectors.length();j++){
                                JSONObject object = inspectors.getJSONObject(j);
                                PropertyData.Inspector inspector = new PropertyData.Inspector();
                                inspector.id = object.getString("id");
                                inspector.name = object.getString("name");
                                inspectorList.add(inspector);
                            }

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                        Log.e("exception..",e.toString());

                        if (progressDialog!=null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    public class NamesAdapter extends RecyclerView.Adapter<NamesAdapter.Holder>{

        //Context context;

         @NonNull
         @Override
         public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View view = LayoutInflater.from(getActivity()).inflate(R.layout.names_adapter,parent,false);
             return new Holder(view);
         }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            if(typeDialog.equalsIgnoreCase("inspector")){
                holder.txt_name.setText(inspectorList.get(position).name);
            }
            else{
                holder.txt_name.setText(supervisorList.get(position).name);
            }

            holder.txt_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(typeDialog.equalsIgnoreCase("inspector")){
                        selectedIdInspector = inspectorList.get(position).id;
                        Constants.ASSIGNED_INSPECTOR = selectedIdInspector;
                        ed_assigned_inspector.setText(inspectorList.get(position).name);
                        dialogSheet.dismiss();
                    }
                    else{
                        selectedIdSupervisor = supervisorList.get(position).id;
                        Constants.ASSIGNED_SUPERVISOR = selectedIdSupervisor;
                        ed_assigned_supervisor.setText(supervisorList.get(position).name);
                        dialogSheet.dismiss();
                    }
                }
            });
        }

         @Override
         public int getItemCount() {
             if(typeDialog.equalsIgnoreCase("inspector"))
                 return inspectorList.size();
             else
                 return supervisorList.size();
         }

       public class Holder extends RecyclerView.ViewHolder{
            TextView txt_name;

            public Holder(@NonNull View itemView) {
                super(itemView);
                txt_name = itemView.findViewById(R.id.txt_name);
            }
        }
     }

    String selectedIdSupervisor="",selectedIdInspector="" ;
    RecyclerView list_rv;
    LinearLayoutManager layoutManager;
    NamesAdapter namesAdapter;
    TextView txt_done,txt_cancel;
    BottomSheetDialog dialogSheet;

    public void listDialog(){
        dialogSheet =  new BottomSheetDialog(getActivity());
        View view =  getActivity().getLayoutInflater().inflate(R.layout.country_select_dialog,null);
        dialogSheet.setContentView(view);
        list_rv = view.findViewById(R.id.list_rv);
        txt_done = view.findViewById(R.id.txt_done);
        txt_cancel = view.findViewById(R.id.txt_cancel);
        layoutManager = new LinearLayoutManager(getActivity());
        list_rv.setLayoutManager(layoutManager);
        namesAdapter = new NamesAdapter();
        list_rv.setAdapter(namesAdapter);

        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equalsIgnoreCase("inspector")){
                    if(selectedIdInspector.isEmpty()){
                        Toast.makeText(getActivity(), "Please select name", Toast.LENGTH_SHORT).show();
                    }else{
                        dialogSheet.dismiss();
                    }
                }else{
                    if(selectedIdSupervisor.isEmpty()){
                        Toast.makeText(getActivity(), "Please select name", Toast.LENGTH_SHORT).show();
                    }else{
                        dialogSheet.dismiss();
                    }
                }


            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSheet.dismiss();
            }
        });

        dialogSheet.show();
    }

}
