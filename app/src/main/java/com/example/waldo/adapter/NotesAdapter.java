package com.example.waldo.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.waldo.R;
import com.example.waldo.Utils.Utils;
import com.example.waldo.models.ProfileNotesModel;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.Holder> {

    Context context;
    ClickItem clickItem;
    ArrayList<ProfileNotesModel> list;

    public interface ClickItem{
        void click(int pos);
    }

    public void clickListerner(ClickItem clickItem){
        this.clickItem= clickItem;
    }

    public NotesAdapter(Context context, ArrayList<ProfileNotesModel> list) {
        this.context = context;
        this.list =list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.notes_adapter,viewGroup, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
      holder.txt_date.setText(Utils.getDate(list.get(i).created_at));
      holder.txt_des.setText(list.get(i).description);
      holder.txt_read_more.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(clickItem!=null){
                  clickItem.click(i);
              }
          }
      });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
         LinearLayout itemClick;
         TextView txt_read_more,txt_date,txt_des;
      public Holder(@NonNull View itemView) {
          super(itemView);
          ButterKnife.bind(this,itemView);
          itemClick = itemView.findViewById(R.id.item_click);
          txt_read_more = itemView.findViewById(R.id.txt_read_more);
          txt_date = itemView.findViewById(R.id.txt_date);
          txt_des = itemView.findViewById(R.id.txt_des);
      }
  }
}
