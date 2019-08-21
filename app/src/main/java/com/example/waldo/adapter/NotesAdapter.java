package com.example.waldo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.waldo.R;

import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.Holder> {

    Context context;
    ClickItem clickItem;

    public interface ClickItem{
        void click();
    }

    public void clickListerner(ClickItem clickItem){
        this.clickItem= clickItem;
    }

    public NotesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.notes_adapter,viewGroup, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
      holder.txt_read_more.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(clickItem!=null){
                  clickItem.click();
              }
          }
      });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class Holder extends RecyclerView.ViewHolder{
         LinearLayout itemClick;
         TextView txt_read_more;
      public Holder(@NonNull View itemView) {
          super(itemView);
          ButterKnife.bind(this,itemView);
          itemClick = itemView.findViewById(R.id.item_click);
          txt_read_more = itemView.findViewById(R.id.txt_read_more);
      }
  }
}
