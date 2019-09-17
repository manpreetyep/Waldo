package com.example.waldo.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.waldo.R;
import com.example.waldo.models.PropertyModel;

import java.util.ArrayList;

public class CompleteTaskAdapter extends RecyclerView.Adapter<CompleteTaskAdapter.Holder> {

   Context context;
   ClickItem clickItem;
    ArrayList<PropertyModel> list;

    public interface ClickItem{
        void click(int p);
    }

    public void clickListerner(ClickItem clickItem){
        this.clickItem= clickItem;
    }

    public CompleteTaskAdapter(Context context,ArrayList<PropertyModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.complete_task_adapter,viewGroup, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.txt_title.setText(list.get(i).site_name);
        holder.txt_des.setText(list.get(i).address);
        holder.item_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clickItem!=null){
                    clickItem.click(i);
                }
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                Fragment myFragment = new TaskDetailFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{

      LinearLayout item_click;
      TextView txt_title,txt_des;

      public Holder(@NonNull View itemView) {
          super(itemView);

          item_click = itemView.findViewById(R.id.item_click);
          txt_title = itemView.findViewById(R.id.txt_title);
          txt_des = itemView.findViewById(R.id.txt_des);
      }
  }

    public  void setFilter(ArrayList<PropertyModel> FilteredDataList) {
        list = FilteredDataList;
        notifyDataSetChanged();
    }
}
