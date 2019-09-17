package com.example.waldo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waldo.R;
import com.example.waldo.models.ProfileNotesModel;
import com.example.waldo.models.PropertyData;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class Add_PropertyWorkDescriptionAdapter extends RecyclerView.Adapter<Add_PropertyWorkDescriptionAdapter.Holder> {

    Context context;
    ClickItem clickItem;
    ArrayList<PropertyData.Forms> list;

    public interface ClickItem {
        void click(int pos,boolean type);
    }

    public void clickListerner(ClickItem clickItem) {
        this.clickItem = clickItem;
    }
    // ArrayList<ProfileNotesModel> list
    public Add_PropertyWorkDescriptionAdapter(Context context,ArrayList<PropertyData.Forms> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Add_PropertyWorkDescriptionAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_property_work_description, viewGroup, false);
        return new Add_PropertyWorkDescriptionAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Add_PropertyWorkDescriptionAdapter.Holder holder, int i) {
        holder.txt_name.setText(list.get(i).form_name);
        if(list.get(i).ischecked)
            holder.checkbox.setChecked(true);
        else
            holder.checkbox.setChecked(false);
        holder.item_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(holder.checkbox.isChecked()){
                  holder.checkbox.setChecked(false);
                   clickItem.click(i,false);
               }else{
                   holder.checkbox.setChecked(true);
                   clickItem.click(i,true);
               }
            }
        });
//        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    Toast.makeText(context, ""+isChecked, Toast.LENGTH_SHORT).show();
//                    clickItem.click(i,false);
//                }else{
//                    Toast.makeText(context, ""+isChecked, Toast.LENGTH_SHORT).show();
//                    clickItem.click(i,true);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_date, txt_des;
        RelativeLayout item_click;
        //ImageView img_circle;
        CheckBox checkbox;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            item_click = itemView.findViewById(R.id.item_click);
            txt_name = itemView.findViewById(R.id.txt_name);
            checkbox = itemView.findViewById(R.id.checkbox);
           // img_circle = itemView.findViewById(R.id.img_circle);

        }
    }
}
