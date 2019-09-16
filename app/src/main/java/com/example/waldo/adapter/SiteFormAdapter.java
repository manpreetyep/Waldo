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

import butterknife.ButterKnife;

public class SiteFormAdapter extends RecyclerView.Adapter<SiteFormAdapter.Holder> {

    Context context;
    ClickItem clickItem;

    public interface ClickItem{
        void click();
    }

    public void clickListerner(ClickItem clickItem){
        this.clickItem= clickItem;
    }

    public SiteFormAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.site_from_list_adapter,viewGroup, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
      holder.itemClick.setOnClickListener(new View.OnClickListener() {
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
        return 0;
    }

    class Holder extends RecyclerView.ViewHolder{
         LinearLayout itemClick;
         TextView txt_title;
      public Holder(@NonNull View itemView) {
          super(itemView);
          ButterKnife.bind(this,itemView);
          itemClick = itemView.findViewById(R.id.item_click);
          txt_title = itemView.findViewById(R.id.txt_title);
      }
  }
}
